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
	
	@RequestMapping("/{projectFilterString}/{startDate}/{endDate}")
	public ReviewStats review(@PathVariable String projectFilterString, @PathVariable String startDate, @PathVariable String endDate) throws IOException, URISyntaxException {
		return getReviewStatistics(projectFilterString, parseDate(startDate), parseDate(endDate));
	}

	@RequestMapping("/{projectFilterString}")
	public ReviewStats review(@PathVariable String projectFilterString) throws IOException, URISyntaxException {
		return getReviewStatistics(projectFilterString, null, null);
	}
	
	@RequestMapping("/{startDate}/{endDate}")
	public ReviewStats review(@PathVariable String startDate, @PathVariable String endDate) throws IOException, URISyntaxException {
		return getReviewStatistics(null, parseDate(startDate), parseDate(endDate));
	}
	
	@RequestMapping("/all")
	public ReviewStats review() throws IOException, URISyntaxException {
		return getReviewStatistics(null, null, null);
	}

	public ReviewStats getReviewStatistics(String projectFilterString, DateTime startDate, DateTime endDate) throws IOException, URISyntaxException{
		if(null == projectFilterString){
			projectFilterString = ALL_REGEX;
		}
		if(null == startDate){
			startDate = new DateTime(0);
		}
		if(null == endDate){
			endDate = new DateTime().plusYears(CURRENT_TIME_OFFSET);//Search far enough into future to offset any chance of incorrect time syncs between servers.
		}
		return statisticsService.getReviewStatistics(projectFilterString, startDate, endDate);
	}
	
	private DateTime parseDate(String stringDate) {
		DateTimeFormatter pattern = DateTimeFormat.forPattern(DATE_PATTERN);
		return pattern.parseDateTime(stringDate);
	}
	
}