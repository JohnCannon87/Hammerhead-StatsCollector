package com.statscollector.sonar.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.log4j.Logger;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.statscollector.gerrit.model.ConnectionTestResults;
import com.statscollector.sonar.authentication.SonarAuthenticationHelper;
import com.statscollector.sonar.config.SonarConfig;
import com.statscollector.sonar.dao.Cell;
import com.statscollector.sonar.dao.Col;
import com.statscollector.sonar.dao.DatedSonarMetric;
import com.statscollector.sonar.dao.SonarDao;
import com.statscollector.sonar.model.SonarMetric;
import com.statscollector.sonar.model.SonarProject;
import com.statscollector.sonar.model.SonarStatistics;
import com.statscollector.sonar.model.SonarTargetsStatus;
import com.statscollector.sonar.service.filter.FilterProjectNamePredicate;

@Service
public class SonarStatisticsService {

    private static final int PERIOD_LENGTH = 12;

    @Autowired
    private SonarDao sonarDao;

    @Autowired
    private SonarAuthenticationHelper authenticationHelper;

    @Autowired
    private SonarConfig sonarConfig;

    private boolean refreshInProgress = false;

    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendYear(4, 4)
            .appendLiteral("-").appendMonthOfYear(2).appendLiteral("-").appendDayOfMonth(2).toFormatter();

    private static final DateTimeFormatter sonarTimeFormatter = new DateTimeFormatterBuilder().appendYear(4, 4)
            .appendLiteral("-").appendMonthOfYear(2).appendLiteral("-").appendDayOfMonth(2).appendLiteral("T")
            .appendHourOfDay(2).appendLiteral(":").appendMinuteOfHour(2).appendLiteral(":").appendMinuteOfHour(2)
            .appendTimeZoneOffset(null, false, 1, 1).toFormatter();

    final static Logger LOGGER = Logger.getLogger(SonarStatisticsService.class);

    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    private static final String ZERO_POINT_ZERO = "0.0";

    private Map<Interval, Map<SonarProject, Map<String, SonarMetric>>> metricsByProjectAndDate;

    public List<SonarProject> getProjectsFilteredByName(final String projectFilterRegex) throws IOException,
            URISyntaxException {
        return getProjectsFilteredByName(getAllSonarProjects(), projectFilterRegex);
    }

    public List<SonarProject> getProjectsFilteredByName(final Collection<SonarProject> projects,
            final String projectFilterRegex) throws IOException,
            URISyntaxException {
        List<SonarProject> toBeFiltered = new ArrayList<>(projects);
        FilterProjectNamePredicate filter = new FilterProjectNamePredicate(projectFilterRegex);
        List<SonarProject> results = filter.filter(toBeFiltered);
        return results;
    }

    public List<SonarProject> getAllSonarProjects() throws IOException, URISyntaxException {
        String allChanges = sonarDao.getLatestStats(authenticationHelper.credentialsProvider());
        List<SonarProject> result = translateIntoProjectList(allChanges);
        return result;
    }

    private List<SonarProject> translateIntoProjectList(final String allChanges) {
        // Setup Parsers
        JsonParser jsonParser = new JsonParser();
        Gson gsonParser = new GsonBuilder().create();
        List<SonarProject> result = new ArrayList<>();
        JsonElement parsedJson = jsonParser.parse(allChanges);
        JsonArray jsonListOfProjects = parsedJson.getAsJsonArray();
        for(JsonElement jsonProjectElement : jsonListOfProjects) {
            JsonObject jsonProject = jsonProjectElement.getAsJsonObject();
            SonarProject sonarProject = gsonParser.fromJson(jsonProject, SonarProject.class);
            result.add(sonarProject);
        }
        return result;
    }

    public void setSonarDao(final SonarDao sonarDao) {
        this.sonarDao = sonarDao;
    }

    public void setAuthenticationHelper(final SonarAuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
    }

    @Scheduled(fixedRate = 7200000)
    public void getStatisticsScheduledTask() {
        LOGGER.info("Is Sonar Refresh In Progress ?: " + refreshInProgress);
        try {
            if(!refreshInProgress) {
                refreshInProgress = true;
                // Create last 12 month period
                DateTime now = DateTime.now();
                DateTime startPeriod = now.minusMonths(PERIOD_LENGTH).dayOfMonth().withMaximumValue();
                DateTime endPeriod = now.dayOfMonth().withMaximumValue();
                try {
                    List<SonarProject> statisticsForPeriod = getStatisticsForPeriod(startPeriod,
                            endPeriod,
                            sonarConfig.getProjectRegex());
                    List<Interval> periodsList = createPeriodsList(startPeriod, endPeriod);
                    metricsByProjectAndDate = sortMetricsIntoMaps(
                            periodsList, statisticsForPeriod);
                } catch(IOException | URISyntaxException e) {
                    LOGGER.error("Error in processing Sonar Stats", e);
                }

                refreshInProgress = false;
                LOGGER.info("Sonar Refresh Completed");
            }
        } catch(Exception e) {
            LOGGER.error("Error Processing Data From Sonar", e);
            refreshInProgress = false;
            throw e;
        }
    }

    private Map<Interval, Map<String, SonarMetric>> condenseMetricsForAllProjectsIntoOne(
            final Map<Interval, Map<SonarProject, Map<String, SonarMetric>>> metricsByProjectAndDate,
            final String projectFilterRegex) throws IOException, URISyntaxException {
        Map<Interval, Map<String, SonarMetric>> result = new HashMap<>();
        List<Interval> intervals = new ArrayList<>();
        if(null != metricsByProjectAndDate) {
            intervals.addAll(metricsByProjectAndDate.keySet());
            Collections.sort(intervals, (final Interval i1, final Interval i2) -> i1.getStart()
                    .compareTo(i2.getStart()));
            for(Interval interval : intervals) {
                Map<SonarProject, Map<String, SonarMetric>> projectMetrics = metricsByProjectAndDate.get(interval);
                Map<String, SonarMetric> metrics = condenseMetricsForProjects(projectMetrics, projectFilterRegex);
                result.put(interval, metrics);
            }
        }
        return result;
    }

    private Map<String, SonarMetric> condenseMetricsForProjects(
            final Map<SonarProject, Map<String, SonarMetric>> projectMetrics, final String projectFilterRegex)
            throws IOException, URISyntaxException {
        Map<String, SonarMetric> result = new HashMap<>();
        Set<SonarProject> projectsSet = projectMetrics.keySet();

        List<SonarProject> projects = getProjectsFilteredByName(projectsSet, projectFilterRegex);

        for(SonarProject sonarProject : projects) {
            Map<String, SonarMetric> metrics = projectMetrics.get(sonarProject);
            if(metrics != null) {
                Set<String> metricTypes = metrics.keySet();
                for(String string : metricTypes) {
                    SonarMetric condensedMetrics = condenseMetrics(metrics.get(string), result.get(string));
                    if(null != condensedMetrics) {
                        result.put(string, condensedMetrics);
                    }
                }
            }
        }
        return result;
    }

    private SonarMetric condenseMetrics(final SonarMetric currentMetric, SonarMetric newMetric) {
        if(newMetric == null) {
            newMetric = new SonarMetric(currentMetric.getKey(), "0", "0");
        }
        if(!currentMetric.getValue().isEmpty() && !newMetric.getValue().isEmpty()) {
            BigDecimal currentValue = new BigDecimal(currentMetric.getValue());
            BigDecimal newValue = new BigDecimal(newMetric.getValue());
            BigDecimal updatedValue = currentValue.add(newValue);
            return new SonarMetric(currentMetric.getKey(), updatedValue.toString(), null);
        }
        return null;
    }

    private Map<Interval, Map<SonarProject, Map<String, SonarMetric>>> sortMetricsIntoMaps(
            final List<Interval> periodsList,
            final List<SonarProject> statisticsForPeriod) {
        Map<Interval, Map<SonarProject, Map<String, SonarMetric>>> result = new HashMap<>();
        for(Interval interval : periodsList) {
            Map<SonarProject, Map<String, SonarMetric>> projectMetrics = getMetricsForProjectInInterval(interval,
                    statisticsForPeriod);
            result.put(interval, projectMetrics);
        }
        return result;
    }

    private Map<SonarProject, Map<String, SonarMetric>> getMetricsForProjectInInterval(final Interval interval,
            final List<SonarProject> statisticsForPeriod) {
        Map<SonarProject, Map<String, SonarMetric>> result = new HashMap<>();
        for(SonarProject sonarProject : statisticsForPeriod) {
            Map<String, SonarMetric> lastMetricForPeriod = getLastMetricForPeriod(sonarProject, interval);
            result.put(sonarProject, lastMetricForPeriod);
        }
        return result;
    }

    private Map<String, SonarMetric> getLastMetricForPeriod(final SonarProject sonarProject, final Interval interval) {
        List<DateTime> dates = new ArrayList<>();
        dates.addAll(sonarProject.getDatedMetricsMaps().keySet());
        Collections.sort(dates);
        DateTime lastDate = null;
        for(DateTime dateTime : dates) {
            if(interval.contains(dateTime)) {
                if(null == lastDate || dateTime.isAfter(lastDate)) {
                    lastDate = dateTime;
                }
            }
        }
        return sonarProject.getDatedMetricsMaps().get(lastDate);
    }

    /**
     * Creates a list of periods representing the start and end of each month contained within the provided start and
     * end periods.
     *
     * @param startPeriod
     * @param endPeriod
     * @return
     */
    private List<Interval> createPeriodsList(final DateTime startPeriod, final DateTime endPeriod) {
        List<Interval> result = new ArrayList<>();
        DateTime month = startPeriod;
        while(month.isBefore(endPeriod)) {
            Interval interval = new Interval(month.dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue(),
                    month.dayOfMonth()
                            .withMaximumValue().millisOfDay().withMaximumValue());
            result.add(interval);
            month = month.plusMonths(1);
        }
        return result;
    }

    public List<SonarProject> getStatisticsForPeriod(final DateTime startDate, final DateTime endDate,
            final String projectRegex) throws IOException, URISyntaxException {
        // Setup Parsers
        JsonParser jsonParser = new JsonParser();
        Gson gsonParser = new GsonBuilder().create();
        List<SonarProject> projects = getAllSonarProjects();
        DateMidnight startDateMidnight = new DateMidnight(startDate);
        DateMidnight endDateMidnight = new DateMidnight(endDate);
        Interval period = new Interval(startDateMidnight, endDateMidnight);
        for(SonarProject sonarProject : projects) {
            String rawResults = sonarDao.getStatsForDateWindow(authenticationHelper.credentialsProvider(),
                    period, sonarProject.getKey());
            JsonElement rootElement = jsonParser.parse(rawResults);
            JsonArray rootArray = rootElement.getAsJsonArray();
            for(JsonElement jsonElement : rootArray) {
                DatedSonarMetric parsedResults = gsonParser.fromJson(jsonElement, DatedSonarMetric.class);
                sonarProject.setDatedMetricsMaps(translateToDatedMetricsMap(parsedResults));
            }
        }

        return projects;
    }

    private Map<DateTime, Map<String, SonarMetric>> translateToDatedMetricsMap(final DatedSonarMetric parsedResults) {
        Map<DateTime, Map<String, SonarMetric>> results = new HashMap<>();
        // Get List Of Metric Names
        List<Col> metricNames = parsedResults.getCols();
        // Get List of "Cells" (actually key value pairs, key is date, value is
        // list of metrics.
        List<Cell> cells = parsedResults.getCells();
        for(Cell cell : cells) {
            DateTime statsInstance = sonarTimeFormatter.parseDateTime(cell.getD());
            List<Double> statsValues = cell.getV();
            if(statsValues.size() != metricNames.size()) {
                LOGGER.error("Metrics Names And Values Arrays Do NOT Match In Size");
                break;
            }
            Map<String, SonarMetric> statisticsMap = new HashMap<>();
            for(int i = 0; i < statsValues.size(); i++) {
                String value = "";
                if(statsValues.get(i) != null) {
                    value = statsValues.get(i).toString();
                }
                SonarMetric metric = new SonarMetric(metricNames.get(i).getMetric(), value, value);
                statisticsMap.put(metric.getKey(), metric);
            }
            results.put(statsInstance, statisticsMap);
        }
        return results;
    }

    public Map<DateTime, SonarStatistics> getAllStatistics(String projectRegex) throws IOException,
            URISyntaxException {

        if(null == projectRegex || projectRegex.isEmpty()) {
            projectRegex = sonarConfig.getProjectRegex();
        }

        Map<Interval, Map<String, SonarMetric>> condensedMetrics = condenseMetricsForAllProjectsIntoOne(
                metricsByProjectAndDate, projectRegex);

        List<Interval> intervals = new ArrayList<>();
        intervals.addAll(condensedMetrics.keySet());
        Collections.sort(intervals, (final Interval i1, final Interval i2) -> i1.getStart().compareTo(i2.getStart()));

        Map<DateTime, SonarStatistics> result = new HashMap<>();
        for(Interval interval : intervals) {
            Map<String, SonarMetric> map = condensedMetrics.get(interval);
            if(null != map && !map.isEmpty()) {
                result.put(interval.getStart(), calculateSonarStatistics(map));
            }
        }

        return result;
    }

    public SonarStatistics getLatestStatistics(String projectRegex) throws IOException, URISyntaxException {

        if(null == projectRegex || projectRegex.isEmpty()) {
            projectRegex = sonarConfig.getProjectRegex();
        }

        Map<Interval, Map<String, SonarMetric>> condensedMetrics = condenseMetricsForAllProjectsIntoOne(
                metricsByProjectAndDate, projectRegex);

        List<Interval> intervals = new ArrayList<>();
        intervals.addAll(condensedMetrics.keySet());
        Collections.sort(intervals, (final Interval i1, final Interval i2) -> i1.getStart().compareTo(i2.getStart()));
        Interval lastInterval = intervals.get(intervals.size() - 1);
        Map<String, SonarMetric> map = condensedMetrics.get(lastInterval);

        return calculateSonarStatistics(map);
    }

    /**
     * @param map
     * @return
     */
    private SonarStatistics calculateSonarStatistics(final Map<String, SonarMetric> map) {
        if(map.isEmpty()) {
            return null;
        }
        SonarMetric linesOfCode = map.get(SonarMetric.LINES_OF_CODE_KEY);
        SonarMetric complexity = map.get(SonarMetric.COMPLEXITY_KEY);
        SonarMetric numberOfFiles = map.get(SonarMetric.FILES_KEY);
        SonarMetric numberOfMethods = map.get(SonarMetric.METHODS_KEY);
        SonarMetric blockerViolations = map.get(SonarMetric.BLOCKER_VIOLATIONS_KEY);
        SonarMetric criticalViolations = map.get(SonarMetric.CRITICAL_VIOLATIONS_KEY);
        SonarMetric majorViolations = map.get(SonarMetric.MAJOR_VIOLATIONS_KEY);
        SonarMetric minorViolations = map.get(SonarMetric.MINOR_VIOLATIONS_KEY);
        SonarMetric infoViolations = map.get(SonarMetric.INFO_VIOLATIONS_KEY);
        SonarMetric linesToCover = map.get(SonarMetric.LINES_TO_COVER_KEY);
        SonarMetric uncoveredLines = map.get(SonarMetric.UNCOVERED_LINES);

        return calculateSonarStatistics(linesOfCode, complexity, numberOfFiles, numberOfMethods, blockerViolations,
                criticalViolations, majorViolations, minorViolations, infoViolations, linesToCover, uncoveredLines);
    }

    private SonarStatistics calculateSonarStatistics(final SonarMetric linesOfCode, final SonarMetric complexity,
            final SonarMetric numberOfFiles, final SonarMetric numberOfMethods, final SonarMetric blockerViolations,
            final SonarMetric criticalViolations, final SonarMetric majorViolations, final SonarMetric minorViolations,
            final SonarMetric infoViolations, final SonarMetric linesToCover, final SonarMetric uncoveredLines) {
        String methodComplexity = calculateComplexity(complexity, numberOfMethods);
        String fileComplexity = calculateComplexity(complexity, numberOfFiles);
        String rulesCompliance = calculateCompliance(blockerViolations, criticalViolations, majorViolations,
                minorViolations, infoViolations, linesOfCode);
        String testCoverage = calculateTestCoverage(uncoveredLines, linesToCover);

        return new SonarStatistics(methodComplexity, fileComplexity, rulesCompliance, testCoverage,
                new BigDecimal(linesOfCode.getValue()).setScale(0).toString());
    }

    private String calculateCompliance(final SonarMetric blockerViolations, final SonarMetric criticalViolations,
            final SonarMetric majorViolations, final SonarMetric minorViolations, final SonarMetric infoViolations,
            final SonarMetric linesOfCode) {
        BigDecimal weightedBlockerViolationsNumber = new BigDecimal(blockerViolations.getValue())
                .multiply(new BigDecimal(sonarConfig.getBlockerWeighting()));
        BigDecimal weightedCriticalViolationsNumber = new BigDecimal(criticalViolations.getValue())
                .multiply(new BigDecimal(sonarConfig.getCriticalWeighting()));
        BigDecimal weightedMajorViolationsNumber = new BigDecimal(majorViolations.getValue()).multiply(new BigDecimal(
                sonarConfig.getMajorWeighting()));
        BigDecimal weightedMinorViolationsNumber = new BigDecimal(minorViolations.getValue()).multiply(new BigDecimal(
                sonarConfig.getMinorWeighting()));
        BigDecimal weightedInfoViolationsNumber = new BigDecimal(infoViolations.getValue()).multiply(new BigDecimal(
                sonarConfig.getInfoWeighting()));
        BigDecimal linesOfCodeNumber = new BigDecimal(linesOfCode.getValue());

        BigDecimal totalWeightedIssues = weightedBlockerViolationsNumber.add(weightedCriticalViolationsNumber)
                .add(weightedMajorViolationsNumber).add(weightedMinorViolationsNumber)
                .add(weightedInfoViolationsNumber);

        BigDecimal rulesViolationDecimal = totalWeightedIssues.setScale(4)
                .divide(linesOfCodeNumber.setScale(4), RoundingMode.HALF_UP).setScale(4);
        BigDecimal rulesViolationPercentage = ONE_HUNDRED.subtract(rulesViolationDecimal.multiply(ONE_HUNDRED))
                .setScale(2);
        return rulesViolationPercentage.toString();
    }

    private String calculateTestCoverage(final SonarMetric uncoveredLines, final SonarMetric linesToCover) {
        if(null == uncoveredLines || null == linesToCover) {
            return ZERO_POINT_ZERO;
        }
        BigDecimal linesToCoverNumber = new BigDecimal(linesToCover.getValue());
        BigDecimal uncoveredLinesNumber = new BigDecimal(uncoveredLines.getValue());
        BigDecimal linesCoveredNumber = linesToCoverNumber.subtract(uncoveredLinesNumber);
        BigDecimal linesCoveredDecimal = linesCoveredNumber.setScale(4)
                .divide(linesToCoverNumber.setScale(4), RoundingMode.HALF_UP).setScale(4);
        BigDecimal linesCoveredPercentage = linesCoveredDecimal.multiply(ONE_HUNDRED).setScale(2);
        return linesCoveredPercentage.toString();
    }

    private String calculateComplexity(final SonarMetric complexity, final SonarMetric numberOfMethods) {
        BigDecimal complexityNumber = new BigDecimal(complexity.getValue());
        BigDecimal methodsNumber = new BigDecimal(numberOfMethods.getValue());
        return complexityNumber.setScale(2).divide(methodsNumber.setScale(2), RoundingMode.HALF_UP).setScale(2)
                .toString();
    }

    /**
     * I test the Sonar connection returning the raw response to allow debugging.
     *
     * @throws Exception
     */
    public ConnectionTestResults<SonarProject> testConnection() {
        StringBuilder connectionDetails = new StringBuilder();
        Credentials credentials = authenticationHelper.credentialsProvider().getCredentials(AuthScope.ANY);
        connectionDetails.append("Attempting Connection With Username: "
                + credentials.getUserPrincipal().getName() + " and Password: "
                + credentials.getPassword());
        String connectionResult = "";
        try {
            connectionResult = sonarDao.testConnection(authenticationHelper.credentialsProvider());
            List<SonarProject> result = translateIntoProjectList(connectionResult);
            return new ConnectionTestResults<SonarProject>(connectionDetails.toString(), result, null);
        } catch(JsonSyntaxException e) {
            return new ConnectionTestResults<SonarProject>(connectionDetails.toString(), null, connectionResult);
        } catch(Exception e) {
            return new ConnectionTestResults<SonarProject>(connectionDetails.toString(), null, e.toString());
        }
    }

    public SonarTargetsStatus getTargetStatus(final String projectRegex) throws IOException, URISyntaxException {
        SonarStatistics latestStatistics = getLatestStatistics(projectRegex);

        BigDecimal fileComplexity = new BigDecimal(latestStatistics.getFileComplexity());
        BigDecimal methodComplexity = new BigDecimal(latestStatistics.getMethodComplexity());
        BigDecimal rulesCompliance = new BigDecimal(latestStatistics.getRulesCompliance());
        BigDecimal testCoverage = new BigDecimal(latestStatistics.getTestCoverage());
        BigDecimal fileComplexityTarget = new BigDecimal(sonarConfig.getFileComplexityTarget());
        BigDecimal methodComplexityTarget = new BigDecimal(sonarConfig.getMethodComplexityTarget());
        BigDecimal rulesComplianceTarget = new BigDecimal(sonarConfig.getRulesComplianceTarget());
        BigDecimal testCoverageTarget = new BigDecimal(sonarConfig.getTestCoverageTarget());
        boolean hitFileTarget = (fileComplexity.compareTo(fileComplexityTarget) <= 0);
        boolean hitMethodTarget = (methodComplexity.compareTo(methodComplexityTarget) <= 0);
        boolean hitTestTarget = (testCoverage.compareTo(testCoverageTarget) >= 0);
        boolean hitRulesTarget = (rulesCompliance.compareTo(rulesComplianceTarget) >= 0);

        return new SonarTargetsStatus(hitFileTarget, hitMethodTarget, hitTestTarget, hitRulesTarget);
    }
}
