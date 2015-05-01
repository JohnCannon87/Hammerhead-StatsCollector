package com.statscollector.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class GerritConfig {

	/**
	 * A List of reviewers to ignore, e.g. automated tools (jenkins) etc. 
	 */
	private List<String> reviewersToIgnore = new ArrayList<>();

	
	public List<String> getReviewersToIgnore() {
		return reviewersToIgnore;
	}
	
	public void addReviewerToIgnore(String reviewer) {
		reviewersToIgnore.add(reviewer);
	}
}
