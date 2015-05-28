package com.statscollector.application.authentication;

import static org.junit.Assert.assertEquals;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.statscollector.application.config.WebConfig;

public class AbstractAuthenticationHelperTest {

	protected static final String TEST_USERNAME = "testUser";
	protected static final String TEST_PASSWORD = "testPass";
	protected static final Integer TEST_PORT = 8080;
	protected static final String TEST_HOST = "testHost";

	private final WebConfig testConfig = Mockito.mock(WebConfig.class);
	private AbstractAuthenticationHelper testHelper;

	@Before
	public void setUp() throws Exception {
		Mockito.when(testConfig.getUsername()).thenReturn(TEST_USERNAME);
		Mockito.when(testConfig.getPassword()).thenReturn(TEST_PASSWORD);

		testHelper = new AbstractAuthenticationHelper() {

			@Override
			protected WebConfig getConfig() {
				return testConfig;
			}
		};
	}

	@Test
	public void credentialsProviderBeanTest() {
		CredentialsProvider credentialsProvider = testHelper.credentialsProvider();
		Credentials credentials = credentialsProvider.getCredentials(AuthScope.ANY);
		assertEquals(TEST_USERNAME, credentials.getUserPrincipal().getName());
		assertEquals(TEST_PASSWORD, credentials.getPassword());

		Credentials sameCredentials = credentialsProvider.getCredentials(AuthScope.ANY);

		assertEquals(credentials, sameCredentials);

		// Now Change Config
		String newPassword = "NewPassword";
		Mockito.when(testConfig.getPassword()).thenReturn(newPassword);
		Credentials newCredentials = credentialsProvider.getCredentials(AuthScope.ANY);
		assertEquals(TEST_USERNAME, newCredentials.getUserPrincipal().getName());
		assertEquals(newPassword, newCredentials.getPassword());
	}
}
