package com.statscollector.neo.sonar.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.statscollector.gerrit.model.ConnectionTestResults;
import com.statscollector.neo.sonar.model.SonarProject;
import com.statscollector.neo.sonar.model.SonarTargetsStatus;
import com.statscollector.neo.sonar.service.SonarService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sonar/stats")
public class SonarStatsController {

    @Autowired
    private SonarService sonarService;

    @RequestMapping(value = "/refreshCache")
    public boolean refreshCache() {
        log.info("Manual Cache Refresh Triggered");
        try {
            sonarService.refreshAllStatistics();
        } catch(Exception e) {
            log.error("Error thrown", e);
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/testConnection")
    public ConnectionTestResults testSonarConnection() {
        return sonarService.testConnection();
    }

    @CrossOrigin
    @RequestMapping(value = "/targetStatus/{projectName}")
    public SonarTargetsStatus targetStatus(@PathVariable final String projectName, final HttpServletResponse response)
            throws IOException,
            URISyntaxException {
        return sonarService.getTargetStatus(projectName);
    }

    @RequestMapping(value = "/{projectRegex}/all")
    public SonarProject allStatistics(@PathVariable final String projectRegex) throws IOException,
            URISyntaxException {
        log.info("Getting All Sonar Statistics");
        String searchRegex;
        if(StringUtils.isEmpty(projectRegex)) {
            searchRegex = ".*";
        } else {
            searchRegex = projectRegex;
        }
        SonarProject result = sonarService
                .getSquashedAndDerivedSonarMetricsForAllProjectsWhoseNamesMatchFilter(searchRegex);
        return result;
    }

}
