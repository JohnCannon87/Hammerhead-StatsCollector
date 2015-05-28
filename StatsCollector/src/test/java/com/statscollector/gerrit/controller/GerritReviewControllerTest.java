package com.statscollector.gerrit.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.statscollector.gerrit.service.GerritStatisticsService;

public class GerritReviewControllerTest {

	private static final String ALL_REGEX = ".*";
	private static final int CURRENT_TIME_OFFSET = 100;
	private static final String CHANGE_STATUS = "status:dontcare";

	private final GerritReviewController controller = new GerritReviewController();
	private GerritStatisticsService mockStatisticsService;

	@Before
	public void setUp() throws Exception {
		mockStatisticsService = Mockito.mock(GerritStatisticsService.class);
		controller.setStatisticsService(mockStatisticsService);
	}

	@Test(expected = RuntimeException.class)
	public void testGetReviewStatisticsNullChangeStatus() throws IOException, URISyntaxException {
		controller.getReviewStatistics(null, null, null, null);
	}

	@Test
	public void testGetReviewStatistics() throws IOException, URISyntaxException {
		controller.getReviewStatistics(CHANGE_STATUS, null, null, null);
		Mockito.verify(mockStatisticsService, Mockito.only()).getReviewStatistics(CHANGE_STATUS, ALL_REGEX,
				new DateTime(0), new DateMidnight().plusYears(CURRENT_TIME_OFFSET).toDateTime());
	}
}
