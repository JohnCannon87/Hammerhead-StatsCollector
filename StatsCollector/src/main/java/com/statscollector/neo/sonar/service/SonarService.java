package com.statscollector.neo.sonar.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.YearMonth;
import java.util.ArrayList;
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
            if(sonarProject.getName().matches(regexFilter)) {
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

    public SonarTargetsStatus getTargetStatus(final String projectRegex, final SonarTargets sonarTargets)
            throws IOException, URISyntaxException {
        // getSquashedSonarMetricsForAllProjectsWhoseNamesMatchFilter
        //
        // BigDecimal fileComplexity = new BigDecimal(latestStatistics.getFileComplexity());
        // BigDecimal methodComplexity = new BigDecimal(latestStatistics.getMethodComplexity());
        // BigDecimal rulesCompliance = new BigDecimal(latestStatistics.getRulesCompliance());
        // BigDecimal testCoverage = new BigDecimal(latestStatistics.getTestCoverage());
        // BigDecimal fileComplexityTarget = getTarget(sonarTargets.getSonarFileTarget(),
        // sonarConfig.getFileComplexityTarget());
        // BigDecimal methodComplexityTarget = getTarget(sonarTargets.getSonarMethodTarget(),
        // sonarConfig.getMethodComplexityTarget());
        // BigDecimal rulesComplianceTarget = getTarget(sonarTargets.getSonarRulesTarget(),
        // sonarConfig.getRulesComplianceTarget());
        // BigDecimal testCoverageTarget = getTarget(sonarTargets.getSonarTestTarget(),
        // sonarConfig.getTestCoverageTarget());
        // boolean hitFileTarget = (fileComplexity.compareTo(fileComplexityTarget) <= 0);
        // boolean hitMethodTarget = (methodComplexity.compareTo(methodComplexityTarget) <= 0);
        // boolean hitTestTarget = (testCoverage.compareTo(testCoverageTarget) >= 0);
        // boolean hitRulesTarget = (rulesCompliance.compareTo(rulesComplianceTarget) >= 0);
        //
        // return new SonarTargetsStatus(hitFileTarget, hitMethodTarget, hitTestTarget, hitRulesTarget);
        return null;
    }

    private BigDecimal getTarget(final String urlTarget, final String configTarget) {
        if(StringUtils.isEmpty(urlTarget)) {
            return new BigDecimal(configTarget);
        } else {
            return new BigDecimal(urlTarget);
        }
    }

}
