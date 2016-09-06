package com.statscollector.neo.sonar.service.metrics;

import java.util.ArrayList;
import java.util.List;

import com.statscollector.neo.sonar.model.SonarMetric;

/**
 * I convert a number metric returned from sonar e.g. 42
 *
 * @author JCannon
 *
 */
public class NumberConverter extends AbstractSonarMetricConverter {

    /**
     * Create Converter with provided name.
     *
     * @param name
     * @param key
     */
    public NumberConverter(final String name, final String key) {
        super(name, key);
    }

    @Override
    protected Integer convert(final String string) {
        try {
            return new Integer(string);
        } catch(NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public SonarMetric squash(final List<SonarMetric> values) {
        List<Integer> convertedValues = new ArrayList<>();
        for(SonarMetric sonarMetric : values) {
            Integer convertedValue;
            if(null != sonarMetric.getValue()) {
                convertedValue = (Integer) sonarMetric.getValue();
            } else {
                convertedValue = convert(sonarMetric.getRawValue());
            }
            convertedValues.add(convertedValue);
        }
        Integer result = 0;
        for(Integer integer : convertedValues) {
            result += integer;
        }
        SonarMetric infoMetric = values.get(0);
        return SonarMetric.builder().key(infoMetric.getKey()).name(infoMetric.getName()).rawValue(result.toString())
                .value(result).build();
    }

}
