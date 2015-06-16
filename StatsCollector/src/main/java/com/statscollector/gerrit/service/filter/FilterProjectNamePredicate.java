package com.statscollector.gerrit.service.filter;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.gerrit.extensions.common.ChangeInfo;

public class FilterProjectNamePredicate implements Predicate<ChangeInfo>, GerritChangeFilter {

	private final String projectNameRegex;

	public FilterProjectNamePredicate(final String projectNameRegex) {
		super();
		this.projectNameRegex = projectNameRegex;
	}

	@Override
	public boolean apply(final ChangeInfo input) {
		return input.project.matches(projectNameRegex);
	}

	@Override
	public List<ChangeInfo> filter(final List<ChangeInfo> toBeFiltered) {
		return Lists.newArrayList(Collections2.filter(toBeFiltered, this));
	}

}
