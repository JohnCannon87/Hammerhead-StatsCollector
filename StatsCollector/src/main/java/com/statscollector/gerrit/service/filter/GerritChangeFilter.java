package com.statscollector.gerrit.service.filter;

import java.util.List;

import com.google.gerrit.extensions.common.ChangeInfo;

public interface GerritChangeFilter {

	public List<ChangeInfo> filter(List<ChangeInfo> toBeFiltered);

}
