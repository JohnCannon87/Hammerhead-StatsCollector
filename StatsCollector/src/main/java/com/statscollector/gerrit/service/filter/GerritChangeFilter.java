package com.statscollector.gerrit.service.filter;

import java.util.List;

import com.statscollector.gerrit.model.GerritChange;

public interface GerritChangeFilter {

	public List<GerritChange> filter(List<GerritChange> toBeFiltered);

}
