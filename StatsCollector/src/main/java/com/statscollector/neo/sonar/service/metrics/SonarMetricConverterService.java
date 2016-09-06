package com.statscollector.neo.sonar.service.metrics;

import java.util.Map;

public class SonarMetricConverterService {

    private final Map<String, SonarMetricConverter> sonarMetricConverters;

    public static final String LINES_OF_CODE_KEY = "ncloc";
    public static final String LINES_OF_CODE_NAME = "Lines Of Code";
    public static final String COMPLEXITY_KEY = "complexity";
    public static final String COMPLEXITY_NAME = "Complexity";
    public static final String FILES_KEY = "files";
    public static final String FILES_NAME = "Files Count";
    public static final String METHODS_KEY = "functions";
    public static final String METHODS_NAME = "Functions Count";
    public static final String BLOCKER_VIOLATIONS_KEY = "blocker_violations";
    public static final String BLOCKER_VIOLATIONS_NAME = "Blockers Count";
    public static final String CRITICAL_VIOLATIONS_KEY = "critical_violations";
    public static final String CRITICAL_VIOLATIONS_NAME = "Criticals Count";
    public static final String MAJOR_VIOLATIONS_KEY = "major_violations";
    public static final String MAJOR_VIOLATIONS_NAME = "Majors Count";
    public static final String MINOR_VIOLATIONS_KEY = "minor_violations";
    public static final String MINOR_VIOLATIONS_NAME = "Minors Count";
    public static final String INFO_VIOLATIONS_KEY = "info_violations";
    public static final String INFO_VIOLATIONS_NAME = "Infos Count";
    public static final String LINES_TO_COVER_KEY = "lines_to_cover";
    public static final String LINES_TO_COVER_NAME = "Lines Testable";
    public static final String UNCOVERED_LINES_KEY = "uncovered_lines";
    public static final String UNCOVERED_LINES_NAME = "Lines Untested";
    public static final String FUNCTION_COMPLEXITY_DISTRIBUTION_KEY = "function_complexity_distribution";
    public static final String FUNCTION_COMPLEXITY_DISTRIBUTION_NAME = "Function Complexity Distribution";
    public static final String FILE_COMPLEXITY_DISTRIBUTION_KEY = "file_complexity_distribution";
    public static final String FILE_COMPLEXITY_DISTRIBUTION_NAME = "File Complexity Distribution";
    public static final String DEFAULT_CONVERTER_KEY = "default";
    public static final String DEFAULT_CONVERTER_NAME = "Default Converter";

    public SonarMetricConverterService(final Map<String, SonarMetricConverter> sonarMetricConverters) {
        this.sonarMetricConverters = sonarMetricConverters;
    }

    public SonarMetricConverter getConverterForKey(final String metricString) {
        if(sonarMetricConverters.containsKey(metricString)) {
            return sonarMetricConverters.get(metricString);
        } else {
            return sonarMetricConverters.get(DEFAULT_CONVERTER_KEY);
        }
    }

}
