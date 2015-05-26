package com.statscollector.gerrit.dao;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.statscollector.gerrit.config.GerritConfig;
import com.statscollector.gerrit.enums.StatusEnum;

public class GerritDaoTest {

	private final GerritDao statisticsDao = new GerritDao();
	private CredentialsProvider credsProvider;
	private GerritConfig gerritConfig;

	@Before
	public void setUp() throws Exception {
		credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope("nreojp.git", 8080), new UsernamePasswordCredentials("jcannon",
				"wigCY0osQ15cuDYco05ABn0WnvV3uetTNyr+sYVF0Q"));
		gerritConfig = Mockito.mock(GerritConfig.class);
		Mockito.when(gerritConfig.getHost()).thenReturn("nreojp.git");
		Mockito.when(gerritConfig.getHostPort()).thenReturn(8080);
		statisticsDao.setGerritConfig(gerritConfig);
	}

	@Test
	public void testGetAllAbandonedChanges() throws Exception {
		String testResult = statisticsDao.getAllChanges(credsProvider, StatusEnum.ABANDONED.toString());
		System.out.println(testResult);
	}

	@Test
	public void testGetAllMergedChanges() throws Exception {
		String testResult = statisticsDao.getAllChanges(credsProvider, StatusEnum.MERGED.toString());
		System.out.println(testResult);
	}

	@Test
	public void testGetAllOpenChanges() throws Exception {
		String testResult = statisticsDao.getAllChanges(credsProvider, StatusEnum.OPEN.toString());
		System.out.println(testResult);
	}

}
