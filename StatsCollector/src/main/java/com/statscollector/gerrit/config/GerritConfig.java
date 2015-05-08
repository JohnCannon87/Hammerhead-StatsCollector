package com.statscollector.gerrit.config;

import java.text.DecimalFormat;
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
	private static final String USERNAME_KEY = "gerrit.username";
	private static final String PASSWORD_KEY = "gerrit.password";
	private static final String NO_PEER_REVIEW_TARGET_KEY = "gerrit.noPeerReviewTarget";
	private static final String ONE_PEER_REVIEW_TARGET_KEY = "gerrit.onePeerReviewTarget";
	private static final String TWO_PEER_REVIEW_TARGET_KEY = "gerrit.twoPeerReviewTarget";
	private static final String COLLABORATIVE_REVIEW_TARGET_KEY = "gerrit.collaborativeReviewTarget";
	private DecimalFormat df = new DecimalFormat("###.##");

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
	
	public String getUsername() {
		return config.getString(USERNAME_KEY);
	}

	public void setUsernameAndPassword(String username, String password) throws ConfigurationException {
		config.setProperty(USERNAME_KEY, username);
		config.setProperty(PASSWORD_KEY, password);
		config.save();
	}
	
	public String getPassword() {
		return config.getString(PASSWORD_KEY);
	}
	
	public void setTargets(Float noPeerReviewTarget, Float onePeerReviewTarget, Float twoPeerReviewTarget, Float collaborativeDevelopmentTarget) throws ConfigurationException{
		config.setProperty(NO_PEER_REVIEW_TARGET_KEY, df.format(noPeerReviewTarget));
		config.setProperty(ONE_PEER_REVIEW_TARGET_KEY, df.format(onePeerReviewTarget));
		config.setProperty(TWO_PEER_REVIEW_TARGET_KEY, df.format(twoPeerReviewTarget));
		config.setProperty(COLLABORATIVE_REVIEW_TARGET_KEY, df.format(collaborativeDevelopmentTarget));
		config.save();
	}
	
	public Float getNoPeerReviewTarget(){
		return config.getFloat(NO_PEER_REVIEW_TARGET_KEY);
	}
	
	public Float getOnePeerReviewTarget(){
		return config.getFloat(ONE_PEER_REVIEW_TARGET_KEY);
	}
	
	public Float getTwoPeerReviewTarget(){
		return config.getFloat(TWO_PEER_REVIEW_TARGET_KEY);
	}
	
	public Float getCollaborativeReviewTarget(){
		return config.getFloat(COLLABORATIVE_REVIEW_TARGET_KEY);
	}
}
