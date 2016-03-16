package com.statscollector.sonar.config;

import com.statscollector.application.config.AbstractTemporaryWebConfig;

public class TempSonarConfig extends AbstractTemporaryWebConfig {

    private String projectRegex, methodComplexityTarget, fileComplexityTarget, rulesComplianceTarget,
    testCoverageTarget;
    private Boolean fillTargetArea;

    public String getProjectRegex() {
        return projectRegex;
    }

    public void setProjectRegex(final String projectRegex) {
        this.projectRegex = projectRegex;
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

    public Boolean getFillTargetArea() {
        return fillTargetArea;
    }

    public void setFillTargetArea(final Boolean fillTargetArea) {
        this.fillTargetArea = fillTargetArea;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fileComplexityTarget == null) ? 0 : fileComplexityTarget.hashCode());
        result = prime * result + ((fillTargetArea == null) ? 0 : fillTargetArea.hashCode());
        result = prime * result + ((methodComplexityTarget == null) ? 0 : methodComplexityTarget.hashCode());
        result = prime * result + ((projectRegex == null) ? 0 : projectRegex.hashCode());
        result = prime * result + ((rulesComplianceTarget == null) ? 0 : rulesComplianceTarget.hashCode());
        result = prime * result + ((testCoverageTarget == null) ? 0 : testCoverageTarget.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        TempSonarConfig other = (TempSonarConfig) obj;
        if(fileComplexityTarget == null) {
            if(other.fileComplexityTarget != null) {
                return false;
            }
        } else if(!fileComplexityTarget.equals(other.fileComplexityTarget)) {
            return false;
        }
        if(fillTargetArea == null) {
            if(other.fillTargetArea != null) {
                return false;
            }
        } else if(!fillTargetArea.equals(other.fillTargetArea)) {
            return false;
        }
        if(methodComplexityTarget == null) {
            if(other.methodComplexityTarget != null) {
                return false;
            }
        } else if(!methodComplexityTarget.equals(other.methodComplexityTarget)) {
            return false;
        }
        if(projectRegex == null) {
            if(other.projectRegex != null) {
                return false;
            }
        } else if(!projectRegex.equals(other.projectRegex)) {
            return false;
        }
        if(rulesComplianceTarget == null) {
            if(other.rulesComplianceTarget != null) {
                return false;
            }
        } else if(!rulesComplianceTarget.equals(other.rulesComplianceTarget)) {
            return false;
        }
        if(testCoverageTarget == null) {
            if(other.testCoverageTarget != null) {
                return false;
            }
        } else if(!testCoverageTarget.equals(other.testCoverageTarget)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TempSonarConfig [projectRegex=" + projectRegex + ", methodComplexityTarget=" + methodComplexityTarget
                + ", fileComplexityTarget=" + fileComplexityTarget + ", rulesComplianceTarget=" + rulesComplianceTarget
                + ", testCoverageTarget=" + testCoverageTarget + ", fillTargetArea=" + fillTargetArea + "]";
    }

}
