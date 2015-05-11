package com.statscollector.gerrit.model;

import java.util.List;

public class ReviewStats {

	private final List<GerritChange> noPeerReviewList, onePeerReviewList, twoPlusPeerReviewList,
	collabrativeDevelopmentList;

	public ReviewStats(final List<GerritChange> noPeerReviewList, final List<GerritChange> onePeerReviewList,
			final List<GerritChange> twoPlusPeerReviewList, final List<GerritChange> collabrativeDevelopmentList) {
		super();
		this.noPeerReviewList = noPeerReviewList;
		this.onePeerReviewList = onePeerReviewList;
		this.twoPlusPeerReviewList = twoPlusPeerReviewList;
		this.collabrativeDevelopmentList = collabrativeDevelopmentList;
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

}
