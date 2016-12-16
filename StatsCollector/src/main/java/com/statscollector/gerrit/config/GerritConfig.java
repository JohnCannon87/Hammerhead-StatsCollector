package com.statscollector.gerrit.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.statscollector.application.config.AbstractWebConfig;

import lombok.EqualsAndHashCode;

@JsonPropertyOrder({ "projectName", "host", "hostPort", "username", "password",
        "threadSplitSize", "reviewersToIgnore" })
@EqualsAndHashCode(callSuper = true)
public class GerritConfig extends AbstractWebConfig {

    private Integer threadSplitSize, startDateOffset, endDateOffset;
    private String reviewersToIgnore;

    @JsonCreator
    public GerritConfig() {
    }

    @JsonProperty(required = true)
    public Integer getThreadSplitSize() {
        return threadSplitSize;
    }

    public void setThreadSplitSize(final Integer threadSplitSize) {
        this.threadSplitSize = threadSplitSize;
    }

    public String getReviewersToIgnore() {
        return reviewersToIgnore;
    }

    public void setReviewersToIgnore(final String reviewersToIgnore) {
        this.reviewersToIgnore = reviewersToIgnore;
    }

    public void replaceWith(final GerritConfig newGerritConfig) {
        BeanUtils.copyProperties(newGerritConfig, this);
    }

    @JsonIgnore
    public List<String> getReviewersToIgnoreList() {
        return Arrays.asList(reviewersToIgnore.split(","));
    }

}
