package com.statscollector.service.filter;

import java.util.List;

import org.joda.time.DateTime;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.statscollector.model.GerritChange;

public class FilterDateUpdatedPredicate implements Predicate<GerritChange>, GerritChangeFilter {

	private final DateTime startDate, endDate;

	public FilterDateUpdatedPredicate(final DateTime startDate, final DateTime endDate) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;

	}

	@Override
	public boolean apply(final GerritChange input) {
		DateTime updated = input.getUpdated();
		return updated.isAfter(startDate) && updated.isBefore(endDate);
	}

	@Override
	public List<GerritChange> filter(final List<GerritChange> toBeFiltered) {
		return Lists.newArrayList(Collections2.filter(toBeFiltered, this));
	}

}
