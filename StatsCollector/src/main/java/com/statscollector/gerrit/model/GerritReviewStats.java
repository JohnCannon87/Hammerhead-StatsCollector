package com.statscollector.gerrit.model;

import java.util.ArrayList;
import java.util.List;

public class GerritReviewStats {

	private final List<GerritChange> noPeerReviewList, onePeerReviewList, twoPlusPeerReviewList,
	collabrativeDevelopmentList;
	
	private String status;

	private Boolean error;
	
	private GerritReviewStats(final List<GerritChange> noPeerReviewList, final List<GerritChange> onePeerReviewList,
			final List<GerritChange> twoPlusPeerReviewList, final List<GerritChange> collabrativeDevelopmentList, final String status, final Boolean error) {
		super();
		this.noPeerReviewList = noPeerReviewList;
		this.onePeerReviewList = onePeerReviewList;
		this.twoPlusPeerReviewList = twoPlusPeerReviewList;
		this.collabrativeDevelopmentList = collabrativeDevelopmentList;
		this.status = status;
		this.error = error;
	}

	public List<GerritChange> getNoPeerReviewList() {
		return noPeerReviewList;
	}

	public List<GerritChange> getOnePeerReviewList() {
		return onePeerReviewList;
	}

	public List<GerritChange> getTwoPlusPeerReviewList() {
		return twoPlusPeerReviewList;
	}

	public List<GerritChange> getCollabrativeDevelopmentList() {
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

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public static GerritReviewStats buildStatsObjectWithValuesAndStatus(
			List<GerritChange> noPeerReviewList,
			List<GerritChange> onePeerReviewList,
			List<GerritChange> twoPlusPeerReviewList,
			List<GerritChange> collabrativeDevelopmentList, String status, Boolean error) {
		return new GerritReviewStats(noPeerReviewList, onePeerReviewList, twoPlusPeerReviewList, collabrativeDevelopmentList, status, error);
	}

	public static GerritReviewStats buildEmptyStatsObjectWithStatus(
			String status, Boolean error) {
		List<GerritChange> emptyList = new ArrayList<GerritChange>();
		return new GerritReviewStats(emptyList, emptyList, emptyList, emptyList, status, error);
	}

}
