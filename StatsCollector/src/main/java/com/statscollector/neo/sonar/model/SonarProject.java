package com.statscollector.neo.sonar.model;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * I contain the state of a sonar project over time.
 *
 * @author JCannon
 *
 */
@Entity
@Data
public class SonarProject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    @Version
    @JsonIgnore
    private long version;
    @Column(unique = true)
    private String key;
    private String name;
    @OneToMany(cascade = { CascadeType.ALL, CascadeType.REMOVE }, orphanRemoval = true)
    @JoinColumn(nullable = true)
    @MapKey(name = "period")
    private Map<YearMonth, SonarMetricPeriod> sonarMetricPeriods = new HashMap<>();

    public SonarMetricPeriod putMetricPeriod(final YearMonth key, final SonarMetricPeriod value) {
        return sonarMetricPeriods.put(key, value);
    }

}
