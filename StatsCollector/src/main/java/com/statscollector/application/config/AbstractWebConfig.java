package com.statscollector.application.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.statscollector.application.authentication.EncryptionHelper;

@JsonPropertyOrder({ "projectName", "host", "hostPort", "username", "password" })
public abstract class AbstractWebConfig {

    protected PropertiesConfiguration config;

    private final String PROJECT_NAME_KEY = "project.name";

    public AbstractWebConfig() throws ConfigurationException {
        config = new PropertiesConfiguration(System.getProperty(getConfigFilePathKey(), getDefaultFilePath()));
    }

    protected abstract String getHostKey();

    protected abstract String getHostPortKey();

    protected abstract String getConfigFilePathKey();

    protected abstract String getDefaultFilePath();

    protected abstract String getUsernameKey();

    protected abstract String getPasswordKey();

    @JsonProperty(required = true)
    public String getProjectName() {
        return config.getString(PROJECT_NAME_KEY);
    }

    public void setProjectName(final String projectName) throws ConfigurationException {
        config.setProperty(PROJECT_NAME_KEY, projectName);
        config.save();
    }

    @JsonProperty(required = true)
    public String getHost() {
        return config.getString(getHostKey());
    }

    public void setHost(final String host) throws ConfigurationException {
        config.setProperty(getHostKey(), host);
        config.save();
    }

    @JsonProperty(required = true)
    public Integer getHostPort() {
        return getIntegerWithDefaultValue(getHostPortKey(), 0);
    }

    public void setHostPort(final Integer hostPort) throws ConfigurationException {
        config.setProperty(getHostPortKey(), hostPort);
        config.save();
    }

    @JsonProperty(required = true)
    public String getUsername() {
        return config.getString(getUsernameKey());
    }

    public void setUsername(final String username) throws ConfigurationException {
        config.setProperty(getUsernameKey(), username);
        config.save();
    }

    public void setUsernameAndPassword(final String username, final String password) throws ConfigurationException {
        config.setProperty(getUsernameKey(), username);
        config.setProperty(getPasswordKey(), EncryptionHelper.encryptPassword(password));
        config.save();
    }

    @JsonProperty(required = true)
    public String getPassword() {
        return EncryptionHelper.decryptPassword(config.getString(getPasswordKey()));
    }

    public void setPassword(final String password) throws ConfigurationException {
        config.setProperty(getPasswordKey(), password);
        config.save();
    }

    public void setConfig(final PropertiesConfiguration config) {
        this.config = config;
    }

    protected Float getFloatWithDefaultValue(final String key, final Float defaultValue) {
        String value = config.getString(key);
        if(StringUtils.isEmpty(value)) {
            return defaultValue;
        } else {
            return Float.valueOf(value);
        }
    }

    protected Integer getIntegerWithDefaultValue(final String key, final Integer defaultValue) {
        try {
            String value = config.getString(key);
            if(StringUtils.isEmpty(value)) {
                return defaultValue;
            } else {
                return Integer.valueOf(value);
            }
        } catch(Exception e) {
            return config.getInteger(key, defaultValue);
        }
    }

}
