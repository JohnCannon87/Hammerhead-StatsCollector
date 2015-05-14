package com.statscollector.application.config;

public class AbstractTemporaryWebConfig {

	private String host, username, password;

	private Integer hostPort;

	public String getHost() {
		return host;
	}

	public void setHost(final String host) {
		this.host = host;
	}

	public Integer getHostPort() {
		return hostPort;
	}

	public void setHostPort(final Integer hostPort) {
		this.hostPort = hostPort;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

}
