package com.statscollector.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.statscollector.model.ReviewStats;
import com.statscollector.service.GerritStatisticsService;

@RestController
@RequestMapping("/gerrit/review")
public class GerritReviewController {

	private static final String DATE_PATTERN = "yyyyMMdd";

	private static final String ALL_REGEX = ".*";

	private static final int CURRENT_TIME_OFFSET = 100;

	@Autowired
	private GerritStatisticsService statisticsService;

	@RequestMapping(value = "/{changeStatus}/{projectFilterString}/{startDate}/{endDate}", produces = "application/json")
	public ReviewStats statusReview(@PathVariable final String changeStatus,
			@PathVariable final String projectFilterString, @PathVariable final String startDate,
			@PathVariable final String endDate) throws IOException, URISyntaxException {
		return getReviewStatistics(changeStatus, projectFilterString, parseDate(startDate), parseDate(endDate));
	}

	@RequestMapping(value = "/{changeStatus}/{projectFilterString}", produces = "application/json")
	public ReviewStats mergedReview(@PathVariable final String changeStatus,
			@PathVariable final String projectFilterString) throws IOException, URISyntaxException {
		return getReviewStatistics(changeStatus, projectFilterString, null, null);
	}

	@RequestMapping(value = "/{changeStatus}/{startDate}/{endDate}", produces = "application/json")
	public ReviewStats mergedReview(@PathVariable final String changeStatus, @PathVariable final String startDate,
			@PathVariable final String endDate) throws IOException, URISyntaxException {
		return getReviewStatistics(changeStatus, null, parseDate(startDate), parseDate(endDate));
	}

	@RequestMapping(value = "/{changeStatus}/all", produces = "application/json")
	public ReviewStats mergedReview(@PathVariable final String changeStatus) throws IOException, URISyntaxException {
		return getReviewStatistics(changeStatus, null, null, null);
	}

	public ReviewStats getReviewStatistics(final String changeStatus, String projectFilterString, DateTime startDate,
			DateTime endDate) throws IOException, URISyntaxException {
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

	private DateTime parseDate(final String stringDate) {
		DateTimeFormatter pattern = DateTimeFormat.forPattern(DATE_PATTERN);
		return pattern.parseDateTime(stringDate);
	}

}