package com.statscollector.neo.sonar.config;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.statscollector.application.config.AbstractWebConfig;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonPropertyOrder({ "host", "hostPort", "username", "password",
        "infoWeighting", "minorWeighting", "majorWeighting", "criticalWeighting", "blockerWeighting" })
@EqualsAndHashCode(callSuper = true)
@ToString
public class SonarConfig extends AbstractWebConfig {

    final static Logger LOGGER = Logger.getLogger(SonarConfig.class);

    private String infoWeighting, minorWeighting, majorWeighting, criticalWeighting, blockerWeighting;

    @JsonCreator
    public SonarConfig() {
        LOGGER.info("Creating SonarConfig");
    }

    @JsonProperty(required = true)
    public String getInfoWeighting() {
        return infoWeighting;
    }

    public void setInfoWeighting(final String infoWeighting) {
        this.infoWeighting = infoWeighting;
    }

    @JsonProperty(required = true)
    public String getMinorWeighting() {
        return minorWeighting;
    }

    public void setMinorWeighting(final String minorWeighting) {
        this.minorWeighting = minorWeighting;
    }

    @JsonProperty(required = true)
    public String getMajorWeighting() {
        return majorWeighting;
    }

    public void setMajorWeighting(final String majorWeighting) {
        this.majorWeighting = majorWeighting;
    }

    @JsonProperty(required = true)
    public String getCriticalWeighting() {
        return criticalWeighting;
    }

    public void setCriticalWeighting(final String criticalWeighting) {
        this.criticalWeighting = criticalWeighting;
    }

    @JsonProperty(required = true)
    public String getBlockerWeighting() {
        return blockerWeighting;
    }

    public void setBlockerWeighting(final String blockerWeighting) {
        this.blockerWeighting = blockerWeighting;
    }

    public void replaceWith(final SonarConfig newSonarConfig) {
        BeanUtils.copyProperties(newSonarConfig, this);
    }

}
