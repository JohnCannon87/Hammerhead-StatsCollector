package com.statscollector.neo.sonar.service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.statscollector.neo.sonar.model.SonarMetric;
import com.statscollector.neo.sonar.model.SonarMetricPeriod;
import com.statscollector.neo.sonar.service.metrics.SonarMetricConverterService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SonarSquashingMetricsService {

    @Autowired
    private SonarMetricConverterService sonarMetricConverterService;

    @Async
    public Future<SonarMetricPeriod> squashMetrics(final Entry<YearMonth, List<SonarMetricPeriod>> yearMonthEntry) {
        List<SonarMetricPeriod> sonarMetricPeriods = yearMonthEntry.getValue();
        YearMonth yearMonth = yearMonthEntry.getKey();
        Map<String, List<SonarMetric>> sonarMetricsByType = new HashMap<>();
        for(SonarMetricPeriod sonarMetricPeriod : sonarMetricPeriods) {
            List<SonarMetric> sonarMetrics = sonarMetricPeriod.getSonarMetrics();
            for(SonarMetric sonarMetric : sonarMetrics) {
                List<SonarMetric> list = sonarMetricsByType.get(sonarMetric.getKey());
                if(null == list) {
                    list = new ArrayList<>();
                    sonarMetricsByType.put(sonarMetric.getKey(), list);
                }
                list.add(sonarMetric);
            }
        }
        SonarMetricPeriod result = new SonarMetricPeriod(yearMonth);
        for(Entry<String, List<SonarMetric>> entry : sonarMetricsByType.entrySet()) {
            SonarMetric sonarMetric = sonarMetricConverterService.getConverterForKey(entry.getKey())
                    .squash(entry.getValue());
            result.getSonarMetrics().add(sonarMetric);
        }
        return new AsyncResult<>(result);
    }
}
