package com.statscollector.gerrit.model;

import java.util.List;

import com.google.gerrit.extensions.common.ChangeInfo;

public class GerritReviewStatsResult {

	private final Boolean success;

	private final Throwable error;

	private final List<ChangeInfo> changes;

	public GerritReviewStatsResult(final Boolean success, final List<ChangeInfo> changes) {
		this(success, null, changes);
	}

	public GerritReviewStatsResult(final Boolean success, final Throwable error, final List<ChangeInfo> changes) {
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

	public List<ChangeInfo> getChanges() {
		return changes;
	}

}
