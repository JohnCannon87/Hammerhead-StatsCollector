package com.statscollector.sonar.config;

import org.apache.commons.configuration.ConfigurationException;

import com.statscollector.application.config.AbstractWebConfig;

public class SonarConfig extends AbstractWebConfig{
	/**
	 * KEYS
	 */
	private static final String HOST_KEY = "sonar.host";
	private static final String HOST_PORT_KEY = "sonar.hostPort";
	private static final String CONFIG_FILE_PATH_KEY = "configFile.path";
	private static final String DEFAULT_FILE_PATH = "SonarStatistics.properties";

	public SonarConfig() throws ConfigurationException {
		super();
	}
	
	protected String getHostKey(){
		return HOST_KEY;
	}
	
	protected String getHostPortKey(){
		return HOST_PORT_KEY;
	}
	
	protected String getConfigFilePathKey(){
		return CONFIG_FILE_PATH_KEY;
	}
	
	protected String getDefaultFilePath(){
		return DEFAULT_FILE_PATH;
	}

}
