package com.statscollector.sonar.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.statscollector.gerrit.model.ConnectionTestResults;
import com.statscollector.sonar.model.SonarTargetsStatus;
import com.statscollector.sonar.service.SonarStatisticsService;

@RestController
@RequestMapping("/sonar/stats")
public class SonarStatsController {

    @Autowired
    private SonarStatisticsService sonarStatisticsService;

    final static Logger LOGGER = Logger.getLogger(SonarStatsController.class);

    @RequestMapping(value = "/refreshCache")
    public boolean refreshCache() {
        LOGGER.info("Manual Cache Refresh Triggered");
        try {
            sonarStatisticsService.getStatisticsScheduledTask();
        } catch(Exception e) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/testConnection")
    public ConnectionTestResults testSonarConnection() {
        return sonarStatisticsService.testConnection();
    }

    @CrossOrigin
    @RequestMapping(value = "/statistics/targetStatus/{projectRegex}/{type}")
    @ResponseBody
    public SonarTargetsStatus targetStatus(@PathVariable final String projectRegex,
            @PathVariable final String type, final HttpServletResponse response) throws IOException,
            URISyntaxException {
        return sonarStatisticsService.getTargetStatus(projectRegex);
    }

    @RequestMapping(value = "/statistics/{projectRegex}/{type}")
    public Object allStatistics(@PathVariable final String projectRegex,
            @PathVariable final String type) throws IOException,
            URISyntaxException {
        LOGGER.info("Getting All Sonar Statistics");
        if(type.equals("all")) {
            return sonarStatisticsService.getAllStatistics(projectRegex);
        } else if(type.equals("latest")) {
            return sonarStatisticsService.getLatestStatistics(projectRegex);
        } else {
            return null;
        }
    }

}
