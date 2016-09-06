package com.statscollector.neo.sonar.model;

public class SonarTargets {

    private final String sonarMethodTarget, sonarFileTarget, sonarRulesTarget, sonarTestTarget;

    public SonarTargets(final String sonarMethodTarget, final String sonarFileTarget, final String sonarRulesTarget,
            final String sonarTestTarget) {
        super();
        this.sonarMethodTarget = sonarMethodTarget;
        this.sonarFileTarget = sonarFileTarget;
        this.sonarRulesTarget = sonarRulesTarget;
        this.sonarTestTarget = sonarTestTarget;
    }

    public String getSonarMethodTarget() {
        return sonarMethodTarget;
    }

    public String getSonarFileTarget() {
        return sonarFileTarget;
    }

    public String getSonarRulesTarget() {
        return sonarRulesTarget;
    }

    public String getSonarTestTarget() {
        return sonarTestTarget;
    }

}
