package com.statscollector.neo.sonar.model;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * I contain all sonar metrics for a period defined as a single month.
 *
 * @author JCannon
 *
 */
@Entity
@Data
public class SonarMetricPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    @Version
    @JsonIgnore
    private long version;
    private YearMonth period;
    @JsonIgnore
    private LocalDateTime actualDateTime;
    @OneToMany(cascade = { CascadeType.ALL, CascadeType.REMOVE }, orphanRemoval = true)
    @JoinColumn(nullable = true)
    private List<SonarMetric> sonarMetrics = new ArrayList<>();
    @Transient
    private List<SonarMetric> derivedMetrics = new ArrayList<>();

    public SonarMetricPeriod() {
        // For Hibernate
    }

    public SonarMetricPeriod(final YearMonth yearMonth) {
        period = yearMonth;
    }
}
