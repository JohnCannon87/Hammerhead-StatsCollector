package com.statscollector.application.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "projectName", "host", "hostPort", "username", "password" })
public class AbstractTemporaryWebConfig {

    private String host, username, password, projectName;

    private Integer hostPort;

    @JsonProperty(required = true)
    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    @JsonProperty(required = true)
    public Integer getHostPort() {
        return hostPort;
    }

    public void setHostPort(final Integer hostPort) {
        this.hostPort = hostPort;
    }

    @JsonProperty(required = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    @JsonProperty(required = true)
    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @JsonProperty(required = true)
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(final String projectName) {
        this.projectName = projectName;
    }

}
