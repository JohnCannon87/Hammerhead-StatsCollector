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
import com.statscollector.neo.sonar.model.SonarTargetsStatus;
import com.statscollector.targets.dao.SonarDisplayParametersRepository;
import com.statscollector.targets.model.SonarDisplayParameters;
import com.statscollector.targets.model.SonarTargetSettings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SonarService {

    private static final Logger LOGGER = Logger.getLogger(SonarService.class);

    private static final String TREND_UP = "up";

    private static final String TREND_DOWN = "down";

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

    @Autowired
    private SonarDisplayParametersRepository sonarDisplayParametersRepository;

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
            final List<SonarProject> allProjects) {
        SonarProject result = sonarMetricService.squashProjectMetrics(allProjects);
        return result;
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
        List<SonarProject> projects = getAllSonarProjectsWhoseNamesMatchFilter(regexFilter);
        SonarProject sonarProject = getSquashedSonarMetricsForAllProjectsWhoseNamesMatchFilter(projects);
        for(Entry<YearMonth, SonarMetricPeriod> entry : sonarProject.getSonarMetricPeriods().entrySet()) {
            SonarMetricPeriod value = entry.getValue();
            value.setDerivedMetrics(sonarMetricService.deriveMetrics(value));
        }
        StringBuilder builder = new StringBuilder();
        for(SonarProject squishedProject : projects) {
            builder.append(squishedProject.getName() + ",");
        }
        builder.deleteCharAt(builder.length() - 1);
        sonarProject.setProjectsSquished(builder.toString());
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
     * @param projectName
     * @param sonarTargets
     * @return
     */
    public SonarTargetsStatus getTargetStatus(final String projectName) {
        SonarDisplayParameters projectParameters = sonarDisplayParametersRepository.findByProjectName(projectName);
        SonarProject sonarProject = getSquashedAndDerivedSonarMetricsForAllProjectsWhoseNamesMatchFilter(
                projectParameters.getSonarRegex());
        ArrayList<YearMonth> keyList = new ArrayList<>(sonarProject.getSonarMetricPeriods().keySet());
        Collections.sort(keyList);
        SonarMetricPeriod latestMetric = sonarProject.getSonarMetricPeriods().get(keyList.get(keyList.size() - 1));
        if(latestMetric != null) {
            Map<String, SonarMetric> derivedMetricsMap = derivedSonarMetricService
                    .convertListToMap(latestMetric.getDerivedMetrics());
            Map<String, SonarMetric> metricsMap = derivedSonarMetricService
                    .convertListToMap(latestMetric.getSonarMetrics());

            Boolean metric1TargetMet = doesMetricMeetTarget(projectParameters, derivedMetricsMap, metricsMap,
                    projectParameters.getDefaultMetric1());
            Boolean metric2TargetMet = doesMetricMeetTarget(projectParameters, derivedMetricsMap, metricsMap,
                    projectParameters.getDefaultMetric2());
            Boolean metric3TargetMet = doesMetricMeetTarget(projectParameters, derivedMetricsMap, metricsMap,
                    projectParameters.getDefaultMetric3());
            Boolean metric4TargetMet = doesMetricMeetTarget(projectParameters, derivedMetricsMap, metricsMap,
                    projectParameters.getDefaultMetric4());

            return new SonarTargetsStatus(metric1TargetMet, metric2TargetMet, metric3TargetMet, metric4TargetMet);
        }
        return new SonarTargetsStatus(false, false, false, false);
    }

    /**
     * @param projectParameters
     * @param derivedMetricsMap
     * @param metricsMap
     * @param metricName
     */
    private Boolean doesMetricMeetTarget(final SonarDisplayParameters projectParameters,
            final Map<String, SonarMetric> derivedMetricsMap, final Map<String, SonarMetric> metricsMap,
            final String metricName) {
        Boolean targetMet;
        SonarTargetSettings sonarTargetSettings = projectParameters.getSonarTargetParam()
                .get(metricName);

        SonarMetric sonarMetric = derivedMetricsMap.get(metricNameAsKey(metricName));
        if(null == sonarMetric) {
            sonarMetric = metricsMap.get(metricName);
        }
        if(null == sonarMetric) {
            targetMet = false;
        } else {
            String trendDirection = sonarTargetSettings.getTrendDirection();
            BigDecimal value = new BigDecimal(sonarMetric.getRawValue());
            BigDecimal target = new BigDecimal(sonarTargetSettings.getTarget());
            if(TREND_UP.equals(trendDirection)) {
                targetMet = value.compareTo(target) >= 0;
            } else if(TREND_DOWN.equals(trendDirection)) {
                targetMet = value.compareTo(target) <= 0;
            } else {
                // No Way Of Knowing
                targetMet = false;
            }
        }
        return targetMet;
    }

    private String metricNameAsKey(final String metricName) {
        if(StringUtils.isNotEmpty(metricName)) {
            return metricName.toLowerCase().replace(" ", "_");
        } else {
            return "";
        }
    }

    private BigDecimal getTarget(final String urlTarget, final String configTarget) {
        if(StringUtils.isEmpty(urlTarget)) {
            return new BigDecimal(configTarget);
        } else {
            return new BigDecimal(urlTarget);
        }
    }

}
