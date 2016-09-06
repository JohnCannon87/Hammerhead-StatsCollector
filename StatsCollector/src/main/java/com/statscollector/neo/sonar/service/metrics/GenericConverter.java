package com.statscollector.neo.sonar.service.metrics;

import java.util.List;

import com.statscollector.neo.sonar.model.SonarMetric;

/**
 * I am a fallback converter that simply returns the passed in string.
 *
 * @author JCannon
 *
 */
public class GenericConverter extends AbstractSonarMetricConverter {

    /**
     * Create Converter with provided name.
     *
     * @param name
     * @param key
     */
    public GenericConverter(final String name, final String key) {
        super(name, key);
    }

    @Override
    protected String convert(final String string) {
        return new String(string);
    }

    @Override
    public SonarMetric squash(final List<SonarMetric> values) {
        StringBuilder rawValueBuilder = new StringBuilder();
        for(SonarMetric sonarMetric : values) {
            rawValueBuilder.append(sonarMetric.getRawValue());
        }
        String rawValueString = rawValueBuilder.toString();
        SonarMetric infoMetric = values.get(0);
        return SonarMetric.builder().key(infoMetric.getKey()).name(infoMetric.getName()).rawValue(rawValueString)
                .value(rawValueString).build();
    }

}
