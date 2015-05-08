package com.statscollector.gerrit.authentication;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.statscollector.gerrit.config.GerritConfig;

/**
 * I'm a helper that creates authentication information for a user to access
 * Gerrit.
 * 
 * @author JCannon
 *
 */
@Component
public class AuthenticationHelper {

	@Autowired
	private GerritConfig gerritConfig;	
	private static final int PORT = 8080;
	private static final String HOST = "nreojp.git";
	private CredentialsProvider credsProvider;
	private boolean credsProviderCreated;

	public CredentialsProvider createAuthenticationCredentials() {
		if (!credsProviderCreated) {
			credsProvider = new BasicCredentialsProvider();
			credsProvider
			.setCredentials(new AuthScope(HOST, PORT), new UsernamePasswordCredentials(gerritConfig.getUsername(), gerritConfig.getPassword()));
		}
		return credsProvider;
	}

}
