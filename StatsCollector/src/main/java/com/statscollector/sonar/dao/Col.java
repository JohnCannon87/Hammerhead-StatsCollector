package com.statscollector.sonar.dao;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Col {

	@Expose
	private String metric;

	/**
	 * 
	 * @return The metric
	 */
	public String getMetric() {
		return metric;
	}

	/**
	 * 
	 * @param metric
	 *            The metric
	 */
	public void setMetric(final String metric) {
		this.metric = metric;
	}

}