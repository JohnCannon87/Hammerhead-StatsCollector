package com.statscollector.gerrit.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.statscollector.application.authentication.AbstractAuthenticationHelper;
import com.statscollector.application.config.WebConfig;
import com.statscollector.gerrit.config.GerritConfig;

/**
 * I'm a helper that creates authentication information for a user to access
 * Gerrit.
 *
 * @author JCannon
 *
 */
@Component
public class GerritAuthenticationHelper extends AbstractAuthenticationHelper {

	@Autowired
	private GerritConfig gerritConfig;

	@Override
	protected WebConfig getConfig() {
		return gerritConfig;
	}

	public void setGerritConfig(final GerritConfig gerritConfig) {
		this.gerritConfig = gerritConfig;
	}
}
