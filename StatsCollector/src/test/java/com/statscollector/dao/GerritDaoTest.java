package com.statscollector.dao;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.junit.Before;
import org.junit.Test;

import com.statscollector.enums.StatusEnum;

public class GerritDaoTest {

	private final GerritDao statisticsDao = new GerritDao();
	private CredentialsProvider credsProvider;

	@Before
	public void setUp() throws Exception {
		credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope("nreojp.git", 8080), new UsernamePasswordCredentials("jcannon",
				"testpassword"));
	}

	@Test
	public void testGetAllAbandonedChanges() throws IOException, URISyntaxException {
		String testResult = statisticsDao.getAllChanges(credsProvider, StatusEnum.ABANDONED.toString());
		System.out.println(testResult);
	}

	@Test
	public void testGetAllMergedChanges() throws IOException, URISyntaxException {
		String testResult = statisticsDao.getAllChanges(credsProvider, StatusEnum.MERGED.toString());
		System.out.println(testResult);
	}

	@Test
	public void testGetAllOpenChanges() throws IOException, URISyntaxException {
		String testResult = statisticsDao.getAllChanges(credsProvider, StatusEnum.OPEN.toString());
		System.out.println(testResult);
	}

}
