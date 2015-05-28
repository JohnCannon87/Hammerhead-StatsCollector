package com.statscollector.sonar.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
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
import com.statscollector.sonar.dao.Cell;
import com.statscollector.sonar.dao.Col;
import com.statscollector.sonar.dao.DatedSonarMetric;
import com.statscollector.sonar.dao.SonarDao;
import com.statscollector.sonar.model.SonarMetric;
import com.statscollector.sonar.model.SonarProject;
import com.statscollector.sonar.service.filter.FilterProjectNamePredicate;

@Service
public class SonarStatisticsService {

	@Autowired
	private SonarDao sonarDao;

	@Autowired
	private SonarAuthenticationHelper authenticationHelper;

	@Autowired
	private SonarConfig sonarConfig;

	private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendYear(4, 4)
			.appendLiteral("-").appendMonthOfYear(2).appendLiteral("-").appendDayOfMonth(2).toFormatter();

	private static final DateTimeFormatter sonarTimeFormatter = new DateTimeFormatterBuilder().appendYear(4, 4)
			.appendLiteral("-").appendMonthOfYear(2).appendLiteral("-").appendDayOfMonth(2).appendLiteral("T")
			.appendHourOfDay(2).appendLiteral(":").appendMinuteOfHour(2).appendLiteral(":").appendMinuteOfHour(2)
			.appendTimeZoneOffset(null, false, 1, 1).toFormatter();

	final static Logger LOGGER = Logger.getLogger(SonarStatisticsService.class);

	public List<SonarProject> getProjectsFilteredByName(final String projectFilterRegex) throws IOException,
			URISyntaxException {
		List<SonarProject> toBeFiltered = getAllSonarProjects();
		FilterProjectNamePredicate filter = new FilterProjectNamePredicate(projectFilterRegex);
		List<SonarProject> results = filter.filter(toBeFiltered);
		return results;
	}

	public List<SonarProject> getAllSonarProjects() throws IOException, URISyntaxException {
		String allChanges = sonarDao.getLatestStats(authenticationHelper.credentialsProvider());
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

	public List<SonarProject> getStatisticsForPeriod(final String startDate, final String endDate,
			final String projectRegex) throws IOException, URISyntaxException {
		// Setup Parsers
		JsonParser jsonParser = new JsonParser();
		Gson gsonParser = new GsonBuilder().create();
		List<SonarProject> projects = getProjectsFilteredByName(projectRegex);
		DateMidnight startDateMidnight = new DateMidnight(formatter.parseMillis(startDate));
		DateMidnight endDateMidnight = new DateMidnight(formatter.parseMillis(endDate));
		Interval period = new Interval(startDateMidnight, endDateMidnight);
		for (SonarProject sonarProject : projects) {
			String rawResults = sonarDao.getStatsForDateWindow(authenticationHelper.credentialsProvider(),
					period, sonarProject.getKey());
			JsonElement rootElement = jsonParser.parse(rawResults);
			JsonArray rootArray = rootElement.getAsJsonArray();
			for (JsonElement jsonElement : rootArray) {
				DatedSonarMetric parsedResults = gsonParser.fromJson(jsonElement, DatedSonarMetric.class);
				sonarProject.setDatedMetricsMaps(translateToDatedMetricsMap(parsedResults));
			}
		}

		return projects;
	}

	private Map<DateTime, Map<String, SonarMetric>> translateToDatedMetricsMap(final DatedSonarMetric parsedResults) {
		Map<DateTime, Map<String, SonarMetric>> results = new HashMap<>();
		// Get List Of Metric Names
		List<Col> metricNames = parsedResults.getCols();
		// Get List of "Cells" (actually key value pairs, key is date, value is
		// list of metrics.
		List<Cell> cells = parsedResults.getCells();
		for (Cell cell : cells) {
			DateTime statsInstance = sonarTimeFormatter.parseDateTime(cell.getD());
			List<Double> statsValues = cell.getV();
			if (statsValues.size() != metricNames.size()) {
				LOGGER.error("Metrics Names And Values Arrays Do NOT Match In Size");
				break;
			}
			Map<String, SonarMetric> statisticsMap = new HashMap<>();
			for (int i = 0; i < statsValues.size(); i++) {
				String value = "";
				if (statsValues.get(i) != null) {
					statsValues.get(i).toString();
				}
				SonarMetric metric = new SonarMetric(metricNames.get(i).getMetric(), value, value);
				statisticsMap.put(metric.getKey(), metric);
			}
			results.put(statsInstance, statisticsMap);
		}
		return results;
	}
}
