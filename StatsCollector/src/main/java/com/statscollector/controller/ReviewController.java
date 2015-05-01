package com.statscollector.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.statscollector.model.ReviewStats;
import com.statscollector.service.GerritService;

@RestController
public class ReviewController {

	@Autowired
	private GerritService statisticsService;

	@RequestMapping("/review")
	public ReviewStats review(
			@RequestParam(value = "projectFilterString", defaultValue = "all") final String projectFilterString) {
		return statisticsService.getReviewStats(projectFilterString);
	}

}
