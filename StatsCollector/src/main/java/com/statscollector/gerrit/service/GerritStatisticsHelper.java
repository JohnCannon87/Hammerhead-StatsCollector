package com.statscollector.gerrit.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.google.gerrit.extensions.common.AccountInfo;
import com.google.gerrit.extensions.common.ApprovalInfo;
import com.google.gerrit.extensions.common.ChangeInfo;
import com.google.gerrit.extensions.common.LabelInfo;
import com.statscollector.gerrit.config.GerritConfig;
import com.statscollector.gerrit.model.GerritReviewStats;
import com.statscollector.gerrit.model.GerritReviewStatsResult;
import com.statscollector.gerrit.model.GerritUserCount;

/**
 * I'm a class that takes a list of changes from Gerrit and performs some
 * business logic on them.
 *
 * @author JCannon
 *
 */
@Component
public class GerritStatisticsHelper {

    private static final String CODE_REVIEW = "Code-Review";

    @Autowired
    private GerritService gerritService;

    @Autowired
    private GerritConfig gerritConfig;

    final Map<String, List<ChangeInfo>> allChanges = new ConcurrentHashMap<>();

    final Map<String, GerritReviewStats> allReviewStats = new ConcurrentHashMap<>();

    final static Logger LOGGER = Logger.getLogger(GerritStatisticsService.class);

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
        } catch(Exception e) {
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
        for(ChangeInfo gerritChange : populatedChanges) {
            int numberOfReviewers = numberOfReviewers(gerritChange);
            switch(numberOfReviewers) {
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
        LabelInfo labelInfo = labels.get(CODE_REVIEW);
        if(null != labelInfo) {
            List<ApprovalInfo> allLabels = labelInfo.all;
            if(null != allLabels) {
                result = result + getNumberOfHumanReviewers(owner, allLabels);
            }
        }
        return result;
    }

    private int getNumberOfHumanReviewers(final String owner, final List<ApprovalInfo> allLabels) {
        int result = 0;
        for(ApprovalInfo approvalInfo : allLabels) {
            if(ifReviewerValid(owner, approvalInfo)) {
                if(isApprovalValueAboveZero(approvalInfo)) {
                    result++;
                } else if(isApprovalValueBelowZero(approvalInfo)) {
                    return 0;
                }
            }
        }
        return result;
    }

    private boolean ifReviewerValid(final String owner, final ApprovalInfo approvalInfo) {
        return ifReviewerNotOwner(owner, approvalInfo) && ifReviewerNotExcluded(approvalInfo);
    }

    private boolean ifReviewerNotExcluded(final ApprovalInfo approvalInfo) {
        return !gerritConfig.getReviewersToIgnoreList().contains(approvalInfo.username);
    }

    private boolean ifReviewerNotOwner(final String owner, final ApprovalInfo approvalInfo) {
        return !approvalInfo.username.equals(owner);
    }

    private boolean isApprovalValueAboveZero(final ApprovalInfo approvalInfo) {
        return approvalInfo.value != null && approvalInfo.value > 0;
    }

    private boolean isApprovalValueBelowZero(final ApprovalInfo approvalInfo) {
        return approvalInfo.value != null && approvalInfo.value < 0;
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

    @SuppressWarnings("unchecked")
    public Map<String, GerritUserCount> createHashMapOfAuthorsAndCounts(final List<ChangeInfo>... reviewLists) {
        Map<String, GerritUserCount> result = new HashMap<String, GerritUserCount>();
        for(List<ChangeInfo> list : reviewLists) {
            for(ChangeInfo changeInfo : list) {
                if(!StringUtils.isEmpty(changeInfo.owner.username)) {
                    if(result.containsKey(changeInfo.owner.username)) {
                        result.get(changeInfo.owner.username).incrementCount();
                    } else {
                        result.put(changeInfo.owner.username, new GerritUserCount(changeInfo.owner));
                    }
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public Map<String, GerritUserCount> createHashMapOfReviwersAndCounts(final List<ChangeInfo>... reviewLists) {
        Map<String, GerritUserCount> result = new HashMap<String, GerritUserCount>();
        for(List<ChangeInfo> list : reviewLists) {
            for(ChangeInfo changeInfo : list) {
                Map<String, LabelInfo> labels = changeInfo.labels;
                for(LabelInfo labelInfo : labels.values()) {
                    // Approvers
                    List<AccountInfo> users = getAllNonFilteredReviewersForLabels(labelInfo);
                    for(AccountInfo user : users) {
                        if(result.containsKey(user.username)) {
                            result.get(user.username).incrementCount();
                        } else {
                            result.put(user.username, new GerritUserCount(user));
                        }
                    }
                }
            }
        }
        return result;
    }

    private List<AccountInfo> getAllNonFilteredReviewersForLabels(final LabelInfo labelInfo) {
        List<AccountInfo> result = new ArrayList<AccountInfo>();

        addToListIfNotInFilterListAndNotNull(labelInfo.approved, result);
        addToListIfNotInFilterListAndNotNull(labelInfo.recommended, result);
        addToListIfNotInFilterListAndNotNull(labelInfo.disliked, result);
        addToListIfNotInFilterListAndNotNull(labelInfo.rejected, result);
        return result;
    }

    private void addToListIfNotInFilterListAndNotNull(final AccountInfo user,
            final List<AccountInfo> result) {
        if(null != user) {
            if(!gerritConfig.getReviewersToIgnoreList().contains(user.username)) {
                result.add(user);
            }
        }
    }
}
