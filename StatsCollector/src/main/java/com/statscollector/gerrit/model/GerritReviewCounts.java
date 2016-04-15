package com.statscollector.gerrit.model;

public class GerritReviewCounts {

    int noPeerReviewCount, onePeerReviewCount, twoPeerReviewCount, collaborativeDevelopmentCount = 0;

    public GerritReviewCounts() {
    }

    public GerritReviewCounts(final int noPeerReviewCount, final int onePeerReviewCount, final int twoPeerReviewCount,
            final int collaborativeDevelopmentCount) {
        this.noPeerReviewCount = noPeerReviewCount;
        this.onePeerReviewCount = onePeerReviewCount;
        this.twoPeerReviewCount = twoPeerReviewCount;
        this.collaborativeDevelopmentCount = collaborativeDevelopmentCount;
    }

    public void incrementNoPeerReviewCount() {
        noPeerReviewCount++;
    }

    public void incrementOnePeerReviewCount() {
        onePeerReviewCount++;
    }

    public void incrementTwoPeerReviewCount() {
        twoPeerReviewCount++;
    }

    public void incrementCollaborativeDevelopmentCount() {
        collaborativeDevelopmentCount++;
    }

    public int getNoPeerReviewCount() {
        return noPeerReviewCount;
    }

    public void setNoPeerReviewCount(final int noPeerReviewCount) {
        this.noPeerReviewCount = noPeerReviewCount;
    }

    public int getOnePeerReviewCount() {
        return onePeerReviewCount;
    }

    public void setOnePeerReviewCount(final int onePeerReviewCount) {
        this.onePeerReviewCount = onePeerReviewCount;
    }

    public int getTwoPeerReviewCount() {
        return twoPeerReviewCount;
    }

    public void setTwoPeerReviewCount(final int twoPeerReviewCount) {
        this.twoPeerReviewCount = twoPeerReviewCount;
    }

    public int getCollaborativeDevelopmentCount() {
        return collaborativeDevelopmentCount;
    }

    public void setCollaborativeDevelopmentCount(final int collaborativeDevelopmentCount) {
        this.collaborativeDevelopmentCount = collaborativeDevelopmentCount;
    }

}
