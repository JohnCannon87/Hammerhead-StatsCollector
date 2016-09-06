package com.statscollector.neo.sonar.service.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.statscollector.neo.sonar.model.SonarMetric;

/**
 * I convert based on a distribution object from sonar e.g. "1=20;2=5;4=2;6=1;8=1;10=0;12=0"
 *
 * @author JCannon
 *
 */
public class DistributionConverter extends AbstractSonarMetricConverter {

    private static final String SPLITTER = ";";
    private static final String EQUALS = "=";

    /**
     * Create Converter with provided name.
     *
     * @param name
     * @param key
     */
    public DistributionConverter(final String name, final String key) {
        super(name, key);
    }

    @Override
    protected Map<String, Integer> convert(final String string) {
        Map<String, Integer> result = new HashMap<>();
        if(!StringUtils.isEmpty(string)) {
            String[] values = string.split(SPLITTER);
            for(String value : values) {
                String[] keyValuePair = value.split(EQUALS);
                try {
                    result.put(keyValuePair[0], Integer.parseInt(keyValuePair[1]));
                } catch(NumberFormatException e) {
                    result.put(keyValuePair[0], 0);
                }

            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public SonarMetric squash(final List<SonarMetric> metrics) {
        List<Map<String, Integer>> convertedValues = new ArrayList<>();
        for(SonarMetric sonarMetric : metrics) {
            Map<String, Integer> convertedValue;
            if(null != sonarMetric.getValue()) {
                convertedValue = (Map<String, Integer>) sonarMetric.getValue();
            } else {
                convertedValue = convert(sonarMetric.getRawValue());
            }
            convertedValues.add(convertedValue);
        }
        Set<String> keys = buildKeyList(convertedValues);
        StringBuilder squashedRawValue = new StringBuilder();
        for(String key : keys) {
            squashedRawValue.append(key);
            squashedRawValue.append(EQUALS);
            squashedRawValue.append(valueSquash(convertedValues, key));
            squashedRawValue.append(SPLITTER);
        }
        SonarMetric infoMetric = metrics.get(0);
        String squashedRawString = squashedRawValue.toString();
        return SonarMetric.builder().key(infoMetric.getKey()).name(infoMetric.getName())
                .rawValue(squashedRawString).value(convert(squashedRawString)).build();
    }

    private Set<String> buildKeyList(final List<Map<String, Integer>> values) {
        Set<String> result = new HashSet<>();
        for(Map<String, Integer> map : values) {
            result.addAll(map.keySet());
        }
        return result;
    }

    private String valueSquash(final List<Map<String, Integer>> convertedValues, final String key) {
        List<Integer> values = new ArrayList<>();
        for(Map<String, Integer> map : convertedValues) {
            Integer integer = map.get(key);
            values.add(integer);
        }
        Integer result = 0;
        for(Integer integer : values) {
            if(null != integer) {
                result += integer;
            }
        }
        return result.toString();
    }

}
