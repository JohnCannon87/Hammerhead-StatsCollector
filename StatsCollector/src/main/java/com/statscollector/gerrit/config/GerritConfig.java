package com.statscollector.gerrit.config;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.stereotype.Component;

@Component
public class GerritConfig {

	/**
	 * KEYS
	 */
	private static final String HOST_KEY = "gerrit.host";
	private static final String REVIEWERS_TO_IGNORE_KEY = "gerrit.ignoreReviewers";

	private final PropertiesConfiguration config;

	public GerritConfig() throws ConfigurationException {
		config = new PropertiesConfiguration("GerritStatistics.properties");
	}

	public String getHost() {
		return config.getString(HOST_KEY);
	}

	public void setHost(final String host) throws ConfigurationException {
		config.setProperty(HOST_KEY, host);
		config.save();
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
}
