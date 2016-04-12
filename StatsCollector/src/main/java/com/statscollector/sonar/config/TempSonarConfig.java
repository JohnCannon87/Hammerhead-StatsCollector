package com.statscollector.sonar.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.statscollector.application.config.AbstractTemporaryWebConfig;

@JsonPropertyOrder({ "projectName", "host", "hostPort", "username", "password", "projectRegex",
        "methodComplexityTarget", "fileComplexityTarget", "rulesComplianceTarget", "testCoverageTarget",
        "infoWeighting", "minorWeighting", "majorWeighting", "criticalWeighting", "blockerWeighting", "fillTargetArea" })
public class TempSonarConfig extends AbstractTemporaryWebConfig {

    private String projectRegex, methodComplexityTarget, fileComplexityTarget, rulesComplianceTarget,
            testCoverageTarget, infoWeighting, minorWeighting, majorWeighting, criticalWeighting, blockerWeighting;
    private Boolean fillTargetArea;

    public String getProjectRegex() {
        return projectRegex;
    }

    public void setProjectRegex(final String projectRegex) {
        this.projectRegex = projectRegex;
    }

    @JsonProperty(required = true)
    public String getMethodComplexityTarget() {
        return methodComplexityTarget;
    }

    public void setMethodComplexityTarget(final String methodComplexityTarget) {
        this.methodComplexityTarget = methodComplexityTarget;
    }

    @JsonProperty(required = true)
    public String getFileComplexityTarget() {
        return fileComplexityTarget;
    }

    public void setFileComplexityTarget(final String fileComplexityTarget) {
        this.fileComplexityTarget = fileComplexityTarget;
    }

    @JsonProperty(required = true)
    public String getRulesComplianceTarget() {
        return rulesComplianceTarget;
    }

    public void setRulesComplianceTarget(final String rulesComplianceTarget) {
        this.rulesComplianceTarget = rulesComplianceTarget;
    }

    @JsonProperty(required = true)
    public String getTestCoverageTarget() {
        return testCoverageTarget;
    }

    public void setTestCoverageTarget(final String testCoverageTarget) {
        this.testCoverageTarget = testCoverageTarget;
    }

    @JsonProperty(required = true)
    public Boolean getFillTargetArea() {
        return fillTargetArea;
    }

    public void setFillTargetArea(final Boolean fillTargetArea) {
        this.fillTargetArea = fillTargetArea;
    }

    @JsonProperty(required = true)
    public String getInfoWeighting() {
        return infoWeighting;
    }

    public void setInfoWeighting(final String infoWeighting) {
        this.infoWeighting = infoWeighting;
    }

    @JsonProperty(required = true)
    public String getMinorWeighting() {
        return minorWeighting;
    }

    public void setMinorWeighting(final String minorWeighting) {
        this.minorWeighting = minorWeighting;
    }

    @JsonProperty(required = true)
    public String getMajorWeighting() {
        return majorWeighting;
    }

    public void setMajorWeighting(final String majorWeighting) {
        this.majorWeighting = majorWeighting;
    }

    @JsonProperty(required = true)
    public String getCriticalWeighting() {
        return criticalWeighting;
    }

    public void setCriticalWeighting(final String criticalWeighting) {
        this.criticalWeighting = criticalWeighting;
    }

    @JsonProperty(required = true)
    public String getBlockerWeighting() {
        return blockerWeighting;
    }

    public void setBlockerWeighting(final String blockerWeighting) {
        this.blockerWeighting = blockerWeighting;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((blockerWeighting == null) ? 0 : blockerWeighting.hashCode());
        result = prime * result + ((criticalWeighting == null) ? 0 : criticalWeighting.hashCode());
        result = prime * result + ((fileComplexityTarget == null) ? 0 : fileComplexityTarget.hashCode());
        result = prime * result + ((fillTargetArea == null) ? 0 : fillTargetArea.hashCode());
        result = prime * result + ((infoWeighting == null) ? 0 : infoWeighting.hashCode());
        result = prime * result + ((majorWeighting == null) ? 0 : majorWeighting.hashCode());
        result = prime * result + ((methodComplexityTarget == null) ? 0 : methodComplexityTarget.hashCode());
        result = prime * result + ((minorWeighting == null) ? 0 : minorWeighting.hashCode());
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
        if(blockerWeighting == null) {
            if(other.blockerWeighting != null) {
                return false;
            }
        } else if(!blockerWeighting.equals(other.blockerWeighting)) {
            return false;
        }
        if(criticalWeighting == null) {
            if(other.criticalWeighting != null) {
                return false;
            }
        } else if(!criticalWeighting.equals(other.criticalWeighting)) {
            return false;
        }
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
        if(infoWeighting == null) {
            if(other.infoWeighting != null) {
                return false;
            }
        } else if(!infoWeighting.equals(other.infoWeighting)) {
            return false;
        }
        if(majorWeighting == null) {
            if(other.majorWeighting != null) {
                return false;
            }
        } else if(!majorWeighting.equals(other.majorWeighting)) {
            return false;
        }
        if(methodComplexityTarget == null) {
            if(other.methodComplexityTarget != null) {
                return false;
            }
        } else if(!methodComplexityTarget.equals(other.methodComplexityTarget)) {
            return false;
        }
        if(minorWeighting == null) {
            if(other.minorWeighting != null) {
                return false;
            }
        } else if(!minorWeighting.equals(other.minorWeighting)) {
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
                + ", testCoverageTarget=" + testCoverageTarget + ", infoWeighting=" + infoWeighting
                + ", minorWeighting=" + minorWeighting + ", majorWeighting=" + majorWeighting + ", criticalWeighting="
                + criticalWeighting + ", blockerWeighting=" + blockerWeighting + ", fillTargetArea=" + fillTargetArea
                + "]";
    }

}
