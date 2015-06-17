package com.statscollector.gerrit.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gerrit.extensions.common.ChangeInfo;

public class ReviewStatsTest {

	// testId", "testChangeId",
	// "testProject", "testOwner", new DateTime(),
	// new DateTime(), "", ""

	@Test
	public void testPercentages() {
		ChangeInfo change = new ChangeInfo();
		List<ChangeInfo> noPeerReviewCount = new ArrayList<>();
		List<ChangeInfo> onePeerReviewCount = new ArrayList<>();
		List<ChangeInfo> twoPeerReviewCount = new ArrayList<>();
		List<ChangeInfo> collaborativeDevelopmentCount = new ArrayList<>();
		noPeerReviewCount.add(change);
		onePeerReviewCount.add(change);
		onePeerReviewCount.add(change);
		onePeerReviewCount.add(change);
		twoPeerReviewCount.add(change);
		twoPeerReviewCount.add(change);

		GerritReviewStats stats = GerritReviewStats.buildStatsObjectWithValuesAndStatus(noPeerReviewCount,
				onePeerReviewCount, twoPeerReviewCount, collaborativeDevelopmentCount, "ok", false);

		float noPeerReviewPercentage = stats.getNoPeerReviewPercentage();
		float onePeerReviewPercentage = stats.getOnePeerReviewPercentage();
		float twoPlusPeerReviewPercentage = stats.getTwoPlusPeerReviewPercentage();
		float collabrativeDevelopmentPercentage = stats.getCollabrativeDevelopmentPercentage();

		assertEquals(1.0f / 6.0f, noPeerReviewPercentage, 0.0f);
		assertEquals(3.0f / 6.0f, onePeerReviewPercentage, 0.0f);
		assertEquals(2.0f / 6.0f, twoPlusPeerReviewPercentage, 0.0f);
		assertEquals(0.0f, collabrativeDevelopmentPercentage, 0.0f);
	}

}
