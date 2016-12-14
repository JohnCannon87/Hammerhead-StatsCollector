package com.statscollector.application.config;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "projectName", "host", "hostPort", "username", "password" })
public class AbstractWebConfig implements WebConfig {

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

    /**
     * Returns null to stop the value being returned when being exported as JSON.
     */
    public String getPassword() {
        return null;
    }

    public String getActualPassword() {
        return this.password;
    }

    public void setActualPassword(final String password) {
        if(!StringUtils.isEmpty(password)) {
            this.password = password;
        }
    }

    public void setPassword(final String password) {
        if(!StringUtils.isEmpty(password)) {
            this.password = password;
        }
    }

    @JsonProperty(required = true)
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(final String projectName) {
        this.projectName = projectName;
    }

}
