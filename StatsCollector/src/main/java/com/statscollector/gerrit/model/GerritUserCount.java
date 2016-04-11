package com.statscollector.gerrit.model;

public class GerritUserCount implements Comparable<GerritUserCount> {

    private final String username;
    private Integer count;

    public GerritUserCount(final String username) {
        this.username = username;
        this.count = 1;
    }

    public String getUsername() {
        return username;
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
