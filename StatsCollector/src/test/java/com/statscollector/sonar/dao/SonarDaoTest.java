package com.statscollector.sonar.dao;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.junit.Before;
import org.junit.Test;

public class SonarDaoTest {

	private SonarDao sonarDao = new SonarDao(); 
	private CredentialsProvider credsProvider;
	
	@Before
	public void setUp() throws Exception {
		credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope("sonar.ojp", 8080), new UsernamePasswordCredentials("jcannon",
				"testpassword"));
	}

	@Test
	public void testGetAllChanges() throws IOException, URISyntaxException {
		sonarDao.getAllChanges(credsProvider);
	}

}
