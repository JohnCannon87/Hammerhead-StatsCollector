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
        "threadSplitSize", "noPeerReviewTarget", "onePeerReviewTarget",
        "twoPeerReviewTarget", "collaborativeReviewTarget", "reviewersToIgnore" })
@EqualsAndHashCode(callSuper = true)
public class GerritConfig extends AbstractWebConfig {

    private Integer threadSplitSize, startDateOffset, endDateOffset;
    private Float noPeerReviewTarget, onePeerReviewTarget, twoPeerReviewTarget, collaborativeReviewTarget;
    private String reviewersToIgnore;
    private boolean showGerritHistory, showGerritPie, showGerritStats;

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

    @JsonProperty(required = true)
    public Float getNoPeerReviewTarget() {
        return noPeerReviewTarget;
    }

    public void setNoPeerReviewTarget(final Float noPeerReviewTarget) {
        this.noPeerReviewTarget = noPeerReviewTarget;
    }

    @JsonProperty(required = true)
    public Float getOnePeerReviewTarget() {
        return onePeerReviewTarget;
    }

    public void setOnePeerReviewTarget(final Float onePeerReviewTarget) {
        this.onePeerReviewTarget = onePeerReviewTarget;
    }

    @JsonProperty(required = true)
    public Float getTwoPeerReviewTarget() {
        return twoPeerReviewTarget;
    }

    public void setTwoPeerReviewTarget(final Float twoPeerReviewTarget) {
        this.twoPeerReviewTarget = twoPeerReviewTarget;
    }

    @JsonProperty(required = true)
    public Float getCollaborativeReviewTarget() {
        return collaborativeReviewTarget;
    }

    public void setCollaborativeReviewTarget(final Float collaborativeReviewTarget) {
        this.collaborativeReviewTarget = collaborativeReviewTarget;
    }

    public String getReviewersToIgnore() {
        return reviewersToIgnore;
    }

    public void setReviewersToIgnore(final String reviewersToIgnore) {
        this.reviewersToIgnore = reviewersToIgnore;
    }

    public boolean getShowGerritHistory() {
        return showGerritHistory;
    }

    public void setShowGerritHistory(final boolean showGerritHistory) {
        this.showGerritHistory = showGerritHistory;
    }

    public boolean getShowGerritPie() {
        return showGerritPie;
    }

    public void setShowGerritPie(final boolean showGerritPie) {
        this.showGerritPie = showGerritPie;
    }

    public boolean isShowGerritStats() {
        return showGerritStats;
    }

    public void setShowGerritStats(final boolean showGerritStats) {
        this.showGerritStats = showGerritStats;
    }

    public void replaceWith(final GerritConfig newGerritConfig) {
        BeanUtils.copyProperties(newGerritConfig, this);
    }

    @JsonIgnore
    public List<String> getReviewersToIgnoreList() {
        return Arrays.asList(reviewersToIgnore.split(","));
    }

}
