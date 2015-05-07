package com.statscollector.gerrit.service;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.statscollector.gerrit.model.GerritChange;
import com.statscollector.gerrit.service.filter.FilterDateUpdatedPredicate;
import com.statscollector.gerrit.service.filter.FilterProjectNamePredicate;

/**
 * I'm a class that takes a list of changes from Gerrit and performs some
 * business logic on them.
 *
 * @author JCannon
 *
 */
@Component
public class GerritStatisticsHelper {

	/**
	 * I return a copy of the provided List but filtered based on the
	 * projectNameRegex provided.
	 *
	 * @param toBeFiltered
	 * @param projectNameRegex
	 * @return
	 */
	public List<GerritChange> filterChangesBasedOnProjectName(final List<GerritChange> toBeFiltered,
			final String projectNameRegex) {
		return Lists.newArrayList(Collections2.filter(toBeFiltered, new FilterProjectNamePredicate(projectNameRegex)));
	}

	/**
	 * I return a copy of the provided List but filtered based on the start and
	 * end dates provided
	 *
	 * @param toBeFiltered
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<GerritChange> filterChangesBasedOnDateRange(final List<GerritChange> toBeFiltered,
			final DateTime startDate, final DateTime endDate) {
		return Lists
				.newArrayList(Collections2.filter(toBeFiltered, new FilterDateUpdatedPredicate(startDate, endDate)));
	}

}
