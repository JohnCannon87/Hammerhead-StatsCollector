package com.statscollector.gerrit.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.statscollector.gerrit.config.GerritConfig;
import com.statscollector.gerrit.model.GerritChange;
import com.statscollector.gerrit.model.ReviewStats;
import com.statscollector.gerrit.service.filter.FilterDateUpdatedPredicate;
import com.statscollector.gerrit.service.filter.FilterProjectNamePredicate;
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

	final static Logger LOGGER = Logger.getLogger(GerritStatisticsService.class);

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
			final DateTime startDate, final DateTime endDate) throws IOException, URISyntaxException {
		List<GerritChange> allChanges = gerritService.getAllChanges(changeStatus);
		return filterChanges(allChanges, getFilters(projectFilterRegex, startDate, endDate));
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
			final DateTime endDate) {
		List<GerritChangeFilter> results = new ArrayList<GerritChangeFilter>();
		results.add(new FilterDateUpdatedPredicate(startDate, endDate));
		results.add(new FilterProjectNamePredicate(projectFilterRegex));
		return results;
	}

	public ReviewStats getReviewStatistics(final String changeStatus, final String projectFilterString,
			final DateTime startDate, final DateTime endDate) throws IOException, URISyntaxException {
		List<GerritChange> noPeerReviewList = new ArrayList<>();
		List<GerritChange> onePeerReviewList = new ArrayList<>();
		List<GerritChange> twoPlusPeerReviewList = new ArrayList<>();
		List<GerritChange> collabrativeDevelopmentList = new ArrayList<>();

		List<GerritChange> changes = getChangesBasedOnParameters(changeStatus, projectFilterString, startDate, endDate);
		gerritService.populateChangeReviewers(changes);
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

		return new ReviewStats(noPeerReviewList, onePeerReviewList, twoPlusPeerReviewList, collabrativeDevelopmentList);
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
			if (reviewValue > 0) {
				reviewersList.add(username);
			}
		}
		List<String> reviewersToIgnore = gerritConfig.getReviewersToIgnore();
		reviewersList.removeAll(reviewersToIgnore);// Get rid of all automated
		// reviewers etc.
		reviewersList.remove(owner);// Remove the owner from the review list
		return reviewersList.size();
	}

}
