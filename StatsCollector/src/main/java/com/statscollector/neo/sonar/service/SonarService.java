package com.statscollector.neo.sonar.service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonSyntaxException;
import com.statscollector.gerrit.model.ConnectionTestResults;
import com.statscollector.neo.sonar.config.SonarConfig;
import com.statscollector.neo.sonar.dao.SonarProjectRepository;
import com.statscollector.neo.sonar.external.SonarHttpClient;
import com.statscollector.neo.sonar.model.RawSonarMetrics;
import com.statscollector.neo.sonar.model.RawSonarProject;
import com.statscollector.neo.sonar.model.SonarMetric;
import com.statscollector.neo.sonar.model.SonarMetricPeriod;
import com.statscollector.neo.sonar.model.SonarProject;
import com.statscollector.neo.sonar.model.SonarTargets;
import com.statscollector.neo.sonar.model.SonarTargetsStatus;

@Service
public class SonarService {

    private static final Logger LOGGER = Logger.getLogger(SonarService.class);

    @Autowired
    private SonarConfig sonarConfig;

    @Autowired
    private SonarHttpClient sonarHttpClient;

    @Autowired
    private SonarMetricService sonarMetricService;

    @Autowired
    private SonarProjectRepository sonarProjectRepository;

    @Autowired
    private DerivedSonarMetricService derivedSonarMetricService;

    public ConnectionTestResults testConnection() {
        List<RawSonarProject> projectNames = sonarHttpClient.getProjectNames();
        return new ConnectionTestResults<>("Connecting", projectNames, "Error Thrown Check Server Logs");
    }

    /**
     * Refresh all sonar statistics and store in the local database.
     */
    @Scheduled(fixedRate = 7200000)
    @Transactional
    public void refreshAllStatistics() {
        List<SonarProject> sonarProjects = buildListOfSonarProjects();
        for(SonarProject sonarProject : sonarProjects) {
            try {
                RawSonarMetrics rawSonarMetrics = sonarHttpClient
                        .getProjectValuesFromStartOfTimeToNow(sonarProject.getKey());
                sonarMetricService.convertRawSonarValues(rawSonarMetrics, sonarProject);
            } catch(JsonSyntaxException e) {
                LOGGER.error("Error thrown getting sonar data for project: " + sonarProject, e);
            }
        }
        sonarProjectRepository.save(sonarProjects);
    }

    /**
     * I return a sonar project containing the squash metrics for all projects that match the provided filter.
     *
     * @param regexFilter
     * @return
     */
    public SonarProject getSquashedSonarMetricsForAllProjectsWhoseNamesMatchFilter(
            final String regexFilter) {
        List<SonarProject> allProjects = getAllSonarProjectsWhoseNamesMatchFilter(regexFilter);
        return sonarMetricService.squashProjectMetrics(allProjects);
    }

    /**
     * I return a sonar project containing the squash metrics for all projects that match the provided filter.
     * The Derived Metrics in this project are also calculated based on the squashed values.
     *
     * @param regexFilter
     * @return
     */
    public SonarProject getSquashedAndDerivedSonarMetricsForAllProjectsWhoseNamesMatchFilter(
            final String regexFilter) {
        SonarProject sonarProject = getSquashedSonarMetricsForAllProjectsWhoseNamesMatchFilter(regexFilter);
        for(Entry<YearMonth, SonarMetricPeriod> entry : sonarProject.getSonarMetricPeriods().entrySet()) {
            SonarMetricPeriod value = entry.getValue();
            value.setDerivedMetrics(sonarMetricService.deriveMetrics(value));
        }
        return sonarProject;
    }

    /**
     * Returns all projects whose names match the provided regex pattern with their converted values populated.
     *
     * @param regexFilter
     * @return
     */
    public List<SonarProject> getAllSonarProjectsWhoseNamesMatchFilter(final String regexFilter) {
        List<SonarProject> result = new ArrayList<>();
        List<SonarProject> allSonarProjects = sonarProjectRepository.findAll();
        for(SonarProject sonarProject : allSonarProjects) {
            if(sonarProject.getKey().matches(regexFilter)) {
                result.add(sonarProject);
            }
        }
        sonarMetricService.populateActualValuesForSonarProjects(result);
        return result;
    }

    private List<SonarProject> buildListOfSonarProjects() {
        Map<String, SonarProject> sonarProjects = new HashMap<>();
        List<SonarProject> allProjects = sonarProjectRepository.findAll();
        for(SonarProject sonarProject : allProjects) {
            if(sonarProjects.containsKey(sonarProject.getKey())) {
                LOGGER.fatal("Error duplicate name for Sonar Project found");
            }
            sonarProjects.put(sonarProject.getKey(), sonarProject);
        }
        List<RawSonarProject> sonarProjectNames = sonarHttpClient.getProjectNames();
        for(RawSonarProject rawSonarProject : sonarProjectNames) {
            if(!sonarProjects.containsKey(rawSonarProject.getKey())) {
                SonarProject sonarProject = new SonarProject();
                sonarProject.setKey(rawSonarProject.getKey());
                sonarProject.setName(rawSonarProject.getName());
                sonarProjects.put(rawSonarProject.getKey(), sonarProject);
            }
        }

        return new ArrayList<>(sonarProjects.values());
    }

    /**
     * Gets the squashed and derived metrics for the given project regex and then returns if they meet the standard
     * metric targets passed in.
     *
     * @param projectRegex
     * @param sonarTargets
     * @return
     */
    public SonarTargetsStatus getTargetStatus(final String projectRegex, final SonarTargets sonarTargets) {
        SonarProject sonarProject = getSquashedAndDerivedSonarMetricsForAllProjectsWhoseNamesMatchFilter(projectRegex);
        ArrayList<YearMonth> keyList = new ArrayList<>(sonarProject.getSonarMetricPeriods().keySet());
        Collections.sort(keyList);
        SonarMetricPeriod latestMetric = sonarProject.getSonarMetricPeriods().get(keyList.get(keyList.size() - 1));
        if(latestMetric != null) {
            Map<String, SonarMetric> metricsMap = derivedSonarMetricService
                    .convertListToMap(latestMetric.getDerivedMetrics());
            BigDecimal fileComplexity = new BigDecimal(
                    metricsMap.get(DerivedSonarMetricService.AVERAGE_FILE_COMPLEXITY_KEY).getRawValue());
            BigDecimal methodComplexity = new BigDecimal(
                    metricsMap.get(DerivedSonarMetricService.AVERAGE_METHOD_COMPLEXITY_KEY).getRawValue());
            BigDecimal rulesCompliance = new BigDecimal(
                    metricsMap.get(DerivedSonarMetricService.RULES_COMPLIANCE_KEY).getRawValue());
            BigDecimal testCoverage = new BigDecimal(
                    metricsMap.get(DerivedSonarMetricService.TEST_COVERAGE_KEY).getRawValue());
            BigDecimal fileComplexityTarget = getTarget(sonarTargets.getSonarFileTarget(),
                    sonarConfig.getFileComplexityTarget());
            BigDecimal methodComplexityTarget = getTarget(sonarTargets.getSonarMethodTarget(),
                    sonarConfig.getMethodComplexityTarget());
            BigDecimal rulesComplianceTarget = getTarget(sonarTargets.getSonarRulesTarget(),
                    sonarConfig.getRulesComplianceTarget());
            BigDecimal testCoverageTarget = getTarget(sonarTargets.getSonarTestTarget(),
                    sonarConfig.getTestCoverageTarget());
            boolean hitFileTarget = fileComplexity.compareTo(fileComplexityTarget) <= 0;
            boolean hitMethodTarget = methodComplexity.compareTo(methodComplexityTarget) <= 0;
            boolean hitTestTarget = testCoverage.compareTo(testCoverageTarget) >= 0;
            boolean hitRulesTarget = rulesCompliance.compareTo(rulesComplianceTarget) >= 0;

            return new SonarTargetsStatus(hitFileTarget, hitMethodTarget, hitTestTarget, hitRulesTarget);
        }
        return new SonarTargetsStatus(false, false, false, false);
    }

    private BigDecimal getTarget(final String urlTarget, final String configTarget) {
        if(StringUtils.isEmpty(urlTarget)) {
            return new BigDecimal(configTarget);
        } else {
            return new BigDecimal(urlTarget);
        }
    }

}
