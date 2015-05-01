package com.statscollector.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.statscollector.model.GerritChange;
import com.statscollector.service.filter.FilterDateUpdatedPredicate;
import com.statscollector.service.filter.FilterProjectNamePredicate;
import com.statscollector.service.filter.GerritChangeFilter;

/**
 * I'm a service to return information based on a Gerrit instance.
 *
 * @author JCannon
 *
 */
@Service
public class GerritStatisticsService {

	@Autowired
	private GerritService gerritService;

	@Autowired
	private GerritStatisticsHelper gerritStatisticsHelper;

	/**
	 * I return a list of changes with unwanted changes filtered out based on
	 * the provided parameters.
	 * 
	 * @param projectFilterRegex
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public List<GerritChange> getChangesBasedOnParameters(final String projectFilterRegex, final DateTime startDate,
			final DateTime endDate) throws IOException, URISyntaxException {
		List<GerritChange> allChanges = gerritService.getAllChanges();
		return filterChanges(allChanges, getFilters(projectFilterRegex, startDate, endDate));
	}

	/**
	 * I act upon the provided list of GerritChangeFilters to remove all
	 * unwanted GerritChanges.
	 *
	 * @param allChanges
	 * @param filters
	 * @return
	 */
	private List<GerritChange> filterChanges(final List<GerritChange> allChanges, final List<GerritChangeFilter> filters) {
		List<GerritChange> results = Lists.newArrayList(allChanges);
		for (GerritChangeFilter filter : filters) {
			results = filter.filter(results);
		}
		return results;
	}

	/**
	 * Returns a list of filters to run against the change list, important to
	 * think about order here, the filter that will remove the largest set
	 * should be first, then the next largest set etc, should help with
	 * performance.
	 *
	 * @param projectFilterRegex
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private List<GerritChangeFilter> getFilters(final String projectFilterRegex, final DateTime startDate,
			final DateTime endDate) {
		List<GerritChangeFilter> results = new ArrayList<GerritChangeFilter>();
		results.add(new FilterDateUpdatedPredicate(startDate, endDate));
		results.add(new FilterProjectNamePredicate(projectFilterRegex));
		return results;
	}

}
