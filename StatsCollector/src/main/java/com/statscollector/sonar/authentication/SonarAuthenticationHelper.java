package com.statscollector.sonar.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.statscollector.application.authentication.AbstractAuthenticationHelper;
import com.statscollector.application.config.WebConfig;
import com.statscollector.sonar.config.SonarConfig;

/**
 * I'm a helper that creates authentication information for a user to access
 * Sonar.
 *
 * @author JCannon
 *
 */
@Component
public class SonarAuthenticationHelper extends AbstractAuthenticationHelper {

	@Autowired
	private SonarConfig sonarConfig;

	@Override
	protected WebConfig getConfig() {
		return sonarConfig;
	}

	public void setSonarConfig(final SonarConfig sonarConfig) {
		this.sonarConfig = sonarConfig;
	}

}
