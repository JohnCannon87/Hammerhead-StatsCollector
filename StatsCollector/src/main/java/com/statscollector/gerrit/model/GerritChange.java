package com.statscollector.gerrit.model;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

/**
 * I'm an object that represents a Single Gerrit Change.
 *
 * @author JCannon
 *
 */
public class GerritChange {
	private final String id, changeId, project, owner, topic, branch;
	private final DateTime created, updated;
	private Map<String, Integer> reviewers;
	private final boolean haveCheckedForReviewers = false;
	private Integer changeNumber;

	public GerritChange(final String id, final String changeId, final String project, final String owner,
			final DateTime created, final DateTime updated, final String topic, final String branch) {
		super();
		this.id = id;
		this.changeId = changeId;
		this.project = project;
		this.owner = owner;
		this.created = created;
		this.updated = updated;
		this.topic = topic;
		this.branch = branch;
		this.reviewers = new HashMap<>();
	}

	public String getId() {
		return id;
	}

	public String getChangeId() {
		return changeId;
	}

	public String getProject() {
		return project;
	}

	public String getOwner() {
		return owner;
	}

	public DateTime getCreated() {
		return created;
	}

	public DateTime getUpdated() {
		return updated;
	}

	public Map<String, Integer> getReviewers() {
		return reviewers;
	}

	public void setReviewers(final Map<String, Integer> reviewers) {
		this.reviewers = reviewers;
	}

	public boolean haveReviewers() {
		return reviewers.isEmpty() || haveCheckedForReviewers;
	}

	public Integer getChangeNumber() {
		return changeNumber;
	}

	public void setChangeNumber(final Integer changeNumber) {
		this.changeNumber = changeNumber;
	}

	public String getTopic() {
		return topic;
	}

	public String getBranch() {
		return branch;
	}
}
