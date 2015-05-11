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
	private CredentialsProvider credsProvider;
	private boolean credsProviderCreated;

	public CredentialsProvider createAuthenticationCredentials() {
		if (!credsProviderCreated) {
			credsProvider = new BasicCredentialsProvider();
			credsProvider
			.setCredentials(new AuthScope(gerritConfig.getHost(), gerritConfig.getHostPort()), new UsernamePasswordCredentials(gerritConfig.getUsername(), gerritConfig.getPassword()));
		}
		return credsProvider;
	}

	public void setGerritConfig(GerritConfig gerritConfig) {
		this.gerritConfig = gerritConfig;
	}
}
