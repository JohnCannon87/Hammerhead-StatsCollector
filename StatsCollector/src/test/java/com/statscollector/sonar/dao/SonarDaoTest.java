package com.statscollector.sonar.dao;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.statscollector.sonar.config.SonarConfig;

public class SonarDaoTest {

	private final SonarDao sonarDao = new SonarDao();
	private CredentialsProvider credsProvider;

	@Before
	public void setUp() throws Exception {
		credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope("sonar.ojp", 9000), new UsernamePasswordCredentials("jcannon",
				"testpassword"));
		SonarConfig sonarConfig = Mockito.mock(SonarConfig.class);
		Mockito.when(sonarConfig.getHost()).thenReturn("sonar.ojp");
		Mockito.when(sonarConfig.getHostPort()).thenReturn(9000);
		sonarDao.setConfig(sonarConfig);
	}

	@Test
	public void testGetAllChanges() throws IOException, URISyntaxException {
		String allChanges = sonarDao.getAllChanges(credsProvider);
		System.out.println(allChanges);
	}

}
