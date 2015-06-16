package com.statscollector.gerrit.service.filter;

import java.util.List;

import org.joda.time.DateTime;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.gerrit.extensions.common.ChangeInfo;

public class FilterDateUpdatedPredicate implements Predicate<ChangeInfo>, GerritChangeFilter {

	private final DateTime startDate, endDate;

	public FilterDateUpdatedPredicate(final DateTime startDate, final DateTime endDate) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;

	}

	@Override
	public boolean apply(final ChangeInfo input) {
		DateTime updated = new DateTime(input.updated);
		return updated.isAfter(startDate) && updated.isBefore(endDate);
	}

	@Override
	public List<ChangeInfo> filter(final List<ChangeInfo> toBeFiltered) {
		return Lists.newArrayList(Collections2.filter(toBeFiltered, this));
	}

}
