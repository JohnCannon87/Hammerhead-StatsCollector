package com.statscollector.gerrit.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.statscollector.gerrit.authentication.AuthenticationHelper;
import com.statscollector.gerrit.config.GerritConfig;
import com.statscollector.gerrit.dao.GerritDao;
import com.statscollector.gerrit.enums.StatusEnum;
import com.statscollector.gerrit.model.GerritChange;
import com.statscollector.gerrit.model.GerritChangeDetails;
import com.statscollector.gerrit.service.GerritService;

public class GerritServiceTest {

	private GerritService statisticsService;

	@Before
	public void setUp() throws Exception {
		GerritConfig gerritConfig = Mockito.mock(GerritConfig.class);
		Mockito.when(gerritConfig.getHost()).thenReturn("nreojp.git:8080");
		Mockito.when(gerritConfig.getUsername()).thenReturn("jcannon");
		Mockito.when(gerritConfig.getPassword()).thenReturn("testpassword");
		statisticsService = new GerritService();
		GerritDao statisticsDao = new GerritDao();
		statisticsDao.setGerritConfig(gerritConfig);
		statisticsService.setStatisticsDao(statisticsDao);
		AuthenticationHelper authenticationHelper = new AuthenticationHelper();
		authenticationHelper.setGerritConfig(gerritConfig);
		statisticsService.setAuthenticationHelper(authenticationHelper);
	}

	@Test
	public void testGetAllChanges() throws IOException, URISyntaxException {
		List<GerritChange> allChanges = statisticsService.getAllChanges(StatusEnum.MERGED.toString());
		System.out.println(allChanges);
	}

	@Test
	public void testGetGerritChangeDetails() throws IOException, URISyntaxException {
		List<GerritChange> allChanges = statisticsService.getAllChanges(StatusEnum.OPEN.toString());
		System.out.println(allChanges);
		Map<String, GerritChangeDetails> gerritChangeDetails = statisticsService.getGerritChangeDetails(allChanges);
		Collection<GerritChangeDetails> values = gerritChangeDetails.values();
		for (GerritChangeDetails gerritChangeDetail : values) {
			System.out.println(gerritChangeDetail);
		}
	}

}
