package com.statscollector.sonar.model;

import java.util.List;

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
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public List<SonarMetric> getMetrics() {
		return metrics;
	}
	public void setMetrics(List<SonarMetric> metrics) {
		this.metrics = metrics;
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SonarProject other = (SonarProject) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (metrics == null) {
			if (other.metrics != null)
				return false;
		} else if (!metrics.equals(other.metrics))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "SonarProject [key=" + key + ", name=" + name + ", language=" + language + ", metrics=" + metrics + "]";
	}
	
}
