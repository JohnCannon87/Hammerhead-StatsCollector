package com.statscollector.service;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.statscollector.authentication.AuthenticationHelper;
import com.statscollector.dao.GerritDao;
import com.statscollector.model.GerritChange;

public class StatisticsServiceTest {

	private GerritService statisticsService;

	@Before
	public void setUp() throws Exception {
		statisticsService = new GerritService();
		GerritDao statisticsDao = new GerritDao();
		statisticsService.setStatisticsDao(statisticsDao);
		AuthenticationHelper authenticationHelper = new AuthenticationHelper();
		statisticsService.setAuthenticationHelper(authenticationHelper);
	}

	@Test
	public void testGetAllChanges() throws IOException, URISyntaxException {
		List<GerritChange> allChanges = statisticsService.getAllChanges();
		System.out.println(allChanges);
	}

	@Test
	public void testGetReviewStats() {
		fail("Not yet implemented");
	}

}
