package com.statscollector.neo.sonar.service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.statscollector.neo.sonar.model.Cell;
import com.statscollector.neo.sonar.model.Col;
import com.statscollector.neo.sonar.model.RawSonarMetrics;
import com.statscollector.neo.sonar.model.SonarMetric;
import com.statscollector.neo.sonar.model.SonarMetricPeriod;
import com.statscollector.neo.sonar.model.SonarProject;
import com.statscollector.neo.sonar.service.metrics.SonarMetricConverterService;

@Service
public class SonarMetricService {

    // For pattern, 2015-07-01T14:16:41+0100
    // private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
    // .appendPattern("YYYY-MM-ddTHH:mm:ssZ").toFormatter();

    @Autowired
    private SonarMetricConverterService sonarMetricConverterService;

    @Autowired
    private DerivedSonarMetricService derivedSonarMetricService;

    /**
     * Convert the provided rawSonarValues and add them to the provided sonar project if that period is missing or a
     * more recent result from that period is provided.
     *
     * @param rawSonarMetrics
     * @param sonarProject
     */
    public void convertRawSonarValues(final RawSonarMetrics rawSonarMetrics, final SonarProject sonarProject) {

        List<Cell> metricCells = rawSonarMetrics.getCells();

        Map<YearMonth, List<Cell>> uncollapsedRawMetrics = convertIntoUncollapsedRawMetrics(metricCells);

        Map<YearMonth, Cell> collapsedRawMetrics = convertIntoCollapsedRawMetrics(uncollapsedRawMetrics);

        Map<YearMonth, SonarMetricPeriod> sonarMetricPeriods = sonarProject.getSonarMetricPeriods();
        Set<YearMonth> collapsedRawMetricsKeys = collapsedRawMetrics.keySet();
        for(YearMonth yearMonth : collapsedRawMetricsKeys) {
            Cell cell = collapsedRawMetrics.get(yearMonth);
            SonarMetricPeriod sonarMetricPeriod = sonarMetricPeriods.get(yearMonth);
            if(periodIsNullOrBeforeNewCellTime(sonarMetricPeriod, cell)) {
                sonarMetricPeriod = new SonarMetricPeriod(yearMonth);
                sonarMetricPeriod.setActualDateTime(cell.getConvertedDate());
                sonarMetricPeriod.setSonarMetrics(createSonarMetrics(cell, rawSonarMetrics.getCols()));
            }
            sonarProject.putMetricPeriod(yearMonth, sonarMetricPeriod);
        }
    }

    public void populateActualValuesForSonarProjects(final List<SonarProject> sonarProjects) {
        for(SonarProject sonarProject : sonarProjects) {
            Map<YearMonth, SonarMetricPeriod> sonarMetricPeriods = sonarProject.getSonarMetricPeriods();
            Collection<SonarMetricPeriod> values = sonarMetricPeriods.values();
            for(SonarMetricPeriod sonarMetricPeriod : values) {
                List<SonarMetric> sonarMetrics = sonarMetricPeriod.getSonarMetrics();
                for(SonarMetric sonarMetric : sonarMetrics) {
                    sonarMetricConverterService.getConverterForKey(sonarMetric.getKey()).populate(sonarMetric);
                }
            }
        }
    }

    /**
     * @param uncollapsedRawMetrics
     * @return
     */
    private Map<YearMonth, Cell> convertIntoCollapsedRawMetrics(
            final Map<YearMonth, List<Cell>> uncollapsedRawMetrics) {
        Map<YearMonth, Cell> collapsedRawMetrics = new HashMap<>();
        Set<YearMonth> uncollapsedRawMetricsKeys = uncollapsedRawMetrics.keySet();
        for(YearMonth yearMonth : uncollapsedRawMetricsKeys) {
            List<Cell> uncollapsedRawMetricsList = uncollapsedRawMetrics.get(yearMonth);
            Collections.sort(uncollapsedRawMetricsList,
                    (o1, o2) -> o2.getConvertedDate().compareTo(o1.getConvertedDate()));
            collapsedRawMetrics.put(yearMonth, uncollapsedRawMetricsList.get(0));
        }
        return collapsedRawMetrics;
    }

    private boolean periodIsNullOrBeforeNewCellTime(final SonarMetricPeriod sonarMetricPeriod, final Cell cell) {
        return null == sonarMetricPeriod || sonarMetricPeriod.getActualDateTime().isBefore(cell.getConvertedDate());
    }

    /**
     * @param metricCells
     * @return
     */
    private Map<YearMonth, List<Cell>> convertIntoUncollapsedRawMetrics(final List<Cell> metricCells) {
        Map<YearMonth, List<Cell>> uncollapsedRawMetrics = new HashMap<>();
        for(Cell cell : metricCells) {
            LocalDateTime metricDate = getMetricDate(cell.getD());
            cell.setConvertedDate(metricDate);
            YearMonth metricYearMonth = YearMonth.from(metricDate);
            List<Cell> uncollapsedRawMetricsList = uncollapsedRawMetrics.get(metricYearMonth);
            if(null == uncollapsedRawMetricsList) {
                uncollapsedRawMetricsList = new ArrayList<>();
                uncollapsedRawMetrics.put(metricYearMonth, uncollapsedRawMetricsList);
            }
            uncollapsedRawMetricsList.add(cell);
        }
        return uncollapsedRawMetrics;
    }

    private LocalDateTime getMetricDate(final String d) {
        return LocalDateTime.parse(d.substring(0, 19));
    }

    private List<SonarMetric> createSonarMetrics(final Cell cell, final List<Col> colList) {
        List<SonarMetric> result = new ArrayList<>();
        for(Col metricTitle : colList) {
            String metricString = metricTitle.getMetric();
            int index = colList.indexOf(metricTitle);
            SonarMetric sonarMetric = sonarMetricConverterService.getConverterForKey(metricString).convert(cell, index);
            result.add(sonarMetric);
        }
        return result;
    }

    public SonarProject squashProjectMetrics(final List<SonarProject> sonarProjects) {
        // Create full list of all potential SonarMetricPeriods
        Set<YearMonth> allYearMonths = new HashSet<>();
        for(SonarProject sonarProject : sonarProjects) {
            Map<YearMonth, SonarMetricPeriod> squashableSonarMetricPeriods = sonarProject.getSonarMetricPeriods();
            allYearMonths.addAll(squashableSonarMetricPeriods.keySet());
        }
        Map<YearMonth, List<SonarMetricPeriod>> sonarMetricPeriods = new HashMap<>();
        for(YearMonth yearMonth : allYearMonths) {
            sonarMetricPeriods.put(yearMonth, new ArrayList<SonarMetricPeriod>());
        }

        // Populate sonarMetricPeriods from sonar projects
        for(SonarProject sonarProject : sonarProjects) {
            Set<Entry<YearMonth, SonarMetricPeriod>> entrySet = sonarProject.getSonarMetricPeriods().entrySet();
            for(Entry<YearMonth, SonarMetricPeriod> entry : entrySet) {
                sonarMetricPeriods.get(entry.getKey()).add(entry.getValue());
            }
        }

        Map<YearMonth, SonarMetricPeriod> squashedSonarMetricPeriods = new HashMap<>();
        for(Entry<YearMonth, List<SonarMetricPeriod>> entry : sonarMetricPeriods.entrySet()) {
            squashedSonarMetricPeriods.put(entry.getKey(), squashMetrics(entry.getValue(), entry.getKey()));
        }

        SonarProject result = new SonarProject();
        result.setKey("squished");
        result.setName("Squashed");
        result.setSonarMetricPeriods(squashedSonarMetricPeriods);
        return result;
    }

    private SonarMetricPeriod squashMetrics(final List<SonarMetricPeriod> value, final YearMonth yearMonth) {
        Map<String, List<SonarMetric>> sonarMetricsByType = new HashMap<>();
        for(SonarMetricPeriod sonarMetricPeriod : value) {
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

        return result;
    }

    /**
     * I use the metrics on the provided period to produce a list of derived metrics e.g. Average Method Complexity.
     *
     * @param value
     * @return
     */
    public List<SonarMetric> deriveMetrics(final SonarMetricPeriod value) {
        List<SonarMetric> result = new ArrayList<>();
        Map<String, SonarMetric> mapOfMetrics = buildMapOfMetrics(value);
        result.add(derivedSonarMetricService.calculateAverageFileComplexity(mapOfMetrics));
        result.add(derivedSonarMetricService.calculateAverageMethodComplexity(mapOfMetrics));
        result.add(derivedSonarMetricService.calculateTestCoverage(mapOfMetrics));
        result.add(derivedSonarMetricService.calculateRulesCompliance(mapOfMetrics));
        return result;
    }

    private Map<String, SonarMetric> buildMapOfMetrics(final SonarMetricPeriod value) {
        Map<String, SonarMetric> result = new HashMap<>();
        List<SonarMetric> sonarMetrics = value.getSonarMetrics();
        for(SonarMetric sonarMetric : sonarMetrics) {
            result.put(sonarMetric.getKey(), sonarMetric);
        }
        return result;
    }

}
