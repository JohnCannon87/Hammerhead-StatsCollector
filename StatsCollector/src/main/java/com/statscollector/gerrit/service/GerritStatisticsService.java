package com.statscollector.gerrit.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.statscollector.gerrit.config.GerritConfig;
import com.statscollector.gerrit.model.GerritChange;
import com.statscollector.gerrit.model.GerritReviewStats;
import com.statscollector.gerrit.model.GerritReviewStatsResult;
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

	static final String REVIEW_STATS_STATUS_OK = "OK";

	private static final String REVIEW_STATS_STATUS_FAILED = "Error No Results Loaded Into Cache";

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
		return filterChanges(gerritStatisticsHelper.getAllChanges().get(changeStatus),
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

	/**
	 * Every 15 minutes I run multiple threads to pull back all the gerrit
	 * details, I split based on gerritConfig.ThreadSplitSize, which is
	 * configurable in the UI.
	 * 
	 * @throws Exception
	 */
	@Scheduled(fixedRate = 4500000)
	public void getReviewStatisticsScheduledTask() throws Exception {
		LOGGER.info("Is Refresh In Progress ?: " + refreshInProgress);
		if (!refreshInProgress) {
			refreshInProgress = true;
			for (String changeStatus : CHANGE_STATUSES) {
				long start = System.currentTimeMillis();
				List<GerritChange> noPeerReviewList = new ArrayList<>();
				List<GerritChange> onePeerReviewList = new ArrayList<>();
				List<GerritChange> twoPlusPeerReviewList = new ArrayList<>();
				List<GerritChange> collabrativeDevelopmentList = new ArrayList<>();
				LOGGER.info("Getting Changes For Status: " + changeStatus);

				List<GerritChange> changes = gerritService.getAllChanges(changeStatus);

				Integer threadSplitSize = gerritConfig.getThreadSplitSize();

				if (changes.size() < threadSplitSize) {

					GerritReviewStatsResult result = null;
					try {
						gerritStatisticsHelper.populateReviewStats(changeStatus, noPeerReviewList, onePeerReviewList,
								twoPlusPeerReviewList, collabrativeDevelopmentList, changes);
						result = new GerritReviewStatsResult(true, changes);
					} catch (Exception e) {
						LOGGER.info("CAUGHT EXCEPTION");
						result = new GerritReviewStatsResult(false, e, changes);
					}

					GerritReviewStats gerritReviewStats = gerritStatisticsHelper.getAllReviewStats().get(changeStatus);
					gerritReviewStats.setError(!result.getSuccess());
					if (!result.getSuccess()) {
						gerritReviewStats.setStatus(gerritReviewStats.getStatus() + "Error Thrown During Processing: "
								+ result.getError().getMessage());
					} else {
						gerritReviewStats.setStatus(gerritReviewStats.getStatus() + "OK");
					}
					gerritReviewStats.setStatus(gerritReviewStats.getStatus() + "Cache Processed Using : 1 Thread");
				} else {
					List<Future<GerritReviewStatsResult>> asyncResults = new ArrayList<>();
					List<List<GerritChange>> partitionedLists = Lists.partition(changes, threadSplitSize);
					for (List<GerritChange> list : partitionedLists) {
						asyncResults.add(gerritStatisticsHelper.populateReviewStatsAsync(changeStatus,
								noPeerReviewList, onePeerReviewList, twoPlusPeerReviewList,
								collabrativeDevelopmentList, list));
					}
					// Wait for all threads to complete.
					for (Future<GerritReviewStatsResult> asyncResult : asyncResults) {
						while (!asyncResult.isDone()) {
						}
					}
					// Process Thread Responses
					GerritReviewStats gerritReviewStats = gerritStatisticsHelper.getAllReviewStats().get(changeStatus);
					for (Future<GerritReviewStatsResult> asyncResult : asyncResults) {
						GerritReviewStatsResult result = asyncResult.get();
						if (result == null) {
							gerritReviewStats.setStatus(gerritReviewStats.getStatus()
									+ "Error Thrown During Processing: Null Pointer Returned, ");
							gerritReviewStats
									.setError(((gerritReviewStats != null && gerritReviewStats.getError()) || true));
						} else if (!result.getSuccess()) {
							gerritReviewStats.setStatus(gerritReviewStats.getStatus()
									+ "Error Thrown During Processing: " + result.getError().getMessage() + ", ");
							gerritReviewStats
									.setError(((gerritReviewStats != null && gerritReviewStats.getError()) || !result
											.getSuccess()));
						} else {
							gerritReviewStats.setStatus(gerritReviewStats.getStatus() + "OK, ");
							gerritReviewStats
									.setError(((gerritReviewStats != null && gerritReviewStats.getError()) || !result
											.getSuccess()));
						}
					}
					gerritReviewStats.setStatus(gerritReviewStats.getStatus() + "Cache Processed Using : "
							+ asyncResults.size() + " Threads");
				}
				long end = System.currentTimeMillis();
				LOGGER.info("Got Changes For Status: " + changeStatus + " In: " + (end - start) / 1000 + " Seconds");
			}
			refreshInProgress = false;
			LOGGER.info("Refresh Completed");
		}
	}

	public GerritReviewStats getReviewStatistics(final String changeStatus, final String projectFilterString,
			final DateTime startDate, final DateTime endDate) throws IOException, URISyntaxException {

		List<GerritChangeFilter> filters = getFilters(projectFilterString, startDate, endDate,
				gerritConfig.getTopicRegex());

		GerritReviewStats reviewStats = gerritStatisticsHelper.getAllReviewStats().get(changeStatus);
		return buildReviewStatsObject(filters, reviewStats);
	}

	private GerritReviewStats buildReviewStatsObject(final List<GerritChangeFilter> filters,
			final GerritReviewStats reviewStats) {
		if (null != reviewStats) {
			return GerritReviewStats.buildStatsObjectWithValuesAndStatus(
					filterChanges(reviewStats.getNoPeerReviewList(), filters),
					filterChanges(reviewStats.getOnePeerReviewList(), filters),
					filterChanges(reviewStats.getTwoPlusPeerReviewList(), filters),
					filterChanges(reviewStats.getCollabrativeDevelopmentList(), filters), reviewStats.getStatus(),
					reviewStats.getError());
		} else {
			return GerritReviewStats.buildEmptyStatsObjectWithStatus(REVIEW_STATS_STATUS_FAILED, true);
		}
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
