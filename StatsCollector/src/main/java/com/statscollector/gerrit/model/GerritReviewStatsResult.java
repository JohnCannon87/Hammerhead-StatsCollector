package com.statscollector.gerrit.model;

import java.util.List;

public class GerritReviewStatsResult {

	private Boolean success;

	private Throwable error;

	private List<GerritChange> changes;

	public GerritReviewStatsResult(Boolean success, List<GerritChange> changes) {
		this(success, null, changes);
	}

	public GerritReviewStatsResult(Boolean success, Throwable error,
			List<GerritChange> changes) {
		this.success = success;
		this.error = error;
		this.changes = changes;
	}

	public Boolean getSuccess() {
		return success;
	}

	public Throwable getError() {
		return error;
	}

	public List<GerritChange> getChanges() {
		return changes;
	}

}
