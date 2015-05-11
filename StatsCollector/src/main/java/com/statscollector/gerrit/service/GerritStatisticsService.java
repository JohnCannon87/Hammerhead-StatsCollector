package com.statscollector.gerrit.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.statscollector.gerrit.config.GerritConfig;
import com.statscollector.gerrit.model.GerritChange;
import com.statscollector.gerrit.model.ReviewStats;
import com.statscollector.gerrit.service.filter.FilterDateUpdatedPredicate;
import com.statscollector.gerrit.service.filter.FilterProjectNamePredicate;
import com.statscollector.gerrit.service.filter.FilterTopicPredicate;
import com.statscollector.gerrit.service.filter.GerritChangeFilter;

/**
 * I'm a service to return information based on a Gerrit instance.
 *
 * @author JCannon
 *
 */
@Service
public class GerritStatisticsService {

	@Autowired
	private GerritService gerritService;

	@Autowired
	private GerritStatisticsHelper gerritStatisticsHelper;

	@Autowired
	private GerritConfig gerritConfig;

	final static String[] CHANGE_STATUSES = { "open", "merged", "abandoned" };

	final static Logger LOGGER = Logger.getLogger(GerritStatisticsService.class);

	final Map<String, List<GerritChange>> allChanges = new ConcurrentHashMap<>();

	final Map<String, ReviewStats> allReviewStats = new ConcurrentHashMap<>();

	private boolean refreshInProgress = false;

	/**
	 * I return a list of changes with unwanted changes filtered out based on
	 * the provided parameters.
	 *
	 * @param projectFilterRegex
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public List<GerritChange> getChangesBasedOnParameters(final String changeStatus, final String projectFilterRegex,
			final DateTime startDate, final DateTime endDate, final String topicNameRegex) throws IOException,
			URISyntaxException {
		return filterChanges(allChanges.get(changeStatus),
				getFilters(projectFilterRegex, startDate, endDate, topicNameRegex));
	}

	/**
	 * I act upon the provided list of GerritChangeFilters to remove all
	 * unwanted GerritChanges.
	 *
	 * @param allChanges
	 * @param filters
	 * @return
	 */
	private List<GerritChange> filterChanges(final List<GerritChange> allChanges, final List<GerritChangeFilter> filters) {
		List<GerritChange> results = Lists.newArrayList(allChanges);
		for (GerritChangeFilter filter : filters) {
			results = filter.filter(results);
		}
		return results;
	}

	/**
	 * Returns a list of filters to run against the change list, important to
	 * think about order here, the filter that will remove the largest set
	 * should be first, then the next largest set etc, should help with
	 * performance.
	 *
	 * @param projectFilterRegex
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private List<GerritChangeFilter> getFilters(final String projectFilterRegex, final DateTime startDate,
			final DateTime endDate, final String topicNameRegex) {
		List<GerritChangeFilter> results = new ArrayList<GerritChangeFilter>();
		results.add(new FilterDateUpdatedPredicate(startDate, endDate));
		results.add(new FilterProjectNamePredicate(projectFilterRegex));
		if (!topicNameRegex.isEmpty()) {
			results.add(new FilterTopicPredicate(topicNameRegex));
		}
		return results;
	}

	@Scheduled(initialDelay = 30000, fixedRate = 4500000)
	// Every 15 minutes
	public void getReviewStatisticsScheduledTask() throws IOException, URISyntaxException {
		if (!refreshInProgress) {
			refreshInProgress = true;
			for (String changeStatus : CHANGE_STATUSES) {
				List<GerritChange> noPeerReviewList = new ArrayList<>();
				List<GerritChange> onePeerReviewList = new ArrayList<>();
				List<GerritChange> twoPlusPeerReviewList = new ArrayList<>();
				List<GerritChange> collabrativeDevelopmentList = new ArrayList<>();

				List<GerritChange> changes = gerritService.getAllChanges(changeStatus);
				gerritService.populateChangeReviewers(changes);
				allChanges.put(changeStatus, changes);
				for (GerritChange gerritChange : changes) {
					int numberOfReviewers = numberOfReviewers(gerritChange);
					LOGGER.info("Number Of Reviewers Found: " + numberOfReviewers);
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
				allReviewStats.put(changeStatus, new ReviewStats(noPeerReviewList, onePeerReviewList,
						twoPlusPeerReviewList, collabrativeDevelopmentList));
			}
			refreshInProgress = false;
		}
	}

	public ReviewStats getReviewStatistics(final String changeStatus, final String projectFilterString,
			final DateTime startDate, final DateTime endDate) throws IOException, URISyntaxException {

		List<GerritChangeFilter> filters = getFilters(projectFilterString, startDate, endDate,
				gerritConfig.getTopicRegex());

		ReviewStats reviewStats = allReviewStats.get(changeStatus);
		return new ReviewStats(filterChanges(reviewStats.getNoPeerReviewList(), filters), filterChanges(
				reviewStats.getOnePeerReviewList(), filters), filterChanges(reviewStats.getTwoPlusPeerReviewList(),
				filters), filterChanges(reviewStats.getCollabrativeDevelopmentList(), filters));
	}

	/**
	 * I return the number of reviewers for the provided change, if the change
	 * has been collaboratively developed I return -1
	 *
	 * @param gerritChange
	 * @return
	 */
	private int numberOfReviewers(final GerritChange gerritChange) {
		LOGGER.info("Calculating changes for change: " + gerritChange);
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

	public void setGerritService(final GerritService gerritService) {
		this.gerritService = gerritService;
	}

	public void setGerritStatisticsHelper(final GerritStatisticsHelper gerritStatisticsHelper) {
		this.gerritStatisticsHelper = gerritStatisticsHelper;
	}

	public void setGerritConfig(final GerritConfig gerritConfig) {
		this.gerritConfig = gerritConfig;
	}
}
