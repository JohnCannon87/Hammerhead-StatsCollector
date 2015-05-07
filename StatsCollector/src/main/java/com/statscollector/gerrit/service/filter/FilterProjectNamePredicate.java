package com.statscollector.gerrit.service.filter;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.statscollector.gerrit.model.GerritChange;

public class FilterProjectNamePredicate implements Predicate<GerritChange>, GerritChangeFilter {

	private final String projectNameRegex;

	public FilterProjectNamePredicate(final String projectNameRegex) {
		super();
		this.projectNameRegex = projectNameRegex;
	}

	@Override
	public boolean apply(final GerritChange input) {
		return input.getProject().matches(projectNameRegex);
	}

	@Override
	public List<GerritChange> filter(final List<GerritChange> toBeFiltered) {
		return Lists.newArrayList(Collections2.filter(toBeFiltered, this));
	}

}
