package com.statscollector.sonar.config;

import org.apache.commons.configuration.ConfigurationException;

import com.statscollector.application.config.AbstractWebConfig;
import com.statscollector.application.config.WebConfig;

public class SonarConfig extends AbstractWebConfig implements WebConfig {
	/**
	 * KEYS
	 */
	private static final String HOST_KEY = "sonar.host";
	private static final String HOST_PORT_KEY = "sonar.hostPort";
	private static final String CONFIG_FILE_PATH_KEY = "configFile.path";
	private static final String DEFAULT_FILE_PATH = "SonarStatistics.properties";
	private static final String USERNAME_KEY = "sonar.username";
	private static final String PASSWORD_KEY = "sonar.password";

	public SonarConfig() throws ConfigurationException {
		super();
	}

	@Override
	protected String getHostKey() {
		return HOST_KEY;
	}

	@Override
	protected String getHostPortKey() {
		return HOST_PORT_KEY;
	}

	@Override
	protected String getConfigFilePathKey() {
		return CONFIG_FILE_PATH_KEY;
	}

	@Override
	protected String getDefaultFilePath() {
		return DEFAULT_FILE_PATH;
	}

	@Override
	protected String getUsernameKey() {
		return USERNAME_KEY;
	}

	@Override
	protected String getPasswordKey() {
		return PASSWORD_KEY;
	}

}
