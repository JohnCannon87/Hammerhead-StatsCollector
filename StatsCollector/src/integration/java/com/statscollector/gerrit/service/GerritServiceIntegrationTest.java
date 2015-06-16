package com.statscollector.gerrit.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.gerrit.extensions.common.ChangeInfo;
import com.statscollector.gerrit.authentication.GerritAuthenticationHelper;
import com.statscollector.gerrit.config.GerritConfig;
import com.statscollector.gerrit.dao.GerritDao;
import com.statscollector.gerrit.enums.StatusEnum;

public class GerritServiceIntegrationTest {

	private GerritService statisticsService;

	@Before
	public void setUp() throws Exception {
		GerritConfig gerritConfig = Mockito.mock(GerritConfig.class);
		Mockito.when(gerritConfig.getHost()).thenReturn("nreojp.git");
		Mockito.when(gerritConfig.getHostPort()).thenReturn(8080);
		Mockito.when(gerritConfig.getUsername()).thenReturn("jcannon");
		Mockito.when(gerritConfig.getPassword()).thenReturn("t9MYEr/hErZpRkGgTGocPzTaxb0ob7Odls6YaaHMVA");
		statisticsService = new GerritService();
		GerritDao statisticsDao = new GerritDao();
		statisticsDao.setGerritConfig(gerritConfig);
		statisticsService.setStatisticsDao(statisticsDao);
		GerritAuthenticationHelper authenticationHelper = new GerritAuthenticationHelper();
		authenticationHelper.setGerritConfig(gerritConfig);
		statisticsService.setAuthenticationHelper(authenticationHelper);
	}

	@Test
	public void testGetAllChanges() throws Exception {
		List<ChangeInfo> allChanges = statisticsService.getAllChanges(StatusEnum.OPEN.toString());
		assertNotNull(allChanges);
		assertTrue(allChanges.size() > 0);
	}

	@Test
	public void testGetGerritChangeDetails() throws Exception {
		List<ChangeInfo> allChanges = statisticsService.getAllChanges(StatusEnum.OPEN.toString());
		assertNotNull(allChanges);
		assertTrue(allChanges.size() > 0);
		Map<String, ChangeInfo> gerritChangeDetails = statisticsService.getGerritChangeDetails(allChanges);
		Collection<ChangeInfo> values = gerritChangeDetails.values();
		assertNotNull(values);
		assertTrue(values.size() > 0);
		for (ChangeInfo details : values) {
			assertNotNull(details);
			assertNotNull(details.changeId);
		}
	}

}
