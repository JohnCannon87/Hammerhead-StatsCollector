package com.statscollector.neo.sonar.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.statscollector.gerrit.model.ConnectionTestResults;
import com.statscollector.neo.sonar.model.SonarProject;
import com.statscollector.neo.sonar.model.SonarTargets;
import com.statscollector.neo.sonar.model.SonarTargetsStatus;
import com.statscollector.neo.sonar.service.SonarService;

@RestController
@RequestMapping("/sonar/stats")
public class SonarStatsController {

    @Autowired
    private SonarService sonarService;

    final static Logger LOGGER = Logger.getLogger(SonarStatsController.class);

    @RequestMapping(value = "/refreshCache")
    public boolean refreshCache() {
        LOGGER.info("Manual Cache Refresh Triggered");
        try {
            sonarService.refreshAllStatistics();
        } catch(Exception e) {
            LOGGER.error("Error thrown", e);
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/testConnection")
    public ConnectionTestResults testSonarConnection() {
        return sonarService.testConnection();
    }

    @CrossOrigin
    @RequestMapping(value = "/targetStatus/{projectRegex}")
    @ResponseBody
    public SonarTargetsStatus targetStatus(@PathVariable final String projectRegex,
            @RequestParam(required = false) final String sonarMethodTarget,
            @RequestParam(required = false) final String sonarFileTarget,
            @RequestParam(required = false) final String sonarTestTarget,
            @RequestParam(required = false) final String sonarRulesTarget, final HttpServletResponse response)
            throws IOException,
            URISyntaxException {
        SonarTargets sonarTargets = new SonarTargets(sonarMethodTarget, sonarFileTarget,
                sonarRulesTarget, sonarTestTarget);
        return sonarService.getTargetStatus(projectRegex, sonarTargets);
    }

    @RequestMapping(value = "/{projectRegex}/all")
    public SonarProject allStatistics(@PathVariable final String projectRegex) throws IOException,
            URISyntaxException {
        LOGGER.info("Getting All Sonar Statistics");
        String searchRegex;
        if(StringUtils.isEmpty(projectRegex)) {
            searchRegex = ".*";
        } else {
            searchRegex = projectRegex;
        }
        return sonarService.getSquashedAndDerivedSonarMetricsForAllProjectsWhoseNamesMatchFilter(searchRegex);
    }

}
