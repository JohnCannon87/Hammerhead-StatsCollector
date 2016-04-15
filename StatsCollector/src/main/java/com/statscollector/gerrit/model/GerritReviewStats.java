package com.statscollector.gerrit.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.google.gerrit.extensions.common.ChangeInfo;

public class GerritReviewStats {

    private final List<ChangeInfo> noPeerReviewList, onePeerReviewList, twoPlusPeerReviewList,
            collabrativeDevelopmentList;

    private Map<DateTime, GerritReviewCounts> changeCountHistory = new HashMap<>();

    private String status;

    private Boolean error;

    private GerritReviewStats(final List<ChangeInfo> noPeerReviewList, final List<ChangeInfo> onePeerReviewList,
            final List<ChangeInfo> twoPlusPeerReviewList, final List<ChangeInfo> collabrativeDevelopmentList,
            final String status, final Boolean error) {
        super();
        this.noPeerReviewList = noPeerReviewList;
        this.onePeerReviewList = onePeerReviewList;
        this.twoPlusPeerReviewList = twoPlusPeerReviewList;
        this.collabrativeDevelopmentList = collabrativeDevelopmentList;
        this.status = status;
        this.error = error;
    }

    public List<ChangeInfo> getNoPeerReviewList() {
        return noPeerReviewList;
    }

    public List<ChangeInfo> getOnePeerReviewList() {
        return onePeerReviewList;
    }

    public List<ChangeInfo> getTwoPlusPeerReviewList() {
        return twoPlusPeerReviewList;
    }

    public List<ChangeInfo> getCollabrativeDevelopmentList() {
        return collabrativeDevelopmentList;
    }

    public int getNoPeerReviewCount() {
        return noPeerReviewList.size();
    }

    public int getOnePeerReviewCount() {
        return onePeerReviewList.size();
    }

    public int getTwoPlusPeerReviewCount() {
        return twoPlusPeerReviewList.size();
    }

    public int getCollabrativeDevelopmentCount() {
        return collabrativeDevelopmentList.size();
    }

    public float getNoPeerReviewPercentage() {
        return (float) noPeerReviewList.size() / (float) getTotalReviewsCount();
    }

    public float getOnePeerReviewPercentage() {
        return (float) onePeerReviewList.size() / (float) getTotalReviewsCount();
    }

    public float getTwoPlusPeerReviewPercentage() {
        return (float) twoPlusPeerReviewList.size() / (float) getTotalReviewsCount();
    }

    public float getCollabrativeDevelopmentPercentage() {
        return (float) collabrativeDevelopmentList.size() / (float) getTotalReviewsCount();
    }

    public int getTotalReviewsCount() {
        return getNoPeerReviewCount() + getOnePeerReviewCount() + getTwoPlusPeerReviewCount()
                + getCollabrativeDevelopmentCount();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(final Boolean error) {
        this.error = error;
    }

    public Map<DateTime, GerritReviewCounts> getChangeCountHistory() {
        return changeCountHistory;
    }

    public void setChangeCountHistory(final Map<DateTime, GerritReviewCounts> changeCountHistory) {
        this.changeCountHistory = changeCountHistory;
    }

    public static GerritReviewStats buildStatsObjectWithValuesAndStatus(final List<ChangeInfo> noPeerReviewList,
            final List<ChangeInfo> onePeerReviewList, final List<ChangeInfo> twoPlusPeerReviewList,
            final List<ChangeInfo> collabrativeDevelopmentList, final String status, final Boolean error) {
        return new GerritReviewStats(noPeerReviewList, onePeerReviewList, twoPlusPeerReviewList,
                collabrativeDevelopmentList, status, error);
    }

    public static GerritReviewStats buildEmptyStatsObjectWithStatus(final String status, final Boolean error) {
        List<ChangeInfo> emptyList = new ArrayList<ChangeInfo>();
        return new GerritReviewStats(emptyList, emptyList, emptyList, emptyList, status, error);
    }

}
