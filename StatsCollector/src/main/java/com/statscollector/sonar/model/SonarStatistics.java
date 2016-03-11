package com.statscollector.sonar.model;

public class SonarStatistics {

    private String methodComplexity, fileComplexity, rulesCompliance, testCoverage, linesOfCode;
    private String methodComplexityTarget, fileComplexityTarget, rulesComplianceTarget, testCoverageTarget;

    public SonarStatistics(final String methodComplexity, final String fileComplexity, final String rulesCompliance,
            final String testCoverage,
            final String linesOfCode, final String methodComplexityTarget, final String fileComplexityTarget,
            final String rulesComplianceTarget, final String testCoverageTarget) {
        this.methodComplexity = methodComplexity;
        this.fileComplexity = fileComplexity;
        this.rulesCompliance = rulesCompliance;
        this.testCoverage = testCoverage;
        this.linesOfCode = linesOfCode;
        this.methodComplexityTarget = methodComplexityTarget;
        this.fileComplexityTarget = fileComplexityTarget;
        this.rulesComplianceTarget = rulesComplianceTarget;
        this.testCoverageTarget = testCoverageTarget;
    }

    public String getMethodComplexityTarget() {
        return methodComplexityTarget;
    }

    public void setMethodComplexityTarget(final String methodComplexityTarget) {
        this.methodComplexityTarget = methodComplexityTarget;
    }

    public String getFileComplexityTarget() {
        return fileComplexityTarget;
    }

    public void setFileComplexityTarget(final String fileComplexityTarget) {
        this.fileComplexityTarget = fileComplexityTarget;
    }

    public String getRulesComplianceTarget() {
        return rulesComplianceTarget;
    }

    public void setRulesComplianceTarget(final String rulesComplianceTarget) {
        this.rulesComplianceTarget = rulesComplianceTarget;
    }

    public String getTestCoverageTarget() {
        return testCoverageTarget;
    }

    public void setTestCoverageTarget(final String testCoverageTarget) {
        this.testCoverageTarget = testCoverageTarget;
    }

    public String getMethodComplexity() {
        return methodComplexity;
    }

    public void setMethodComplexity(final String methodComplexity) {
        this.methodComplexity = methodComplexity;
    }

    public String getFileComplexity() {
        return fileComplexity;
    }

    public void setFileComplexity(final String fileComplexity) {
        this.fileComplexity = fileComplexity;
    }

    public String getRulesCompliance() {
        return rulesCompliance;
    }

    public void setRulesCompliance(final String rulesCompliance) {
        this.rulesCompliance = rulesCompliance;
    }

    public String getTestCoverage() {
        return testCoverage;
    }

    public void setTestCoverage(final String testCoverage) {
        this.testCoverage = testCoverage;
    }

    public String getLinesOfCode() {
        return linesOfCode;
    }

    public void setLinesOfCode(final String linesOfCode) {
        this.linesOfCode = linesOfCode;
    }

}
