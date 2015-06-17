package com.statscollector.gerrit.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
import com.google.gerrit.extensions.common.ApprovalInfo;
import com.google.gerrit.extensions.common.ChangeInfo;
import com.google.gerrit.extensions.common.LabelInfo;
import com.statscollector.gerrit.config.GerritConfig;
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

	final Map<String, List<ChangeInfo>> allChanges = new ConcurrentHashMap<>();

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
	public List<ChangeInfo> filterChangesBasedOnProjectName(final List<ChangeInfo> toBeFiltered,
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
	public List<ChangeInfo> filterChangesBasedOnDateRange(final List<ChangeInfo> toBeFiltered,
			final DateTime startDate, final DateTime endDate) {
		return Lists
				.newArrayList(Collections2.filter(toBeFiltered, new FilterDateUpdatedPredicate(startDate, endDate)));
	}

	@Async
	public Future<GerritReviewStatsResult> populateReviewStatsAsync(final String changeStatus,
			final List<ChangeInfo> noPeerReviewList, final List<ChangeInfo> onePeerReviewList,
			final List<ChangeInfo> twoPlusPeerReviewList, final List<ChangeInfo> collabrativeDevelopmentList,
			final List<ChangeInfo> changes) throws IOException, URISyntaxException {
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

	public Map<String, GerritReviewStats> populateReviewStats(final String changeStatus,
			final List<ChangeInfo> noPeerReviewList, final List<ChangeInfo> onePeerReviewList,
			final List<ChangeInfo> twoPlusPeerReviewList, final List<ChangeInfo> collabrativeDevelopmentList,
			final List<ChangeInfo> changes) throws Exception {
		List<ChangeInfo> populatedChanges = gerritService.populateChangeReviewers(changes);
		allChanges.put(changeStatus, populatedChanges);
		for (ChangeInfo gerritChange : populatedChanges) {
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
		return allReviewStats;
	}

	/**
	 * I return the number of reviewers for the provided change, if the change
	 * has been collaboratively developed I return -1
	 *
	 * @param gerritChange
	 * @return
	 */
	private int numberOfReviewers(final ChangeInfo gerritChange) {
		// LOGGER.info("Calculating changes for change: " + gerritChange);
		int result = 0;
		String owner = gerritChange.owner.username;
		Map<String, LabelInfo> labels = gerritChange.labels;
		Collection<LabelInfo> values = labels.values();
		for (LabelInfo labelInfo : values) {
			List<ApprovalInfo> allLabels = labelInfo.all;
			if (null != allLabels) {
				result = result + getNumberOfHumanReviewers(result, owner, allLabels);
			}
		}
		return result;
	}

	private int getNumberOfHumanReviewers(final int result, final String owner, final List<ApprovalInfo> allLabels) {
		for (ApprovalInfo approvalInfo : allLabels) {
			if (ifReviewerValid(owner, approvalInfo) && isApprovalValueAboveZero(approvalInfo)) {
				return 1;
			}
		}
		return 0;
	}

	private boolean ifReviewerValid(final String owner, final ApprovalInfo approvalInfo) {
		return ifReviewerNotOwner(owner, approvalInfo) && ifReviewerNotExcluded(approvalInfo);
	}

	private boolean ifReviewerNotExcluded(final ApprovalInfo approvalInfo) {
		return !gerritConfig.getReviewersToIgnore().contains(approvalInfo.username);
	}

	private boolean ifReviewerNotOwner(final String owner, final ApprovalInfo approvalInfo) {
		return !approvalInfo.username.equals(owner);
	}

	private boolean isApprovalValueAboveZero(final ApprovalInfo approvalInfo) {
		return approvalInfo.value != null && approvalInfo.value > 1;
	}

	public Map<String, List<ChangeInfo>> getAllChanges() {
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
