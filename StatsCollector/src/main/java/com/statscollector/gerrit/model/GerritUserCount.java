package com.statscollector.gerrit.model;

import com.google.gerrit.extensions.common.AccountInfo;

public class GerritUserCount implements Comparable<GerritUserCount> {

    private final AccountInfo user;
    private Integer count;
    private Boolean didDoOwnReview;

    public GerritUserCount(final AccountInfo user) {
        this.user = user;
        this.count = 1;
    }

    public AccountInfo getUser() {
        return user;
    }

    public Integer getCount() {
        return count;
    }

    public void incrementCount() {
        count++;
    }

    public Boolean getDidDoOwnReview() {
        return didDoOwnReview;
    }

    public void setDidDoOwnReview(final Boolean didDoOwnReview) {
        this.didDoOwnReview = didDoOwnReview;
    }

    @Override
    public int compareTo(final GerritUserCount o) {
        return o.count.compareTo(this.count);
    }

}
