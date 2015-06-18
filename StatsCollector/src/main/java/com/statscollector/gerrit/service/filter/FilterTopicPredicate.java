package com.statscollector.gerrit.service.filter;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.gerrit.extensions.common.ChangeInfo;

public class FilterTopicPredicate implements Predicate<ChangeInfo>, GerritChangeFilter {

	private final String topicNameRegex;

	public FilterTopicPredicate(final String topicNameRegex) {
		super();
		this.topicNameRegex = topicNameRegex;
	}

	@Override
	public boolean apply(final ChangeInfo input) {
		if (null != input.topic) {
			return !input.topic.matches(topicNameRegex);
		} else {
			return true;
		}

	}

	@Override
	public List<ChangeInfo> filter(final List<ChangeInfo> toBeFiltered) {
		return Lists.newArrayList(Collections2.filter(toBeFiltered, this));
	}

}
