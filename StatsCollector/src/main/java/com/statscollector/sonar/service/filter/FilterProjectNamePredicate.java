package com.statscollector.sonar.service.filter;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.statscollector.neo.sonar.model.SonarProject;

public class FilterProjectNamePredicate implements Predicate<SonarProject>, SonarProjectFilter {

	private final String projectNameRegex;

	public FilterProjectNamePredicate(final String projectNameRegex) {
		super();
		this.projectNameRegex = projectNameRegex;
	}

	@Override
	public boolean apply(final SonarProject input) {
		return input.getName().matches(projectNameRegex);
	}

	@Override
	public List<SonarProject> filter(final List<SonarProject> toBeFiltered) {
		return Lists.newArrayList(Collections2.filter(toBeFiltered, this));
	}

}
