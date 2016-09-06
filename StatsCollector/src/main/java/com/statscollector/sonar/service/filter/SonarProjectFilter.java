package com.statscollector.sonar.service.filter;

import java.util.List;

import com.statscollector.neo.sonar.model.SonarProject;


public interface SonarProjectFilter {

	public List<SonarProject> filter(List<SonarProject> toBeFiltered);

}
