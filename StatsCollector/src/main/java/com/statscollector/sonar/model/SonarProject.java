package com.statscollector.sonar.model;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.google.gson.annotations.SerializedName;

public class SonarProject {
	@SerializedName("key")
	private String key;
	@SerializedName("name")
	private String name;
	@SerializedName("lang")
	private String language;
	@SerializedName("msr")
	private List<SonarMetric> metrics;
	private Map<DateTime, Map<String, SonarMetric>> datedMetricsMaps;

	public String getKey() {
		return key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(final String language) {
		this.language = language;
	}

	public List<SonarMetric> getMetrics() {
		return metrics;
	}

	public void setMetrics(final List<SonarMetric> metrics) {
		this.metrics = metrics;
	}

	public Map<DateTime, Map<String, SonarMetric>> getDatedMetricsMaps() {
		return datedMetricsMaps;
	}

	public void setDatedMetricsMaps(final Map<DateTime, Map<String, SonarMetric>> datedMetricsMaps) {
		this.datedMetricsMaps = datedMetricsMaps;
	}

	@Override
	public String toString() {
		return "SonarProject [key=" + key + ", name=" + name + ", language=" + language + ", metrics=" + metrics
				+ ", datedMetricsMaps=" + datedMetricsMaps + "]";
	}

}
