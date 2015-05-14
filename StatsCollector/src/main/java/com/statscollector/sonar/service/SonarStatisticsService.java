package com.statscollector.sonar.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.statscollector.sonar.model.SonarMetric;
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

	public List<SonarProject> getProjectsFilteredByName(final String projectFilterRegex) throws IOException,
	URISyntaxException {
		List<SonarProject> toBeFiltered = getAllSonarProjects();
		FilterProjectNamePredicate filter = new FilterProjectNamePredicate(projectFilterRegex);
		List<SonarProject> results = filter.filter(toBeFiltered);
		return results;
	}

	public List<SonarProject> getAllSonarProjects() throws IOException, URISyntaxException {
		String allChanges = sonarDao.getAllChanges(authenticationHelper.createAuthenticationCredentials());
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

	public SonarStatistics getStatistics() throws IOException, URISyntaxException {
		List<SonarProject> allSonarProjects = getProjectsFilteredByName(sonarConfig.getProjectRegex());
		SonarStatistics result = null;
		for (SonarProject sonarProject : allSonarProjects) {
			Map<String, SonarMetric> metricsMap = sonarProject.getMetricsMap();

			SonarMetric methodComplexityMetric = metricsMap.get(SonarMetric.METHOD_COMPLEXITY_KEY);
			SonarMetric fileComplexityMetric = metricsMap.get(SonarMetric.FILE_COMPLEXITY_KEY);
			SonarMetric rulesComplianceMetric = metricsMap.get(SonarMetric.RULES_COMPLIANCE_KEY);
			SonarMetric testCoverageMetric = metricsMap.get(SonarMetric.TEST_COVERAGE_KEY);
			SonarMetric linesOfCodeMetric = metricsMap.get(SonarMetric.LINES_OF_CODE_KEY);

			String methodComplexity = "";
			String fileComplexity = "";
			String rulesCompliance = "";
			String testCoverage = "";
			String linesOfCode = "";

			if (null != methodComplexityMetric) {
				methodComplexity = methodComplexityMetric.getValue();
			}
			if (null != fileComplexityMetric) {
				fileComplexity = fileComplexityMetric.getValue();
			}
			if (null != rulesComplianceMetric) {
				rulesCompliance = rulesComplianceMetric.getValue();
			}
			if (null != testCoverageMetric) {
				testCoverage = testCoverageMetric.getValue();
			}
			if (null != linesOfCodeMetric) {
				linesOfCode = linesOfCodeMetric.getValue();
			}

			result = new SonarStatistics(methodComplexity, fileComplexity, rulesCompliance, testCoverage, linesOfCode);
		}
		return result;
	}
}
