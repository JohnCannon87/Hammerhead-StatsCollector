package com.statscollector.sonar.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.statscollector.sonar.authentication.SonarAuthenticationHelper;
import com.statscollector.sonar.config.SonarConfig;
import com.statscollector.sonar.dao.SonarDao;
import com.statscollector.sonar.model.SonarProject;
import com.statscollector.sonar.model.SonarStatistics;
import com.statscollector.sonar.service.filter.FilterProjectNamePredicate;

@Service
public class SonarStatisticsService {

	@Autowired
	private SonarDao sonarDao;

	@Autowired
	private SonarAuthenticationHelper authenticationHelper;

	@Autowired
	private SonarConfig sonarConfig;

	private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendDayOfYear(4)
			.appendLiteral("-").appendMonthOfYear(2).appendLiteral("-").appendDayOfMonth(2).toFormatter();

	public List<SonarProject> getProjectsFilteredByName(final String projectFilterRegex) throws IOException,
	URISyntaxException {
		List<SonarProject> toBeFiltered = getAllSonarProjects();
		FilterProjectNamePredicate filter = new FilterProjectNamePredicate(projectFilterRegex);
		List<SonarProject> results = filter.filter(toBeFiltered);
		return results;
	}

	public List<SonarProject> getAllSonarProjects() throws IOException, URISyntaxException {
		String allChanges = sonarDao.getLatestStats(authenticationHelper.createAuthenticationCredentials());
		List<SonarProject> result = translateIntoProjectList(allChanges);
		return result;
	}

	private List<SonarProject> translateIntoProjectList(final String allChanges) {
		// Setup Parsers
		JsonParser jsonParser = new JsonParser();
		Gson gsonParser = new GsonBuilder().create();
		List<SonarProject> result = new ArrayList<>();
		JsonElement parsedJson = jsonParser.parse(allChanges);
		JsonArray jsonListOfProjects = parsedJson.getAsJsonArray();
		for (JsonElement jsonProjectElement : jsonListOfProjects) {
			JsonObject jsonProject = jsonProjectElement.getAsJsonObject();
			System.out.println(jsonProject.get("key"));
			SonarProject sonarProject = gsonParser.fromJson(jsonProject, SonarProject.class);
			result.add(sonarProject);
		}
		return result;
	}

	public void setSonarDao(final SonarDao sonarDao) {
		this.sonarDao = sonarDao;
	}

	public void setAuthenticationHelper(final SonarAuthenticationHelper authenticationHelper) {
		this.authenticationHelper = authenticationHelper;
	}

	public void getStatisticsScheduledTask() {
		// TODO Auto-generated method stub

	}

	public SonarStatistics getStatisticsForPeriod(final String startDate, final String endDate,
			final String projectRegex) throws IOException, URISyntaxException {
		List<SonarProject> projects = getProjectsFilteredByName(projectRegex);
		DateMidnight startDateMidnight = new DateMidnight(formatter.parseMillis(startDate));
		DateMidnight endDateMidnight = new DateMidnight(formatter.parseMillis(endDate));
		Interval period = new Interval(startDateMidnight, endDateMidnight);
		for (SonarProject sonarProject : projects) {
			sonarDao.getStatsForDateWindow(authenticationHelper.createAuthenticationCredentials(), period,
					sonarProject.getKey());
		}

		return null;
	}

	public SonarMetrics getStatisticsForPeriod(final Interval period) {

	}
}
