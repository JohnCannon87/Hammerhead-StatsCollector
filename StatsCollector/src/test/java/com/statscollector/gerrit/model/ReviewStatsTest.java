package com.statscollector.gerrit.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

public class ReviewStatsTest {
		
	@Test
	public void testPercentages() {
		GerritChange change = new GerritChange("testId", "testChangeId", "testProject", "testOwner", new DateTime(), new DateTime());
		List<GerritChange> noPeerReviewCount = new ArrayList<>();
		List<GerritChange> onePeerReviewCount = new ArrayList<>();
		List<GerritChange> twoPeerReviewCount = new ArrayList<>();
		List<GerritChange> collaborativeDevelopmentCount = new ArrayList<>();
		noPeerReviewCount.add(change);
		onePeerReviewCount.add(change);
		onePeerReviewCount.add(change);
		onePeerReviewCount.add(change);
		twoPeerReviewCount.add(change);
		twoPeerReviewCount.add(change);
		
		ReviewStats stats = new ReviewStats(noPeerReviewCount, onePeerReviewCount, twoPeerReviewCount, collaborativeDevelopmentCount);
		
		float noPeerReviewPercentage = stats.getNoPeerReviewPercentage();
		float onePeerReviewPercentage = stats.getOnePeerReviewPercentage();
		float twoPlusPeerReviewPercentage = stats.getTwoPlusPeerReviewPercentage();
		float collabrativeDevelopmentPercentage = stats.getCollabrativeDevelopmentPercentage();
		
		assertEquals(1.0f/6.0f, noPeerReviewPercentage, 0.0f);
		assertEquals(3.0f/6.0f, onePeerReviewPercentage, 0.0f);
		assertEquals(2.0f/6.0f, twoPlusPeerReviewPercentage, 0.0f);
		assertEquals(0.0f, collabrativeDevelopmentPercentage, 0.0f);
	}

}
