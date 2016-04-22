package com.statscollector.gerrit.model;

import java.util.List;

public class ConnectionTestResults<T extends Object> {

    private String connectionDetails;
    private List<T> connectionResult;
    private String errorResult;

    public ConnectionTestResults(final String connectionDetails, final List<T> result,
            final String errorResult) {
        super();
        this.connectionDetails = connectionDetails;
        this.connectionResult = result;
        this.errorResult = errorResult;
    }

    public String getConnectionDetails() {
        return connectionDetails;
    }

    public void setConnectionDetails(final String connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    public List<T> getConnectionResult() {
        return connectionResult;
    }

    public void setConnectionResult(final List<T> connectionResult) {
        this.connectionResult = connectionResult;
    }

    public String getErrorResult() {
        return errorResult;
    }

    public void setErrorResult(final String errorResult) {
        this.errorResult = errorResult;
    }

}
