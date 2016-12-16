package com.statscollector.targets.model;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import lombok.Data;

@Data
@Entity
public class SonarDisplayParameters {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Version
    private long version;

    @Column(unique = true)
    private String projectName;
    private String sonarRegex;
    private Integer sonarHistoryLength;
    @OneToMany(cascade = CascadeType.ALL)
    private Map<String, SonarTargetSettings> sonarTargetParam;
    private String defaultMetric1;
    private String defaultMetric2;
    private String defaultMetric3;
    private String defaultMetric4;
    private String defaultExtraMetric1;
    private String defaultExtraMetric2;
    private String defaultExtraMetric3;
}
