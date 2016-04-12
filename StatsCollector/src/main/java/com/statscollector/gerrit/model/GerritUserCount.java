package com.statscollector.gerrit.model;

import com.google.gerrit.extensions.common.AccountInfo;

public class GerritUserCount implements Comparable<GerritUserCount> {

    private final AccountInfo user;
    private Integer count;

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

    @Override
    public int compareTo(final GerritUserCount o) {
        return o.count.compareTo(this.count);
    }

}
