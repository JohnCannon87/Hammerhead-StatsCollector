package com.statscollector.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.statscollector.gerrit.authentication.AuthenticationHelper;
import com.statscollector.gerrit.dao.GerritDao;
import com.statscollector.gerrit.enums.StatusEnum;
import com.statscollector.gerrit.model.GerritChange;
import com.statscollector.gerrit.model.GerritChangeDetails;
import com.statscollector.gerrit.service.GerritService;

public class GerritServiceTest {

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
		List<GerritChange> allChanges = statisticsService.getAllChanges(StatusEnum.MERGED.toString());
		System.out.println(allChanges);
	}

	@Test
	public void testGetGerritChangeDetails() throws IOException, URISyntaxException {
		List<GerritChange> allChanges = statisticsService.getAllChanges(StatusEnum.MERGED.toString());
		System.out.println(allChanges);
		Map<String, GerritChangeDetails> gerritChangeDetails = statisticsService.getGerritChangeDetails(allChanges);
		Collection<GerritChangeDetails> values = gerritChangeDetails.values();
		for (GerritChangeDetails gerritChangeDetail : values) {
			System.out.println(gerritChangeDetail);
		}
	}

}
