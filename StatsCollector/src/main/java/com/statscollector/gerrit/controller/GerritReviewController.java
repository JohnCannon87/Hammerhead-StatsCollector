package com.statscollector.gerrit.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.statscollector.gerrit.model.GerritReviewStats;
import com.statscollector.gerrit.service.GerritStatisticsService;

@RestController
@RequestMapping("/gerrit/review")
public class GerritReviewController {

	private static final String ALL_REGEX = ".*";

	private static final int CURRENT_TIME_OFFSET = 100;

	final static Logger LOGGER = Logger.getLogger(GerritStatisticsService.class);

	@Autowired
	private GerritStatisticsService statisticsService;

	@RequestMapping(value = "/refreshCache")
	public void refreshCache() throws Exception {
		LOGGER.info("Manual Cache Refresh Triggered");
		statisticsService.getReviewStatisticsScheduledTask();
	}

	@RequestMapping(value = "/{changeStatus}/{projectFilterString}/{startDateOffset}/{endDateOffset}", produces = "application/json")
	public GerritReviewStats statusReview(@PathVariable final String changeStatus,
			@PathVariable final String projectFilterString, @PathVariable final Integer startDateOffset,
			@PathVariable final Integer endDateOffset) {
		try {
			return getReviewStatistics(changeStatus, projectFilterString, calculateDateFromOffset(startDateOffset),
					calculateDateFromOffset(endDateOffset));
		} catch (Exception e) {
			return GerritReviewStats.buildEmptyStatsObjectWithStatus(e.getMessage(), true);
		}
	}

	@RequestMapping(value = "/{changeStatus}/{projectFilterString}", produces = "application/json")
	public GerritReviewStats mergedReview(@PathVariable final String changeStatus,
			@PathVariable final String projectFilterString) {
		try {
			return getReviewStatistics(changeStatus, projectFilterString, null, null);
		} catch (Exception e) {
			return GerritReviewStats.buildEmptyStatsObjectWithStatus(e.getMessage(), true);
		}
	}

	@RequestMapping(value = "/{changeStatus}/{startDateOffset}/{endDateOffset}", produces = "application/json")
	public GerritReviewStats mergedReview(@PathVariable final String changeStatus,
			@PathVariable final Integer startDateOffset, @PathVariable final Integer endDateOffset) throws IOException,
			URISyntaxException {
		try {
			return getReviewStatistics(changeStatus, null, calculateDateFromOffset(startDateOffset),
					calculateDateFromOffset(endDateOffset));
		} catch (Exception e) {
			return GerritReviewStats.buildEmptyStatsObjectWithStatus(e.getMessage(), true);
		}
	}

	@RequestMapping(value = "/{changeStatus}/all", produces = "application/json")
	public GerritReviewStats mergedReview(@PathVariable final String changeStatus) throws IOException,
			URISyntaxException {
		try {
			return getReviewStatistics(changeStatus, null, null, null);
		} catch (Exception e) {
			return GerritReviewStats.buildEmptyStatsObjectWithStatus(e.getMessage(), true);
		}
	}

	public GerritReviewStats getReviewStatistics(final String changeStatus, String projectFilterString,
			DateTime startDate, DateTime endDate) throws IOException, URISyntaxException {
		if (null == projectFilterString) {
			projectFilterString = ALL_REGEX;
		}
		if (null == startDate) {
			startDate = new DateTime(0);
		}
		if (null == endDate) {
			// Search far enough into future to offset any chance of incorrect
			// time syncs between servers.
			endDate = new DateTime().plusYears(CURRENT_TIME_OFFSET);
		}
		return statisticsService.getReviewStatistics(changeStatus, projectFilterString, startDate, endDate);
	}

	private DateTime calculateDateFromOffset(final Integer offset) {
		return new DateTime().plusDays(offset);
	}

}