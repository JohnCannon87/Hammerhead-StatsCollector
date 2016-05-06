package com.statscollector.gerrit.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.statscollector.gerrit.model.ConnectionTestResults;
import com.statscollector.gerrit.model.GerritAuthorsAndReviewersList;
import com.statscollector.gerrit.model.GerritReviewStats;
import com.statscollector.gerrit.model.GerritUserCount;
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
    public boolean refreshCache() {
        LOGGER.info("Manual Cache Refresh Triggered");
        try {
            statisticsService.getReviewStatisticsScheduledTask();
        } catch(Exception e) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/testConnection")
    public ConnectionTestResults testGerritConnection() {
        return statisticsService.testConnection();
    }

    @RequestMapping(value = "/authors/{changeStatus}/{projectFilterString}/{projectFilterOutString}/{startDateOffset}/{endDateOffset}", produces = "application/json")
    public GerritAuthorsAndReviewersList authorsList(@PathVariable final String changeStatus,
            @PathVariable final String projectFilterString, @PathVariable final String projectFilterOutString,
            @PathVariable final Integer startDateOffset,
            @PathVariable final Integer endDateOffset) {
        try {
            return getCommitAuthors(changeStatus, projectFilterString, projectFilterOutString,
                    calculateDateFromOffset(startDateOffset),
                    calculateDateFromOffset(endDateOffset));
        } catch(Exception e) {
            LOGGER.error("Error Producing Authors and Reviewers List", e);
            return new GerritAuthorsAndReviewersList(new ArrayList<GerritUserCount>(),
                    new ArrayList<GerritUserCount>());
        }
    }

    @RequestMapping(value = "/authors/{changeStatus}/{projectFilterString}/{startDateOffset}/{endDateOffset}", produces = "application/json")
    public GerritAuthorsAndReviewersList authorsList(@PathVariable final String changeStatus,
            @PathVariable final String projectFilterString,
            @PathVariable final Integer startDateOffset,
            @PathVariable final Integer endDateOffset) {
        try {
            return getCommitAuthors(changeStatus, projectFilterString, "",
                    calculateDateFromOffset(startDateOffset),
                    calculateDateFromOffset(endDateOffset));
        } catch(Exception e) {
            LOGGER.error("Error Producing Authors and Reviewers List", e);
            return new GerritAuthorsAndReviewersList(new ArrayList<GerritUserCount>(),
                    new ArrayList<GerritUserCount>());
        }
    }

    @RequestMapping(value = "/{changeStatus}/{projectFilterString}/{startDateOffset}/{endDateOffset}", produces = "application/json")
    public GerritReviewStats statusReview(@PathVariable final String changeStatus,
            @PathVariable final String projectFilterString,
            @PathVariable final Integer startDateOffset,
            @PathVariable final Integer endDateOffset) {
        try {
            return getReviewStatistics(changeStatus, projectFilterString, "",
                    calculateDateFromOffset(startDateOffset),
                    calculateDateFromOffset(endDateOffset));
        } catch(Exception e) {
            LOGGER.error("Error Producing review List", e);
            return GerritReviewStats.buildEmptyStatsObjectWithStatus(e.getMessage(), true);
        }
    }

    @RequestMapping(value = "/{changeStatus}/{projectFilterString}/{projectFilterOutString}/{startDateOffset}/{endDateOffset}", produces = "application/json")
    public GerritReviewStats statusReview(@PathVariable final String changeStatus,
            @PathVariable final String projectFilterString, @PathVariable final String projectFilterOutString,
            @PathVariable final Integer startDateOffset,
            @PathVariable final Integer endDateOffset) {
        try {
            return getReviewStatistics(changeStatus, projectFilterString, projectFilterOutString,
                    calculateDateFromOffset(startDateOffset),
                    calculateDateFromOffset(endDateOffset));
        } catch(Exception e) {
            LOGGER.error("Error Producing review List", e);
            return GerritReviewStats.buildEmptyStatsObjectWithStatus(e.getMessage(), true);
        }
    }

    @RequestMapping(value = "/{changeStatus}/{projectFilterString}", produces = "application/json")
    public GerritReviewStats mergedReview(@PathVariable final String changeStatus,
            @PathVariable final String projectFilterString) {
        try {
            return getReviewStatistics(changeStatus, projectFilterString, null, null, null);
        } catch(Exception e) {
            return GerritReviewStats.buildEmptyStatsObjectWithStatus(e.getMessage(), true);
        }
    }

    @RequestMapping(value = "/{changeStatus}/{startDateOffset}/{endDateOffset}", produces = "application/json")
    public GerritReviewStats mergedReview(@PathVariable final String changeStatus,
            @PathVariable final Integer startDateOffset, @PathVariable final Integer endDateOffset) throws IOException,
            URISyntaxException {
        try {
            return getReviewStatistics(changeStatus, null, null, calculateDateFromOffset(startDateOffset),
                    calculateDateFromOffset(endDateOffset));
        } catch(Exception e) {
            return GerritReviewStats.buildEmptyStatsObjectWithStatus(e.getMessage(), true);
        }
    }

    @RequestMapping(value = "/{changeStatus}/all", produces = "application/json")
    public GerritReviewStats mergedReview(@PathVariable final String changeStatus) throws IOException,
            URISyntaxException {
        try {
            return getReviewStatistics(changeStatus, null, null, null, null);
        } catch(Exception e) {
            return GerritReviewStats.buildEmptyStatsObjectWithStatus(e.getMessage(), true);
        }
    }

    public GerritAuthorsAndReviewersList getCommitAuthors(final String changeStatus, String projectFilterString,
            final String projectFilterOutString,
            DateTime startDate, DateTime endDate) throws IOException, URISyntaxException {
        if(null == changeStatus || changeStatus.isEmpty()) {
            throw new RuntimeException("Error change status cannot be null");
        }
        if(null == projectFilterString) {
            projectFilterString = ALL_REGEX;
        }
        if(null == startDate) {
            startDate = new DateTime(0);
        }
        if(null == endDate) {
            // Search far enough into future to offset any chance of incorrect
            // time syncs between servers.
            endDate = new DateMidnight().plusYears(CURRENT_TIME_OFFSET).toDateTime();
        }
        return statisticsService.getCommitAuthors(changeStatus, projectFilterString, projectFilterOutString, startDate,
                endDate);
    }

    public GerritReviewStats getReviewStatistics(final String changeStatus, String projectFilterString,
            String projectFilterOutString,
            DateTime startDate, DateTime endDate) throws IOException, URISyntaxException {
        if(null == changeStatus || changeStatus.isEmpty()) {
            throw new RuntimeException("Error change status cannot be null");
        }
        if(null == projectFilterString) {
            projectFilterString = ALL_REGEX;
        }
        if(null == projectFilterOutString) {
            projectFilterOutString = "";
        }
        if(null == startDate) {
            startDate = new DateTime(0);
        }
        if(null == endDate) {
            // Search far enough into future to offset any chance of incorrect
            // time syncs between servers.
            endDate = new DateMidnight().plusYears(CURRENT_TIME_OFFSET).toDateTime();
        }
        return statisticsService.getReviewStatistics(changeStatus, projectFilterString, projectFilterOutString,
                startDate, endDate);
    }

    private DateTime calculateDateFromOffset(final Integer offset) {
        return new DateTime().plusDays(offset);
    }

    public void setStatisticsService(final GerritStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }
}
