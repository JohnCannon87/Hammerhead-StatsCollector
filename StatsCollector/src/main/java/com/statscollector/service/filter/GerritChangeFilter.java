package com.statscollector.service.filter;

import java.util.List;

import com.statscollector.model.GerritChange;

public interface GerritChangeFilter {

	public List<GerritChange> filter(List<GerritChange> toBeFiltered);

}
