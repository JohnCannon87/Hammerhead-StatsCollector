package com.statscollector.neo.sonar.service;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.statscollector.neo.sonar.model.SonarMetric;
import com.statscollector.neo.sonar.model.SonarMetricPeriod;
import com.statscollector.neo.sonar.service.metrics.SonarMetricConverterService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SonarMetricPeriodConversionHelper {

    @Autowired
    private SonarMetricConverterService sonarMetricConverterService;

    /**
     * @param sonarMetricPeriod
     */
    @Async
    public Future<Boolean> convertPeriod(final SonarMetricPeriod sonarMetricPeriod) {
        List<SonarMetric> sonarMetrics = sonarMetricPeriod.getSonarMetrics();
        for(SonarMetric sonarMetric : sonarMetrics) {
            sonarMetricConverterService.getConverterForKey(sonarMetric.getKey()).populate(sonarMetric);
        }
        return new AsyncResult<>(true);
    }

}
