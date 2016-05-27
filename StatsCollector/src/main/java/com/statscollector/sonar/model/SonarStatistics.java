package com.statscollector.sonar.model;

import java.util.Map;

public class SonarStatistics {

    private String methodComplexity, fileComplexity, rulesCompliance, testCoverage, linesOfCode;
    private Map<Integer, Integer> functionComplexityDistribution, fileComplexityDistribution;

    public SonarStatistics(final String methodComplexity, final String fileComplexity, final String rulesCompliance,
            final String testCoverage,
            final String linesOfCode, final Map<Integer, Integer> functionComplexityDistribution,
            final Map<Integer, Integer> fileComplexityDistribution) {
        this.methodComplexity = methodComplexity;
        this.fileComplexity = fileComplexity;
        this.rulesCompliance = rulesCompliance;
        this.testCoverage = testCoverage;
        this.linesOfCode = linesOfCode;
        this.functionComplexityDistribution = functionComplexityDistribution;
        this.fileComplexityDistribution = fileComplexityDistribution;
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

    public Map<Integer, Integer> getFunctionComplexityDistribution() {
        return functionComplexityDistribution;
    }

    public void setFunctionComplexityDistribution(final Map<Integer, Integer> functionComplexityDistribution) {
        this.functionComplexityDistribution = functionComplexityDistribution;
    }

    public Map<Integer, Integer> getFileComplexityDistribution() {
        return fileComplexityDistribution;
    }

    public void setFileComplexityDistribution(final Map<Integer, Integer> fileComplexityDistribution) {
        this.fileComplexityDistribution = fileComplexityDistribution;
    }

}
