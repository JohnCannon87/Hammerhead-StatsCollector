package com.statscollector.model;

public class ReviewStats {

	private final int noPeerReviewCount, onePeerReviewCount, twoPlusPeerReviewCount, collabrativeDevelopmentCount;

	public ReviewStats(final int noPeerReviewCount, final int onePeerReviewCount, final int twoPlusPeerReviewCount,
			final int collabrativeDevelopmentCount) {
		super();
		this.noPeerReviewCount = noPeerReviewCount;
		this.onePeerReviewCount = onePeerReviewCount;
		this.twoPlusPeerReviewCount = twoPlusPeerReviewCount;
		this.collabrativeDevelopmentCount = collabrativeDevelopmentCount;
	}

	public int getNoPeerReviewCount() {
		return noPeerReviewCount;
	}

	public int getOnePeerReviewCount() {
		return onePeerReviewCount;
	}

	public int getTwoPlusPeerReviewCount() {
		return twoPlusPeerReviewCount;
	}

	public int getCollabrativeDevelopmentCount() {
		return collabrativeDevelopmentCount;
	}

}
