package com.statscollector.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

/**
 * I'm an object that represents a Single Gerrit Change.
 *
 * @author JCannon
 *
 */
public class GerritChange {
	private final String id, changeId, project, owner;
	private final DateTime created, updated;
	private final List<String> reviewers;
	private final boolean haveCheckedForReviewers = false;

	public GerritChange(final String id, final String changeId, final String project, final String owner,
			final DateTime created, final DateTime updated) {
		super();
		this.id = id;
		this.changeId = changeId;
		this.project = project;
		this.owner = owner;
		this.created = created;
		this.updated = updated;
		this.reviewers = new ArrayList<>();
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

	public List<String> getReviewers() {
		return reviewers;
	}

	public boolean haveReviewers() {
		return reviewers.isEmpty() || haveCheckedForReviewers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((changeId == null) ? 0 : changeId.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((project == null) ? 0 : project.hashCode());
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
		if (changeId == null) {
			if (other.changeId != null) {
				return false;
			}
		} else if (!changeId.equals(other.changeId)) {
			return false;
		}
		if (created == null) {
			if (other.created != null) {
				return false;
			}
		} else if (!created.equals(other.created)) {
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
		return "Change [id=" + id + ", changeId=" + changeId + ", project=" + project + ", owner=" + owner
				+ ", created=" + created + ", updated=" + updated + "]";
	}
}
