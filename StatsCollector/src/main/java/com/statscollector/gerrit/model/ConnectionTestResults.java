package com.statscollector.gerrit.model;

import java.util.List;

import com.google.gerrit.extensions.common.ChangeInfo;

public class ConnectionTestResults {

    private String connectionDetails;
    private List<ChangeInfo> connectionResult;
    private String errorResult;

    public ConnectionTestResults(final String connectionDetails, final List<ChangeInfo> result,
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

    public List<ChangeInfo> getConnectionResult() {
        return connectionResult;
    }

    public void setConnectionResult(final List<ChangeInfo> connectionResult) {
        this.connectionResult = connectionResult;
    }

    public String getErrorResult() {
        return errorResult;
    }

    public void setErrorResult(final String errorResult) {
        this.errorResult = errorResult;
    }

}
