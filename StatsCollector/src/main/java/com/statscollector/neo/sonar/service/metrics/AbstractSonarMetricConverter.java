package com.statscollector.neo.sonar.service.metrics;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.statscollector.neo.sonar.model.Cell;
import com.statscollector.neo.sonar.model.SonarMetric;

/**
 * Abstract class to handle the conversion of cell data into Sonar Metrics.
 *
 * @author JCannon
 *
 */
public abstract class AbstractSonarMetricConverter implements SonarMetricConverter {

    private final String name;
    private final String key;

    /**
     * Create Converter
     */
    public AbstractSonarMetricConverter(final String name, final String key) {
        super();
        this.name = name;
        this.key = key;
    }

    /**
     * Convert the provided cell at provided index into a sonar metric with constructed name.
     *
     * @param cell
     * @param index
     * @return
     */
    public SonarMetric convert(final Cell cell, final int index) {
        if(cellIsNotNullAndHasAValue(cell)) {
            return SonarMetric.builder().name(name).key(key).value(convert(cell.getV().get(index)))
                    .rawValue(cell.getV().get(index))
                    .build();
        } else {
            return SonarMetric.builder().name(name).value(null).rawValue("").build();
        }
    }

    /**
     * Convert the rawValue in the SonarMetric into the actual value on the SonarMetric.
     *
     * @param sonarMetric
     */
    public void populate(final SonarMetric sonarMetric) {
        sonarMetric.setValue(convert(sonarMetric.getRawValue()));
    }

    /**
     * @param cell
     * @return
     */
    private boolean cellIsNotNullAndHasAValue(final Cell cell) {
        if(cell != null) {
            List<String> values = cell.getV();
            return !CollectionUtils.isEmpty(values);
        }
        return false;
    }

    protected abstract Object convert(final String string);
}
