package com.statscollector.sonar.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class DatedSonarMetric {

	@Expose
	private List<Col> cols = new ArrayList<Col>();
	@Expose
	private List<Cell> cells = new ArrayList<Cell>();

	/**
	 * 
	 * @return The cols
	 */
	public List<Col> getCols() {
		return cols;
	}

	/**
	 * 
	 * @param cols
	 *            The cols
	 */
	public void setCols(final List<Col> cols) {
		this.cols = cols;
	}

	/**
	 * 
	 * @return The cells
	 */
	public List<Cell> getCells() {
		return cells;
	}

	/**
	 * 
	 * @param cells
	 *            The cells
	 */
	public void setCells(final List<Cell> cells) {
		this.cells = cells;
	}

}