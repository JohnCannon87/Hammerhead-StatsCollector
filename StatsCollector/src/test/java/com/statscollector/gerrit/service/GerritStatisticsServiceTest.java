package com.statscollector.gerrit.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.statscollector.gerrit.authentication.AuthenticationHelper;
import com.statscollector.gerrit.config.GerritConfig;
import com.statscollector.gerrit.dao.GerritDao;
import com.statscollector.gerrit.model.GerritReviewStats;
import com.statscollector.gerrit.service.GerritService;
import com.statscollector.gerrit.service.GerritStatisticsHelper;
import com.statscollector.gerrit.service.GerritStatisticsService;

public class GerritStatisticsServiceTest {

	private GerritStatisticsService gerritStatisticsService;

	@Before
	public void setUp() throws Exception {
		GerritStatisticsHelper gerritStatisticsHelper = new GerritStatisticsHelper();
		GerritConfig gerritConfig = Mockito.mock(GerritConfig.class);
		Mockito.when(gerritConfig.getHost()).thenReturn("nreojp.git:8080");
		Mockito.when(gerritConfig.getUsername()).thenReturn("jcannon");
		Mockito.when(gerritConfig.getPassword()).thenReturn("testpassword");
		GerritService statisticsService = new GerritService();
		GerritDao statisticsDao = new GerritDao();
		statisticsDao.setGerritConfig(gerritConfig);
		statisticsService.setStatisticsDao(statisticsDao);
		AuthenticationHelper authenticationHelper = new AuthenticationHelper();
		authenticationHelper.setGerritConfig(gerritConfig);
		statisticsService.setAuthenticationHelper(authenticationHelper);
		gerritStatisticsService = new GerritStatisticsService();
		gerritStatisticsService.setGerritConfig(gerritConfig);
		gerritStatisticsService.setGerritService(statisticsService);
		gerritStatisticsService.setGerritStatisticsHelper(gerritStatisticsHelper);
	}
	
	@Test
	public void testGetReviewStatistics() throws IOException, URISyntaxException{
		DateTime startDate = new DateTime(0);
		DateTime endDate = new DateTime().plusYears(100);
		GerritReviewStats reviewStatistics = gerritStatisticsService.getReviewStatistics("open", ".*", startDate, endDate);
	}
	
}
