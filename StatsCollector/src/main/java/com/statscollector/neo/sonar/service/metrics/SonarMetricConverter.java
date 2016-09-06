package com.statscollector.neo.sonar.service.metrics;

import java.util.List;

import com.statscollector.neo.sonar.model.Cell;
import com.statscollector.neo.sonar.model.SonarMetric;

/**
 * I convert raw metrics from sonar into SonarMetrics.
 *
 * @author JCannon
 *
 */
public interface SonarMetricConverter {

    /**
     * Convert the provided cell at provided index into a sonar metric with constructed name.
     *
     * @param cell
     * @param index
     * @return
     */
    public SonarMetric convert(final Cell cell, final int index);

    /**
     * Convert the rawValue in the SonarMetric into the actual value on the SonarMetric.
     *
     * @param sonarMetric
     */
    public void populate(final SonarMetric sonarMetric);

    public SonarMetric squash(List<SonarMetric> value);

}
