package com.statscollector.sonar.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public Map<String, SonarMetric> getMetricsMap() {
		Map<String, SonarMetric> result = new HashMap<>();
		if (null != metrics) {
			for (SonarMetric sonarMetric : metrics) {
				result.put(sonarMetric.getKey(), sonarMetric);
			}
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((metrics == null) ? 0 : metrics.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SonarProject other = (SonarProject) obj;
		if (key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		if (language == null) {
			if (other.language != null) {
				return false;
			}
		} else if (!language.equals(other.language)) {
			return false;
		}
		if (metrics == null) {
			if (other.metrics != null) {
				return false;
			}
		} else if (!metrics.equals(other.metrics)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "SonarProject [key=" + key + ", name=" + name + ", language=" + language + ", metrics=" + metrics + "]";
	}

}
