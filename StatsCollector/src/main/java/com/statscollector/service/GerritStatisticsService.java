package com.statscollector.service;

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
import com.statscollector.config.GerritConfig;
import com.statscollector.model.GerritChange;
import com.statscollector.model.ReviewStats;
import com.statscollector.service.filter.FilterDateUpdatedPredicate;
import com.statscollector.service.filter.FilterProjectNamePredicate;
import com.statscollector.service.filter.GerritChangeFilter;

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
	public List<GerritChange> getChangesBasedOnParameters(final String projectFilterRegex, final DateTime startDate,
			final DateTime endDate) throws IOException, URISyntaxException {
		List<GerritChange> allChanges = gerritService.getAllMergedChanges();
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

	public ReviewStats getReviewStatistics(String projectFilterString,
			DateTime startDate, DateTime endDate) throws IOException, URISyntaxException {
		int noPeerReviewCount = 0, onePeerReviewCount = 0, twoPlusPeerReviewCount = 0, collabrativeDevelopmentCount = 0;
		List<GerritChange> changes = getChangesBasedOnParameters(projectFilterString, startDate, endDate);
		gerritService.populateChangeReviewers(changes);
		for (GerritChange gerritChange : changes) {
			int numberOfReviewers = numberOfReviewers(gerritChange);
			LOGGER.info("Number Of Reviewers Found: " + numberOfReviewers);
			switch(numberOfReviewers){
				case -1:
					collabrativeDevelopmentCount++;
				break;
				case 0:
					noPeerReviewCount++;
				break;
				case 1:
					onePeerReviewCount++;
				break;
				default:
					twoPlusPeerReviewCount++;
				break;
			}
		}
		
		return new ReviewStats(noPeerReviewCount, onePeerReviewCount, twoPlusPeerReviewCount, collabrativeDevelopmentCount);
	}

	/**
	 * I return the number of reviewers for the provided change, if the change has been collaboratively developed I return -1
	 * @param gerritChange
	 * @return
	 */
	private int numberOfReviewers(GerritChange gerritChange) {
		LOGGER.info("Calculating changes for change: " + gerritChange);
		String owner = gerritChange.getOwner();
		Map<String, Integer> reviewers = gerritChange.getReviewers();
		Set<String> reviewersUsernames = reviewers.keySet();
		List<String> reviewersList = new ArrayList<>();
		for (String username : reviewersUsernames) {
			Integer reviewValue = reviewers.get(username);
			if(reviewValue > 0){
				reviewersList.add(username);
			}
		}
		List<String> reviewersToIgnore = gerritConfig.getReviewersToIgnore();
		reviewersList.removeAll(reviewersToIgnore);//Get rid of all automated reviewers etc.
		reviewersList.remove(owner);//Remove the owner from the review list
		return reviewersList.size();
	}

}
