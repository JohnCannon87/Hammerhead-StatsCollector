package com.statscollector.gerrit.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Before;
import org.mockito.Mockito;

import com.google.gerrit.extensions.common.ChangeInfo;

public class GerritStatisticsServiceTest {

	private final GerritStatisticsService gerritStatisticsService = new GerritStatisticsService();
	private final GerritStatisticsHelper gerritStatisticsHelper = Mockito.mock(GerritStatisticsHelper.class);

	private final DateTime beforeFilterDate = new DateTime(0).withYear(2014).withMonthOfYear(1).withDayOfMonth(1);
	private final DateTime duringFilterDate = new DateTime(0).withYear(2015).withMonthOfYear(6).withDayOfMonth(1);
	private final DateTime afterFilterDate = new DateTime(0).withYear(2017).withMonthOfYear(1).withDayOfMonth(1);
	private final DateTime startDate = new DateTime(0).withYear(2015).withMonthOfYear(1).withDayOfMonth(1);
	private final DateTime endDate = new DateTime(0).withYear(2016).withMonthOfYear(1).withDayOfMonth(1);
	private final ChangeInfo workingChange = new ChangeInfo();

	@Before
	public void setUp() throws Exception {
		workingChange.id = "workingId";
		workingChange.changeId = "workingChangeId";
		workingChange.project = "testProj";
		workingChange.updated = new Timestamp(duringFilterDate.getMillis());
		workingChange.branch = "working";
		gerritStatisticsService.setGerritStatisticsHelper(gerritStatisticsHelper);
		Map<String, List<ChangeInfo>> changes = new HashMap<>();
		changes.put("testStatus", createChanges());
		Mockito.when(gerritStatisticsHelper.getAllChanges()).thenReturn(changes);
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

	private List<ChangeInfo> createChanges() {
		List<ChangeInfo> result = new ArrayList<>();
		result.add(createChangeInfo("testProj", beforeFilterDate));
		result.add(createChangeInfo("failProj", duringFilterDate));
		result.add(workingChange);
		result.add(createChangeInfo("testProj", afterFilterDate));

		return result;
	}

	private ChangeInfo createChangeInfo(final String project, final DateTime dateTime) {
		ChangeInfo changeInfo = new ChangeInfo();
		changeInfo.id = "testId";
		changeInfo.changeId = "testChangeId";
		changeInfo.project = project;
		changeInfo.updated = new Timestamp(dateTime.getMillis());
		changeInfo.branch = "develop";
		return changeInfo;
	}

}
