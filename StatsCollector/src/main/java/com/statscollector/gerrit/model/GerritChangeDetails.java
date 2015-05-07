package com.statscollector.gerrit.model;

import java.util.HashMap;
import java.util.Map;


public class GerritChangeDetails {
	
	Map<String, Integer> reviewers = new HashMap<>();

	public Map<String, Integer> getReviewers() {
		return reviewers;
	}
	
	public void addReviewer(String reviewer, Integer reviewValue){
		reviewers.put(reviewer, reviewValue);
	}

}
