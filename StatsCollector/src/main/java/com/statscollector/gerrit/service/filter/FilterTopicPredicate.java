package com.statscollector.gerrit.service.filter;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.statscollector.gerrit.model.GerritChange;

public class FilterTopicPredicate implements Predicate<GerritChange>, GerritChangeFilter {

	private final String topicNameRegex;

	public FilterTopicPredicate(final String topicNameRegex) {
		super();
		this.topicNameRegex = topicNameRegex;
	}

	@Override
	public boolean apply(final GerritChange input) {
		return !input.getTopic().matches(topicNameRegex);
	}

	@Override
	public List<GerritChange> filter(final List<GerritChange> toBeFiltered) {
		return Lists.newArrayList(Collections2.filter(toBeFiltered, this));
	}

}
