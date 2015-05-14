package com.statscollector.application.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public abstract class AbstractWebConfig {

	protected final PropertiesConfiguration config;

	public AbstractWebConfig() throws ConfigurationException {
		config = new PropertiesConfiguration(System.getProperty(getConfigFilePathKey(), getDefaultFilePath()));
	}

	protected abstract String getHostKey();

	protected abstract String getHostPortKey();

	protected abstract String getConfigFilePathKey();

	protected abstract String getDefaultFilePath();

	protected abstract String getUsernameKey();

	protected abstract String getPasswordKey();

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

	public void setHostPort(final Integer hostPort) throws ConfigurationException {
		config.setProperty(getHostPortKey(), hostPort);
		config.save();
	}

	public String getUsername() {
		return config.getString(getUsernameKey());
	}

	public void setUsername(final String username) throws ConfigurationException {
		config.setProperty(getUsernameKey(), username);
		config.save();
	}

	public void setUsernameAndPassword(final String username, final String password) throws ConfigurationException {
		config.setProperty(getUsernameKey(), username);
		config.setProperty(getPasswordKey(), password);
		config.save();
	}

	public String getPassword() {
		return config.getString(getPasswordKey());
	}

	public void setPassword(final String password) throws ConfigurationException {
		config.setProperty(getPasswordKey(), password);
		config.save();
	}
}
