package com.statscollector.gerrit.model;

import java.util.List;

public class ReviewStats {

	private final List<GerritChange> noPeerReviewList, onePeerReviewList, twoPlusPeerReviewList,
			collabrativeDevelopmentList;

	public ReviewStats(final List<GerritChange> noPeerReviewCount, final List<GerritChange> onePeerReviewCount,
			final List<GerritChange> twoPlusPeerReviewCount, final List<GerritChange> collabrativeDevelopmentCount) {
		super();
		this.noPeerReviewList = noPeerReviewCount;
		this.onePeerReviewList = onePeerReviewCount;
		this.twoPlusPeerReviewList = twoPlusPeerReviewCount;
		this.collabrativeDevelopmentList = collabrativeDevelopmentCount;
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

	public int getTotalReviewsCount() {
		return getNoPeerReviewCount() + getOnePeerReviewCount() + getTwoPlusPeerReviewCount()
				+ getCollabrativeDevelopmentCount();
	}

}
