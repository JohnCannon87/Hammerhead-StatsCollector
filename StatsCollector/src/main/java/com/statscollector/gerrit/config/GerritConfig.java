package com.statscollector.gerrit.config;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.statscollector.application.config.AbstractWebConfig;
import com.statscollector.application.config.WebConfig;

@Component
@JsonPropertyOrder({ "projectName", "host", "hostPort", "username", "password", "projectRegex", "topicRegex",
    "threadSplitSize", "startDateOffset", "endDateOffset", "noPeerReviewTarget", "onePeerReviewTarget",
    "twoPeerReviewTarget", "collaborativeReviewTarget", "reviewersToIgnore" })
public class GerritConfig extends AbstractWebConfig implements WebConfig {

    /**
     * KEYS
     */
    private static final String HOST_KEY = "gerrit.host";
    private static final String HOST_PORT_KEY = "gerrit.hostPort";
    private static final String REVIEWERS_TO_IGNORE_KEY = "gerrit.ignoreReviewers";
    private static final String USERNAME_KEY = "gerrit.username";
    private static final String PASSWORD_KEY = "gerrit.password";
    private static final String NO_PEER_REVIEW_TARGET_KEY = "gerrit.noPeerReviewTarget";
    private static final String ONE_PEER_REVIEW_TARGET_KEY = "gerrit.onePeerReviewTarget";
    private static final String TWO_PEER_REVIEW_TARGET_KEY = "gerrit.twoPeerReviewTarget";
    private static final String COLLABORATIVE_REVIEW_TARGET_KEY = "gerrit.collaborativeReviewTarget";
    private static final String CONFIG_VERSION_KEY = "gerrit.configVersion";
    private static final String CONFIG_FILE_PATH_KEY = "gerritConfigFile.path";
    private static final String DEFAULT_FILE_PATH = "GerritStatistics.properties";
    private static final String TOPIC_REGEX_KEY = "gerrit.topicRegex";
    private static final String THREAD_SPLIT_SIZE = "gerrit.threadSplitSize";
    private static final String START_DATE_KEY = "gerrit.startDateOffset";
    private static final String END_DATE_KEY = "gerrit.endDateOffset";
    private static final String PROJECT_REGEX_KEY = "gerrit.projectRegex";
    private final DecimalFormat df = new DecimalFormat("###.##");

    public GerritConfig() throws ConfigurationException {
        super();
    }

    @Override
    protected String getHostKey() {
        return HOST_KEY;
    }

    @Override
    protected String getHostPortKey() {
        return HOST_PORT_KEY;
    }

    @Override
    protected String getConfigFilePathKey() {
        return CONFIG_FILE_PATH_KEY;
    }

    @Override
    protected String getDefaultFilePath() {
        return DEFAULT_FILE_PATH;
    }

    @Override
    protected String getUsernameKey() {
        return USERNAME_KEY;
    }

    @Override
    protected String getPasswordKey() {
        return PASSWORD_KEY;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @JsonIgnore
    public List<String> getReviewersToIgnoreList() {
        return (List) config.getList(REVIEWERS_TO_IGNORE_KEY);
    }

    public String getReviewersToIgnore() {
        return StringUtils.join(((List) config.getList(REVIEWERS_TO_IGNORE_KEY)).toArray(), ",");
    }

    public void setReviewersToIgnore(final String reviewersToIgnore) throws ConfigurationException {
        config.setProperty(REVIEWERS_TO_IGNORE_KEY, Arrays.asList(reviewersToIgnore.split(",")));
        config.save();
    }

    public void setTargets(final Float noPeerReviewTarget, final Float onePeerReviewTarget,
            final Float twoPeerReviewTarget, final Float collaborativeDevelopmentTarget) throws ConfigurationException {
        config.setProperty(NO_PEER_REVIEW_TARGET_KEY, df.format(noPeerReviewTarget));
        config.setProperty(ONE_PEER_REVIEW_TARGET_KEY, df.format(onePeerReviewTarget));
        config.setProperty(TWO_PEER_REVIEW_TARGET_KEY, df.format(twoPeerReviewTarget));
        config.setProperty(COLLABORATIVE_REVIEW_TARGET_KEY, df.format(collaborativeDevelopmentTarget));
        config.save();
    }

    @JsonProperty(required = true)
    public Float getNoPeerReviewTarget() {
        return getFloatWithDefaultValue(NO_PEER_REVIEW_TARGET_KEY, 0.0f);
    }

    @JsonProperty(required = true)
    public Float getOnePeerReviewTarget() {
        return getFloatWithDefaultValue(ONE_PEER_REVIEW_TARGET_KEY, 0.0f);
    }

    @JsonProperty(required = true)
    public Float getTwoPeerReviewTarget() {
        return getFloatWithDefaultValue(TWO_PEER_REVIEW_TARGET_KEY, 0.0f);
    }

    @JsonProperty(required = true)
    public Float getCollaborativeReviewTarget() {
        return getFloatWithDefaultValue(COLLABORATIVE_REVIEW_TARGET_KEY, 0.0f);
    }

    public void setConfigVersion(final String configVersion) throws ConfigurationException {
        config.setProperty(CONFIG_VERSION_KEY, configVersion);
        config.save();
    }

    public String getTopicRegex() {
        return config.getString(TOPIC_REGEX_KEY);
    }

    public void setTopicRegex(final String topicRegex) throws ConfigurationException {
        config.setProperty(TOPIC_REGEX_KEY, topicRegex);
        config.save();
    }

    @JsonProperty(required = true)
    public Integer getThreadSplitSize() {
        return getIntegerWithDefaultValue(THREAD_SPLIT_SIZE, 0);
    }

    public void setThreadSplitSize(final Integer threadSplitSize) throws ConfigurationException {
        config.setProperty(THREAD_SPLIT_SIZE, threadSplitSize);
        config.save();
    }

    @JsonProperty(required = true)
    public Integer getStartDateOffset() {
        return getIntegerWithDefaultValue(START_DATE_KEY, 0);
    }

    public void setStartDateOffset(final Integer startDateOffset) throws ConfigurationException {
        config.setProperty(START_DATE_KEY, startDateOffset);
        config.save();
    }

    @JsonProperty(required = true)
    public Integer getEndDateOffset() {
        return getIntegerWithDefaultValue(END_DATE_KEY, 0);
    }

    public void setEndDateOffset(final Integer endDateOffset) throws ConfigurationException {
        config.setProperty(END_DATE_KEY, endDateOffset);
        config.save();
    }

    public String getProjectRegex() {
        return config.getString(PROJECT_REGEX_KEY);
    }

    public void setProjectRegex(final String projectRegex) throws ConfigurationException {
        config.setProperty(PROJECT_REGEX_KEY, projectRegex);
        config.save();
    }

    public void replaceWith(final TempGerritConfig newGerritConfig) {
        BeanUtils.copyProperties(newGerritConfig, this);
    }
}
