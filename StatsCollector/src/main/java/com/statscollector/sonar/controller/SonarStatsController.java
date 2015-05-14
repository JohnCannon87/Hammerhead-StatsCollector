package com.statscollector.sonar.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.statscollector.sonar.model.SonarStatistics;
import com.statscollector.sonar.service.SonarStatisticsService;

@RestController
@RequestMapping("/sonar/stats")
public class SonarStatsController {

	@Autowired
	private SonarStatisticsService sonarStatisticsService;

	final static Logger LOGGER = Logger.getLogger(SonarStatsController.class);

	@RequestMapping(value = "/refreshCache")
	public void refreshCache() throws IOException, URISyntaxException, InterruptedException, ExecutionException {
		LOGGER.info("Manual Cache Refresh Triggered");
		sonarStatisticsService.getStatisticsScheduledTask();
	}

	@RequestMapping(value = "/allStatistics")
	public SonarStatistics allStatistics() throws IOException, URISyntaxException {
		LOGGER.info("Getting Sonar Statistics");
		return sonarStatisticsService.getStatistics();
	}

}
