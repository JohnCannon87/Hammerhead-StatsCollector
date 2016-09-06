package com.statscollector.neo.sonar.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.statscollector.neo.sonar.config.SonarConfig;
import com.statscollector.neo.sonar.model.SonarMetric;
import com.statscollector.neo.sonar.service.metrics.SonarMetricConverterService;

@Service
public class DerivedSonarMetricService {

    @Autowired
    private SonarConfig sonarConfig;

    private static final String AVERAGE_METHOD_COMPLEXITY_NAME = "Average Method Complexity";
    private static final String AVERAGE_METHOD_COMPLEXITY_KEY = "average_method_complexity";
    private static final String AVERAGE_FILE_COMPLEXITY_NAME = "Average File Complexity";
    private static final String AVERAGE_FILE_COMPLEXITY_KEY = "average_file_complexity";
    private static final String TEST_COVERAGE_KEY = "test_coverage";
    private static final String TEST_COVERAGE_NAME = "Test Coverage";
    private static final String RULES_COMPLIANCE_KEY = "rules_compliance";
    private static final String RULES_COMPLIANCE_NAME = "Rules Coverage";
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    /**
     * Calculate the average file complexity based on the passed in metrics.
     *
     * @param mapOfMetrics
     * @return
     */
    public SonarMetric calculateAverageFileComplexity(final Map<String, SonarMetric> mapOfMetrics) {
        SonarMetric numberOfFiles = mapOfMetrics.get(SonarMetricConverterService.FILES_KEY);
        SonarMetric complexity = mapOfMetrics.get(SonarMetricConverterService.COMPLEXITY_KEY);
        BigDecimal numberOfFilesValue = new BigDecimal(numberOfFiles.getRawValue());
        BigDecimal complexityValue = new BigDecimal(complexity.getRawValue());
        BigDecimal averageMethodComplexityValue = complexityValue.setScale(2)
                .divide(numberOfFilesValue.setScale(2), RoundingMode.HALF_UP)
                .setScale(2);
        return SonarMetric.builder().key(AVERAGE_FILE_COMPLEXITY_KEY).name(AVERAGE_FILE_COMPLEXITY_NAME)
                .value(averageMethodComplexityValue).rawValue(averageMethodComplexityValue.toString()).build();
    }

    /**
     * Calculate the average method complexity based on the passed in metrics.
     *
     * @param mapOfMetrics
     * @return
     */
    public SonarMetric calculateAverageMethodComplexity(final Map<String, SonarMetric> mapOfMetrics) {
        SonarMetric numberOfMethods = mapOfMetrics.get(SonarMetricConverterService.METHODS_KEY);
        SonarMetric complexity = mapOfMetrics.get(SonarMetricConverterService.COMPLEXITY_KEY);
        BigDecimal numberOfMethodsValue = new BigDecimal(numberOfMethods.getRawValue());
        BigDecimal complexityValue = new BigDecimal(complexity.getRawValue());
        BigDecimal averageMethodComplexityValue = complexityValue.setScale(2)
                .divide(numberOfMethodsValue.setScale(2), RoundingMode.HALF_UP)
                .setScale(2);
        return SonarMetric.builder().key(AVERAGE_METHOD_COMPLEXITY_KEY).name(AVERAGE_METHOD_COMPLEXITY_NAME)
                .value(averageMethodComplexityValue).rawValue(averageMethodComplexityValue.toString()).build();
    }

    /**
     * Calculate the test coverage based on the passed in metrics.
     *
     * @param mapOfMetrics
     * @return
     */
    public SonarMetric calculateTestCoverage(final Map<String, SonarMetric> mapOfMetrics) {
        SonarMetric linesToCover = mapOfMetrics.get(SonarMetricConverterService.LINES_TO_COVER_KEY);
        SonarMetric uncoveredLines = mapOfMetrics.get(SonarMetricConverterService.UNCOVERED_LINES_KEY);
        BigDecimal linesToCoverValue = new BigDecimal(linesToCover.getRawValue());
        BigDecimal uncoveredLinesValue = new BigDecimal(uncoveredLines.getRawValue());
        BigDecimal linesCoveredValue = linesToCoverValue.subtract(uncoveredLinesValue);
        BigDecimal linesCoveredDecimal = linesCoveredValue.setScale(4)
                .divide(linesToCoverValue.setScale(4), RoundingMode.HALF_UP).setScale(4);
        BigDecimal linesCoveredPercentage = linesCoveredDecimal.multiply(ONE_HUNDRED).setScale(2);
        return SonarMetric.builder().key(TEST_COVERAGE_KEY).name(TEST_COVERAGE_NAME)
                .value(linesCoveredPercentage).rawValue(linesCoveredPercentage.toString()).build();
    }

    /**
     * Calculate the rules compliance based on the passed in metrics.
     *
     * @param mapOfMetrics
     * @return
     */
    public SonarMetric calculateRulesCompliance(final Map<String, SonarMetric> mapOfMetrics) {
        SonarMetric blockerViolations = mapOfMetrics.get(SonarMetricConverterService.BLOCKER_VIOLATIONS_KEY);
        SonarMetric criticalViolations = mapOfMetrics.get(SonarMetricConverterService.CRITICAL_VIOLATIONS_KEY);
        SonarMetric majorViolations = mapOfMetrics.get(SonarMetricConverterService.MAJOR_VIOLATIONS_KEY);
        SonarMetric minorViolations = mapOfMetrics.get(SonarMetricConverterService.MINOR_VIOLATIONS_KEY);
        SonarMetric infoViolations = mapOfMetrics.get(SonarMetricConverterService.INFO_VIOLATIONS_KEY);
        SonarMetric linesOfCode = mapOfMetrics.get(SonarMetricConverterService.LINES_OF_CODE_KEY);
        BigDecimal blockerViolationsWeightedValue = new BigDecimal(blockerViolations.getRawValue())
                .multiply(new BigDecimal(sonarConfig.getBlockerWeighting()));
        BigDecimal criticalViolationsWeightedValue = new BigDecimal(criticalViolations.getRawValue())
                .multiply(new BigDecimal(sonarConfig.getCriticalWeighting()));
        BigDecimal majorViolationsWeightedValue = new BigDecimal(majorViolations.getRawValue())
                .multiply(new BigDecimal(sonarConfig.getMajorWeighting()));
        BigDecimal minorViolationsWeightedValue = new BigDecimal(minorViolations.getRawValue())
                .multiply(new BigDecimal(sonarConfig.getMinorWeighting()));
        BigDecimal infoViolationsWeightedValue = new BigDecimal(infoViolations.getRawValue())
                .multiply(new BigDecimal(sonarConfig.getInfoWeighting()));
        BigDecimal linesOfCodeValue = new BigDecimal(linesOfCode.getRawValue());
        BigDecimal totalWeightedIssues = blockerViolationsWeightedValue.add(criticalViolationsWeightedValue)
                .add(majorViolationsWeightedValue).add(minorViolationsWeightedValue)
                .add(infoViolationsWeightedValue);
        BigDecimal rulesViolationDecimal = totalWeightedIssues.setScale(4)
                .divide(linesOfCodeValue.setScale(4), RoundingMode.HALF_UP).setScale(4);
        BigDecimal rulesViolationPercentage = ONE_HUNDRED.subtract(rulesViolationDecimal.multiply(ONE_HUNDRED))
                .setScale(2);
        return SonarMetric.builder().key(RULES_COMPLIANCE_KEY).name(RULES_COMPLIANCE_NAME)
                .value(rulesViolationPercentage).rawValue(rulesViolationPercentage.toString()).build();
    }

}
