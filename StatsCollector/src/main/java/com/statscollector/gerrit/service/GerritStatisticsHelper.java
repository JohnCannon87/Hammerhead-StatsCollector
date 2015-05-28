package com.statscollector.gerrit.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.statscollector.gerrit.config.GerritConfig;
import com.statscollector.gerrit.model.GerritChange;
import com.statscollector.gerrit.model.GerritReviewStats;
import com.statscollector.gerrit.model.GerritReviewStatsResult;
import com.statscollector.gerrit.service.filter.FilterDateUpdatedPredicate;
import com.statscollector.gerrit.service.filter.FilterProjectNamePredicate;

/**
 * I'm a class that takes a list of changes from Gerrit and performs some
 * business logic on them.
 *
 * @author JCannon
 *
 */
@Component
public class GerritStatisticsHelper {

	@Autowired
	private GerritService gerritService;

	@Autowired
	private GerritConfig gerritConfig;

	final Map<String, List<GerritChange>> allChanges = new ConcurrentHashMap<>();

	final Map<String, GerritReviewStats> allReviewStats = new ConcurrentHashMap<>();

	final static Logger LOGGER = Logger.getLogger(GerritStatisticsService.class);

	/**
	 * I return a copy of the provided List but filtered based on the
	 * projectNameRegex provided.
	 *
	 * @param toBeFiltered
	 * @param projectNameRegex
	 * @return
	 */
	public List<GerritChange> filterChangesBasedOnProjectName(final List<GerritChange> toBeFiltered,
			final String projectNameRegex) {
		return Lists.newArrayList(Collections2.filter(toBeFiltered, new FilterProjectNamePredicate(projectNameRegex)));
	}

	/**
	 * I return a copy of the provided List but filtered based on the start and
	 * end dates provided
	 *
	 * @param toBeFiltered
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<GerritChange> filterChangesBasedOnDateRange(final List<GerritChange> toBeFiltered,
			final DateTime startDate, final DateTime endDate) {
		return Lists
				.newArrayList(Collections2.filter(toBeFiltered, new FilterDateUpdatedPredicate(startDate, endDate)));
	}

	@Async
	public Future<GerritReviewStatsResult> populateReviewStatsAsync(final String changeStatus,
			final List<GerritChange> noPeerReviewList, final List<GerritChange> onePeerReviewList,
			final List<GerritChange> twoPlusPeerReviewList, final List<GerritChange> collabrativeDevelopmentList,
			final List<GerritChange> changes) throws IOException, URISyntaxException {
		LOGGER.info("Starting Thread To Process Changes");
		GerritReviewStatsResult result = null;
		try {
			populateReviewStats(changeStatus, noPeerReviewList, onePeerReviewList, twoPlusPeerReviewList,
					collabrativeDevelopmentList, changes);
			result = new GerritReviewStatsResult(true, changes);
		} catch (Exception e) {
			LOGGER.info("CAUGHT EXCEPTION");
			result = new GerritReviewStatsResult(false, e, changes);
		}
		LOGGER.info("Thread Finished");
		return new AsyncResult<GerritReviewStatsResult>(result);
	}

	public void populateReviewStats(final String changeStatus, final List<GerritChange> noPeerReviewList,
			final List<GerritChange> onePeerReviewList, final List<GerritChange> twoPlusPeerReviewList,
			final List<GerritChange> collabrativeDevelopmentList, final List<GerritChange> changes) throws Exception {
		gerritService.populateChangeReviewers(changes);
		allChanges.put(changeStatus, changes);
		for (GerritChange gerritChange : changes) {
			int numberOfReviewers = numberOfReviewers(gerritChange);
			switch (numberOfReviewers) {
			case -1:
				collabrativeDevelopmentList.add(gerritChange);
				break;
			case 0:
				noPeerReviewList.add(gerritChange);
				break;
			case 1:
				onePeerReviewList.add(gerritChange);
				break;
			default:
				twoPlusPeerReviewList.add(gerritChange);
				break;
			}
		}

		allReviewStats.put(changeStatus, GerritReviewStats.buildStatsObjectWithValuesAndStatus(noPeerReviewList,
				onePeerReviewList, twoPlusPeerReviewList, collabrativeDevelopmentList, "", false));
	}

	/**
	 * I return the number of reviewers for the provided change, if the change
	 * has been collaboratively developed I return -1
	 *
	 * @param gerritChange
	 * @return
	 */
	private int numberOfReviewers(final GerritChange gerritChange) {
		// LOGGER.info("Calculating changes for change: " + gerritChange);
		String owner = gerritChange.getOwner();
		Map<String, Integer> reviewers = gerritChange.getReviewers();
		Set<String> reviewersUsernames = reviewers.keySet();
		List<String> reviewersList = new ArrayList<>();
		for (String username : reviewersUsernames) {
			Integer reviewValue = reviewers.get(username);
			if (reviewValue != null && reviewValue > 0) {
				reviewersList.add(username);
			}
		}
		List<String> reviewersToIgnore = gerritConfig.getReviewersToIgnore();
		reviewersList.removeAll(reviewersToIgnore);// Get rid of all automated
		// reviewers etc.
		reviewersList.remove(owner);// Remove the owner from the review list
		return reviewersList.size();
	}

	public Map<String, List<GerritChange>> getAllChanges() {
		return allChanges;
	}

	public Map<String, GerritReviewStats> getAllReviewStats() {
		return allReviewStats;
	}

	public void setGerritService(final GerritService gerritService) {
		this.gerritService = gerritService;
	}

	public void setGerritConfig(final GerritConfig gerritConfig) {
		this.gerritConfig = gerritConfig;
	}

}
