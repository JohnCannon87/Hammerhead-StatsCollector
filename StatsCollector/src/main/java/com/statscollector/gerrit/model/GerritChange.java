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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((branch == null) ? 0 : branch.hashCode());
		result = prime * result + ((changeId == null) ? 0 : changeId.hashCode());
		result = prime * result + ((changeNumber == null) ? 0 : changeNumber.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + (haveCheckedForReviewers ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((project == null) ? 0 : project.hashCode());
		result = prime * result + ((reviewers == null) ? 0 : reviewers.hashCode());
		result = prime * result + ((topic == null) ? 0 : topic.hashCode());
		result = prime * result + ((updated == null) ? 0 : updated.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		GerritChange other = (GerritChange) obj;
		if (branch == null) {
			if (other.branch != null) {
				return false;
			}
		} else if (!branch.equals(other.branch)) {
			return false;
		}
		if (changeId == null) {
			if (other.changeId != null) {
				return false;
			}
		} else if (!changeId.equals(other.changeId)) {
			return false;
		}
		if (changeNumber == null) {
			if (other.changeNumber != null) {
				return false;
			}
		} else if (!changeNumber.equals(other.changeNumber)) {
			return false;
		}
		if (created == null) {
			if (other.created != null) {
				return false;
			}
		} else if (!created.equals(other.created)) {
			return false;
		}
		if (haveCheckedForReviewers != other.haveCheckedForReviewers) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (owner == null) {
			if (other.owner != null) {
				return false;
			}
		} else if (!owner.equals(other.owner)) {
			return false;
		}
		if (project == null) {
			if (other.project != null) {
				return false;
			}
		} else if (!project.equals(other.project)) {
			return false;
		}
		if (reviewers == null) {
			if (other.reviewers != null) {
				return false;
			}
		} else if (!reviewers.equals(other.reviewers)) {
			return false;
		}
		if (topic == null) {
			if (other.topic != null) {
				return false;
			}
		} else if (!topic.equals(other.topic)) {
			return false;
		}
		if (updated == null) {
			if (other.updated != null) {
				return false;
			}
		} else if (!updated.equals(other.updated)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "GerritChange [id=" + id + ", changeId=" + changeId + ", project=" + project + ", owner=" + owner
				+ ", topic=" + topic + ", branch=" + branch + ", created=" + created + ", updated=" + updated
				+ ", reviewers=" + reviewers + ", haveCheckedForReviewers=" + haveCheckedForReviewers
				+ ", changeNumber=" + changeNumber + "]";
	}
}
