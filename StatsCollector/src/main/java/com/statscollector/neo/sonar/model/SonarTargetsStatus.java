package com.statscollector.neo.sonar.model;

public class SonarTargetsStatus {

    private final boolean hitFileTarget, hitMethodTarget, hitTestTarget, hitRulesTarget;

    public SonarTargetsStatus(final boolean hitFileTarget, final boolean hitMethodTarget, final boolean hitTestTarget,
            final boolean hitRulesTarget) {
        super();
        this.hitFileTarget = hitFileTarget;
        this.hitMethodTarget = hitMethodTarget;
        this.hitTestTarget = hitTestTarget;
        this.hitRulesTarget = hitRulesTarget;
    }

    public boolean isHitFileTarget() {
        return hitFileTarget;
    }

    public boolean isHitMethodTarget() {
        return hitMethodTarget;
    }

    public boolean isHitTestTarget() {
        return hitTestTarget;
    }

    public boolean isHitRulesTarget() {
        return hitRulesTarget;
    }

}
