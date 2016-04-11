package com.statscollector.gerrit.config;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.statscollector.application.config.AbstractTemporaryWebConfig;

@JsonPropertyOrder({"projectName", "host", "hostPort", "username", "password", "projectRegex", "topicRegex", "threadSplitSize", "startDateOffset", "endDateOffset", "noPeerReviewTarget", "onePeerReviewTarget", "twoPeerReviewTarget", "collaborativeReviewTarget", "reviewersToIgnore"})
public class TempGerritConfig extends AbstractTemporaryWebConfig{

	private String projectRegex, topicRegex;
	private Integer threadSplitSize, startDateOffset, endDateOffset;
	private Float noPeerReviewTarget, onePeerReviewTarget, twoPeerReviewTarget, collaborativeReviewTarget;
	private List<String> reviewersToIgnore;

    @JsonProperty(value = "Project Regex")
	public String getProjectRegex() {
		return projectRegex;
	}
	
	public void setProjectRegex(String projectRegex) {
		this.projectRegex = projectRegex;
	}

    @JsonProperty(value = "Topic Regex")
	public String getTopicRegex() {
		return topicRegex;
	}
	
	public void setTopicRegex(String topicRegex) {
		this.topicRegex = topicRegex;
	}

    @JsonProperty(value = "Thread Split Size", required=true)
	public Integer getThreadSplitSize() {
		return threadSplitSize;
	}
	
	public void setThreadSplitSize(Integer threadSplitSize) {
		this.threadSplitSize = threadSplitSize;
	}

    @JsonProperty(value = "Start Date Offset", required=true)
	public Integer getStartDateOffset() {
		return startDateOffset;
	}
	
	public void setStartDateOffset(Integer startDateOffset) {
		this.startDateOffset = startDateOffset;
	}

    @JsonProperty(value = "End Date Offset", required=true)
	public Integer getEndDateOffset() {
		return endDateOffset;
	}
	
	public void setEndDateOffset(Integer endDateOffset) {
		this.endDateOffset = endDateOffset;
	}

    @JsonProperty(value = "No Peer Review Target", required=true)
	public Float getNoPeerReviewTarget() {
		return noPeerReviewTarget;
	}
	
	public void setNoPeerReviewTarget(Float noPeerReviewTarget) {
		this.noPeerReviewTarget = noPeerReviewTarget;
	}

    @JsonProperty(value = "One Peer Review Target", required=true)
	public Float getOnePeerReviewTarget() {
		return onePeerReviewTarget;
	}
	
	public void setOnePeerReviewTarget(Float onePeerReviewTarget) {
		this.onePeerReviewTarget = onePeerReviewTarget;
	}

    @JsonProperty(value = "Two Peer Review Target", required=true)
	public Float getTwoPeerReviewTarget() {
		return twoPeerReviewTarget;
	}
	
	public void setTwoPeerReviewTarget(Float twoPeerReviewTarget) {
		this.twoPeerReviewTarget = twoPeerReviewTarget;
	}

    @JsonProperty(value = "Collaborative Review Target", required=true)
	public Float getCollaborativeReviewTarget() {
		return collaborativeReviewTarget;
	}
	
	public void setCollaborativeReviewTarget(Float collaborativeReviewTarget) {
		this.collaborativeReviewTarget = collaborativeReviewTarget;
	}

    @JsonProperty(value = "Reviewers To Ignore")
	public List<String> getReviewersToIgnore() {
		return reviewersToIgnore;
	}
	
	public void setReviewersToIgnore(List<String> reviewersToIgnore) {
		this.reviewersToIgnore = reviewersToIgnore;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((collaborativeReviewTarget == null) ? 0 : collaborativeReviewTarget.hashCode());
		result = prime * result + ((endDateOffset == null) ? 0 : endDateOffset.hashCode());
		result = prime * result + ((noPeerReviewTarget == null) ? 0 : noPeerReviewTarget.hashCode());
		result = prime * result + ((onePeerReviewTarget == null) ? 0 : onePeerReviewTarget.hashCode());
		result = prime * result + ((projectRegex == null) ? 0 : projectRegex.hashCode());
		result = prime * result + ((reviewersToIgnore == null) ? 0 : reviewersToIgnore.hashCode());
		result = prime * result + ((startDateOffset == null) ? 0 : startDateOffset.hashCode());
		result = prime * result + ((threadSplitSize == null) ? 0 : threadSplitSize.hashCode());
		result = prime * result + ((topicRegex == null) ? 0 : topicRegex.hashCode());
		result = prime * result + ((twoPeerReviewTarget == null) ? 0 : twoPeerReviewTarget.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TempGerritConfig other = (TempGerritConfig) obj;
		if (collaborativeReviewTarget == null) {
			if (other.collaborativeReviewTarget != null)
				return false;
		} else if (!collaborativeReviewTarget.equals(other.collaborativeReviewTarget))
			return false;
		if (endDateOffset == null) {
			if (other.endDateOffset != null)
				return false;
		} else if (!endDateOffset.equals(other.endDateOffset))
			return false;
		if (noPeerReviewTarget == null) {
			if (other.noPeerReviewTarget != null)
				return false;
		} else if (!noPeerReviewTarget.equals(other.noPeerReviewTarget))
			return false;
		if (onePeerReviewTarget == null) {
			if (other.onePeerReviewTarget != null)
				return false;
		} else if (!onePeerReviewTarget.equals(other.onePeerReviewTarget))
			return false;
		if (projectRegex == null) {
			if (other.projectRegex != null)
				return false;
		} else if (!projectRegex.equals(other.projectRegex))
			return false;
		if (reviewersToIgnore == null) {
			if (other.reviewersToIgnore != null)
				return false;
		} else if (!reviewersToIgnore.equals(other.reviewersToIgnore))
			return false;
		if (startDateOffset == null) {
			if (other.startDateOffset != null)
				return false;
		} else if (!startDateOffset.equals(other.startDateOffset))
			return false;
		if (threadSplitSize == null) {
			if (other.threadSplitSize != null)
				return false;
		} else if (!threadSplitSize.equals(other.threadSplitSize))
			return false;
		if (topicRegex == null) {
			if (other.topicRegex != null)
				return false;
		} else if (!topicRegex.equals(other.topicRegex))
			return false;
		if (twoPeerReviewTarget == null) {
			if (other.twoPeerReviewTarget != null)
				return false;
		} else if (!twoPeerReviewTarget.equals(other.twoPeerReviewTarget))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "TempGerritConfig [projectRegex=" + projectRegex + ", topicRegex=" + topicRegex + ", threadSplitSize="
				+ threadSplitSize + ", startDateOffset=" + startDateOffset + ", endDateOffset=" + endDateOffset
				+ ", noPeerReviewTarget=" + noPeerReviewTarget + ", onePeerReviewTarget=" + onePeerReviewTarget
				+ ", twoPeerReviewTarget=" + twoPeerReviewTarget + ", collaborativeReviewTarget="
				+ collaborativeReviewTarget + ", reviewersToIgnore=" + reviewersToIgnore + "]";
	}
	
}
