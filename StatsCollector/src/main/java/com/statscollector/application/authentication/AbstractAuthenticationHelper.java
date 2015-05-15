package com.statscollector.application.authentication;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;

import com.statscollector.application.config.WebConfig;

/**
 * I am an abstract class that provides common authentication methods for an
 * extending class
 *
 * @author JCannon
 *
 */
public abstract class AbstractAuthenticationHelper {

	private CredentialsProvider credsProvider;
	private boolean credsProviderCreated = false;

	public CredentialsProvider createAuthenticationCredentials() {
		if (!credsProviderCreated) {
			WebConfig config = getConfig();
			credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(config.getHost(), config.getHostPort()),
					new UsernamePasswordCredentials(config.getUsername(), config.getPassword()));
			credsProviderCreated = true;
		}
		return credsProvider;
	}

	protected abstract WebConfig getConfig();

}
