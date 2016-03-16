package com.statscollector.sonar.config;

import org.apache.commons.configuration.ConfigurationException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.statscollector.application.config.AbstractWebConfig;
import com.statscollector.application.config.WebConfig;

@Component
public class SonarConfig extends AbstractWebConfig implements WebConfig {

    /**
     * KEYS
     */
    private static final String HOST_KEY = "sonar.host";
    private static final String HOST_PORT_KEY = "sonar.hostPort";
    private static final String CONFIG_FILE_PATH_KEY = "sonarConfigFile.path";
    private static final String DEFAULT_FILE_PATH = "SonarStatistics.properties";
    private static final String USERNAME_KEY = "sonar.username";
    private static final String PASSWORD_KEY = "sonar.password";
    private static final String PROJECT_REGEX_KEY = "sonar.projectRegex";
    private static final String METHOD_COMPLEXITY_TARGET = "sonar.methodComplexityTarget";
    private static final String FILE_COMPLEXITY_TARGET = "sonar.fileComplexityTarget";
    private static final String TEST_COVERAGE_TARGET = "sonar.testCoverageTarget";
    private static final String RULES_COMPLIANCE_TARGET = "sonar.rulesComplianceTarget";
    private static final String BLOCKER_WEIGHTING = "sonar.blockerWeighting";
    private static final String CRITICAL_WEIGHTING = "sonar.criticalWeighting";
    private static final String MAJOR_WEIGHTING = "sonar.majorWeighting";
    private static final String MINOR_WEIGHTING = "sonar.minorWeighting";
    private static final String INFO_WEIGHTING = "sonar.infoWeighting";
    private static final String FILL_TARGET_AREA = "sonar.fillTargetArea";

    public SonarConfig() throws ConfigurationException {
        super();
    }

    @Override
    protected String getHostKey() {
        return HOST_KEY;
    }

    @Override
    protected String getHostPortKey() {
        return HOST_PORT_KEY;
    }

    @Override
    protected String getConfigFilePathKey() {
        return CONFIG_FILE_PATH_KEY;
    }

    @Override
    protected String getDefaultFilePath() {
        return DEFAULT_FILE_PATH;
    }

    @Override
    protected String getUsernameKey() {
        return USERNAME_KEY;
    }

    @Override
    protected String getPasswordKey() {
        return PASSWORD_KEY;
    }

    public String getProjectRegex() {
        return config.getString(PROJECT_REGEX_KEY);
    }

    public void setProjectRegex(final String projectRegex) throws ConfigurationException {
        config.setProperty(PROJECT_REGEX_KEY, projectRegex);
        config.save();
    }

    public String getMethodComplexityTarget() {
        return config.getString(METHOD_COMPLEXITY_TARGET);
    }

    public void setMethodComplexityTarget(final String methodComplexityTarget) throws ConfigurationException {
        config.setProperty(METHOD_COMPLEXITY_TARGET, methodComplexityTarget);
        config.save();
    }

    public String getFileComplexityTarget() {
        return config.getString(FILE_COMPLEXITY_TARGET);
    }

    public void setFileComplexityTarget(final String fileComplexityTarget) throws ConfigurationException {
        config.setProperty(FILE_COMPLEXITY_TARGET, fileComplexityTarget);
        config.save();
    }

    public String getTestCoverageTarget() {
        return config.getString(TEST_COVERAGE_TARGET);
    }

    public void setTestCoverageTarget(final String testCoverageTarget) throws ConfigurationException {
        config.setProperty(TEST_COVERAGE_TARGET, testCoverageTarget);
        config.save();
    }

    public String getRulesComplianceTarget() {
        return config.getString(RULES_COMPLIANCE_TARGET);
    }

    public void setRulesComplianceTarget(final String rulesComplianceTarget) throws ConfigurationException {
        config.setProperty(RULES_COMPLIANCE_TARGET, rulesComplianceTarget);
        config.save();
    }

    public String getProjectRegexKey() {
        return config.getString(PROJECT_REGEX_KEY);
    }

    public String getBlockerWeighting() {
        return config.getString(BLOCKER_WEIGHTING);
    }

    public void setBlockerWeighting(final String weighting) throws ConfigurationException {
        config.setProperty(BLOCKER_WEIGHTING, weighting);
        config.save();
    }

    public String getCriticalWeighting() {
        return config.getString(CRITICAL_WEIGHTING);
    }

    public void setCriticalWeighting(final String weighting) throws ConfigurationException {
        config.setProperty(CRITICAL_WEIGHTING, weighting);
        config.save();
    }

    public String getMajorWeighting() {
        return config.getString(MAJOR_WEIGHTING);
    }

    public void setMajorWeighting(final String weighting) throws ConfigurationException {
        config.setProperty(MAJOR_WEIGHTING, weighting);
        config.save();
    }

    public String getMinorWeighting() {
        return config.getString(MINOR_WEIGHTING);
    }

    public void setMinorWeighting(final String weighting) throws ConfigurationException {
        config.setProperty(MINOR_WEIGHTING, weighting);
        config.save();
    }

    public String getInfoWeighting() {
        return config.getString(INFO_WEIGHTING);
    }

    public void setInfoWeighting(final String weighting) throws ConfigurationException {
        config.setProperty(INFO_WEIGHTING, weighting);
        config.save();
    }

    public Boolean getFillTargetArea() {
        return config.getBoolean(FILL_TARGET_AREA, false);
    }

    public void setFillTargetArea(final Boolean fillTargetArea) throws ConfigurationException {
        config.setProperty(FILL_TARGET_AREA, fillTargetArea);
        config.save();
    }

    public void replaceWith(final TempSonarConfig newSonarConfig) {
        BeanUtils.copyProperties(newSonarConfig, this);
    }

}
