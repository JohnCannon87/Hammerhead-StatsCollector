package com.statscollector.targets.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import lombok.Data;

@Data
@Entity
public class GerritDisplayParameters {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Version
    private long version;

    @Column(unique = true)
    private String projectName;
    private String gerritRegex;
    private String gerritFilterOutRegex;
    private String topicRegex;

}
