package com.statscollector.application.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public abstract class AbstractWebConfig {

	protected final PropertiesConfiguration config;
	
	public AbstractWebConfig() throws ConfigurationException {
		config = new PropertiesConfiguration(System.getProperty(
				getConfigFilePathKey(), getDefaultFilePath()));
	}

	protected abstract String getHostKey();
	
	protected abstract String getHostPortKey();

	protected abstract String getConfigFilePathKey();

	protected abstract String getDefaultFilePath();
	
	public String getHost() {
		return config.getString(getHostKey());
	}

	public void setHost(final String host) throws ConfigurationException {
		config.setProperty(getHostKey(), host);
		config.save();
	}
	
	public Integer getHostPort() {
		return config.getInt(getHostPortKey());
	}
	
	public void setHostPort(Integer hostPort) throws ConfigurationException {
		config.setProperty(getHostPortKey(), hostPort);
		config.save();
	}
}
