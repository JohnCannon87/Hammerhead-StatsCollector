package com.statscollector.neo.sonar.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;

/**
 * I hold the information for a single sonar metric, as well as the static keys for various metric names.
 *
 * @author JCannon
 *
 */
@Entity
@Data
@Builder
public class SonarMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    @Version
    @JsonIgnore
    private long version;
    private String name;
    private String key;
    @Transient
    private Object value;
    private String rawValue;

    public SonarMetric() {
        // For Hibernate
    }

    public SonarMetric(final Long id, final Long version, final String name, final String key, final Object value,
            final String rawValue) {
        super();
        this.id = id;
        this.version = version;
        this.name = name;
        this.key = key;
        this.value = value;
        this.rawValue = rawValue;
    }

}
