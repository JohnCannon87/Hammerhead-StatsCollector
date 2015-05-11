package com.statscollector.gerrit.config;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.springframework.stereotype.Component;

import com.statscollector.application.config.AbstractWebConfig;

@Component
public class GerritConfig extends AbstractWebConfig{
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
	private static final String CONFIG_FILE_PATH_KEY = "configFile.path";
	private static final String DEFAULT_FILE_PATH = "GerritStatistics.properties";
	private static final String TOPIC_REGEX_KEY = "gerrit.topicRegex";
	private static final String THREAD_SPLIT_SIZE = "gerrit.threadSplitSize";
	private final DecimalFormat df = new DecimalFormat("###.##");

	public GerritConfig() throws ConfigurationException {
		super();
	}
	
	protected String getHostKey(){
		return HOST_KEY;
	}
	
	protected String getHostPortKey(){
		return HOST_PORT_KEY;
	}
	
	protected String getConfigFilePathKey(){
		return CONFIG_FILE_PATH_KEY;
	}
	
	protected String getDefaultFilePath(){
		return DEFAULT_FILE_PATH;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<String> getReviewersToIgnore() {
		return (List) config.getList(REVIEWERS_TO_IGNORE_KEY);
	}

	public void setReviewersToIgnore(final List<String> reviewersToIgnore) throws ConfigurationException {
		config.setProperty(REVIEWERS_TO_IGNORE_KEY, reviewersToIgnore);
		config.save();
	}

	public void addReviewer(final String reviewer) throws ConfigurationException {
		List<String> reviewersToIgnore = getReviewersToIgnore();
		reviewersToIgnore.add(reviewer);
		setReviewersToIgnore(reviewersToIgnore);
	}

	public void removeReviewer(final String reviewer) throws ConfigurationException {
		List<String> reviewersToIgnore = getReviewersToIgnore();
		reviewersToIgnore.remove(reviewer);
		setReviewersToIgnore(reviewersToIgnore);
	}

	public String getUsername() {
		return config.getString(USERNAME_KEY);
	}

	public void setUsernameAndPassword(final String username, final String password) throws ConfigurationException {
		config.setProperty(USERNAME_KEY, username);
		config.setProperty(PASSWORD_KEY, password);
		config.save();
	}

	public String getPassword() {
		return config.getString(PASSWORD_KEY);
	}

	public void setTargets(final Float noPeerReviewTarget, final Float onePeerReviewTarget,
			final Float twoPeerReviewTarget, final Float collaborativeDevelopmentTarget) throws ConfigurationException {
		config.setProperty(NO_PEER_REVIEW_TARGET_KEY, df.format(noPeerReviewTarget));
		config.setProperty(ONE_PEER_REVIEW_TARGET_KEY, df.format(onePeerReviewTarget));
		config.setProperty(TWO_PEER_REVIEW_TARGET_KEY, df.format(twoPeerReviewTarget));
		config.setProperty(COLLABORATIVE_REVIEW_TARGET_KEY, df.format(collaborativeDevelopmentTarget));
		config.save();
	}

	public Float getNoPeerReviewTarget() {
		return config.getFloat(NO_PEER_REVIEW_TARGET_KEY);
	}

	public Float getOnePeerReviewTarget() {
		return config.getFloat(ONE_PEER_REVIEW_TARGET_KEY);
	}

	public Float getTwoPeerReviewTarget() {
		return config.getFloat(TWO_PEER_REVIEW_TARGET_KEY);
	}

	public Float getCollaborativeReviewTarget() {
		return config.getFloat(COLLABORATIVE_REVIEW_TARGET_KEY);
	}

	public String getConfigVersion() {
		return config.getString(CONFIG_VERSION_KEY);
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

	public Integer getThreadSplitSize() {
		return config.getInt(THREAD_SPLIT_SIZE);
	}
	
	public void setThreadSplitSize(Integer threadSplitSize) throws ConfigurationException{
		config.setProperty(THREAD_SPLIT_SIZE, threadSplitSize);
		config.save();
	}

	public void replaceWith(final GerritConfig newGerritConfig) throws ConfigurationException {
		this.setHost(newGerritConfig.getHost());
		this.setHostPort(newGerritConfig.getHostPort());
		this.setReviewersToIgnore(newGerritConfig.getReviewersToIgnore());
		this.setTargets(newGerritConfig.getNoPeerReviewTarget(), newGerritConfig.getOnePeerReviewTarget(),
				newGerritConfig.getTwoPeerReviewTarget(), newGerritConfig.getCollaborativeReviewTarget());
		this.setUsernameAndPassword(newGerritConfig.getUsername(), newGerritConfig.getPassword());
		this.setTopicRegex(newGerritConfig.getTopicRegex());
		this.setThreadSplitSize(newGerritConfig.getThreadSplitSize());
	}
}
