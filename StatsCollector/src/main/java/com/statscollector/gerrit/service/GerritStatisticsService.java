package com.statscollector.gerrit.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.gerrit.extensions.common.ChangeInfo;
import com.statscollector.gerrit.config.GerritConfig;
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
	 * I act upon the provided list of GerritChangeFilters to remove all
	 * unwanted GerritChanges.
	 *
	 * @param allChanges
	 * @param filters
	 * @return
	 */
	private List<ChangeInfo> filterChanges(final List<ChangeInfo> allChanges, final List<GerritChangeFilter> filters) {
		List<ChangeInfo> results = Lists.newArrayList(allChanges);
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
		try {
			if (!refreshInProgress) {
				refreshInProgress = true;
				for (String changeStatus : CHANGE_STATUSES) {
					long start = System.currentTimeMillis();
					List<ChangeInfo> noPeerReviewList = new ArrayList<>();
					List<ChangeInfo> onePeerReviewList = new ArrayList<>();
					List<ChangeInfo> twoPlusPeerReviewList = new ArrayList<>();
					List<ChangeInfo> collabrativeDevelopmentList = new ArrayList<>();
					LOGGER.info("Getting Changes For Status: " + changeStatus);

					List<ChangeInfo> changes = gerritService.getAllChanges(changeStatus);

					Integer threadSplitSize = gerritConfig.getThreadSplitSize();

					if (changes.size() < threadSplitSize) {

						GerritReviewStatsResult result = null;
						try {
							gerritStatisticsHelper.populateReviewStats(changeStatus, noPeerReviewList,
									onePeerReviewList, twoPlusPeerReviewList, collabrativeDevelopmentList, changes);
							result = new GerritReviewStatsResult(true, changes);
						} catch (Exception e) {
							LOGGER.info("CAUGHT EXCEPTION");
							result = new GerritReviewStatsResult(false, e, changes);
						}

						GerritReviewStats gerritReviewStats = gerritStatisticsHelper.getAllReviewStats().get(
								changeStatus);
						gerritReviewStats.setError(!result.getSuccess());
						if (!result.getSuccess()) {
							gerritReviewStats.setStatus(gerritReviewStats.getStatus()
									+ "Error Thrown During Processing: " + result.getError().getMessage());
						} else {
							gerritReviewStats.setStatus(gerritReviewStats.getStatus() + "OK");
						}
						gerritReviewStats.setStatus(gerritReviewStats.getStatus() + "Cache Processed Using : 1 Thread");
					} else {
						List<Future<GerritReviewStatsResult>> asyncResults = new ArrayList<>();
						List<List<ChangeInfo>> partitionedLists;
						if (threadSplitSize > 0) {
							partitionedLists = Lists.partition(changes, threadSplitSize);
						} else {
							partitionedLists = Arrays.asList(changes);
						}
						for (List<ChangeInfo> list : partitionedLists) {
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
						GerritReviewStats gerritReviewStats = gerritStatisticsHelper.getAllReviewStats().get(
								changeStatus);
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
		} catch (Exception e) {
			LOGGER.error("Error Processing Data From Gerrit", e);
			refreshInProgress = false;
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
