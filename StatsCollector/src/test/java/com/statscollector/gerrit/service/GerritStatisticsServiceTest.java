package com.statscollector.gerrit.service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.statscollector.gerrit.model.GerritChange;

public class GerritStatisticsServiceTest {

	private final GerritStatisticsService gerritStatisticsService = new GerritStatisticsService();
	private final GerritStatisticsHelper gerritStatisticsHelper = Mockito.mock(GerritStatisticsHelper.class);

	private final DateTime beforeFilterDate = new DateTime(0).withYear(2014).withMonthOfYear(1).withDayOfMonth(1);
	private final DateTime duringFilterDate = new DateTime(0).withYear(2015).withMonthOfYear(6).withDayOfMonth(1);
	private final DateTime afterFilterDate = new DateTime(0).withYear(2017).withMonthOfYear(1).withDayOfMonth(1);
	private final DateTime startDate = new DateTime(0).withYear(2015).withMonthOfYear(1).withDayOfMonth(1);
	private final DateTime endDate = new DateTime(0).withYear(2016).withMonthOfYear(1).withDayOfMonth(1);
	private final GerritChange workingChange = new GerritChange("workingId", "workingChangeId", "testProj",
			"workingOwner", duringFilterDate, duringFilterDate, "", "working");

	@Before
	public void setUp() throws Exception {
		gerritStatisticsService.setGerritStatisticsHelper(gerritStatisticsHelper);
		Map<String, List<GerritChange>> changes = new HashMap<>();
		changes.put("testStatus", createChanges());
		Mockito.when(gerritStatisticsHelper.getAllChanges()).thenReturn(changes);
	}

	@Test
	public void testGetChangesBasedOnParameters() throws IOException, URISyntaxException {
		List<GerritChange> changesBasedOnParameters = gerritStatisticsService.getChangesBasedOnParameters("testStatus",
				".*test.*", startDate, endDate, "");
		assertEquals(1, changesBasedOnParameters.size());
		assertEquals(workingChange, changesBasedOnParameters.get(0));
	}

	// @Test
	// public void testGetReviewStatisticsScheduledTask() {
	// gerritStatisticsService.getReviewStatisticsScheduledTask();
	// }
	//
	// @Test
	// public void testGetReviewStatistics() throws IOException,
	// URISyntaxException {
	// GerritReviewStats reviewStatistics =
	// gerritStatisticsService.getReviewStatistics("testStatus", ".*test.*",
	// startDate, endDate);
	// }

	private List<GerritChange> createChanges() {
		List<GerritChange> result = new ArrayList<>();
		result.add(new GerritChange("testId", "testChangeId", "testProj", "testOwner", beforeFilterDate,
				beforeFilterDate, "", "develop"));
		result.add(new GerritChange("testId", "testChangeId", "failProj", "testOwner", duringFilterDate,
				duringFilterDate, "", "develop"));
		result.add(workingChange);
		result.add(new GerritChange("testId", "testChangeId", "testProj", "testOwner", afterFilterDate,
				afterFilterDate, "", "develop"));

		return result;
	}

}
