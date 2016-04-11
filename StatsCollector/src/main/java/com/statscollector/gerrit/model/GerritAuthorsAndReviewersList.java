package com.statscollector.gerrit.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GerritAuthorsAndReviewersList {

    public List<GerritUserCount> authorsCountList;
    public List<GerritUserCount> reviewersCountList;

    public GerritAuthorsAndReviewersList(final Collection<GerritUserCount> authorsCountList,
            final Collection<GerritUserCount> reviewersCountList) {
        this.authorsCountList = new ArrayList<GerritUserCount>(authorsCountList);
        this.reviewersCountList = new ArrayList<GerritUserCount>(reviewersCountList);
        Collections.sort(this.authorsCountList);
        Collections.sort(this.reviewersCountList);
    }

    public Collection<GerritUserCount> getAuthorsCountList() {
        return authorsCountList;
    }

    public Collection<GerritUserCount> getReviewersCountList() {
        return reviewersCountList;
    }

}
