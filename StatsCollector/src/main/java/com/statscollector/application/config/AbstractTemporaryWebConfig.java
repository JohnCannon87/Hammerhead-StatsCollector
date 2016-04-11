package com.statscollector.application.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"projectName", "host", "hostPort", "username", "password"})
public class AbstractTemporaryWebConfig {

	private String host, username, password, projectName;

	private Integer hostPort;

    @JsonProperty(value = "Host", required=true)
	public String getHost() {
		return host;
	}

	public void setHost(final String host) {
		this.host = host;
	}

    @JsonProperty(value = "Host Port", required=true)
	public Integer getHostPort() {
		return hostPort;
	}

	public void setHostPort(final Integer hostPort) {
		this.hostPort = hostPort;
	}

    @JsonProperty(value = "Username", required=true)
	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

    @JsonProperty(value = "Password", required=true)
	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

    @JsonProperty(value = "Project Name", required=true)
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
