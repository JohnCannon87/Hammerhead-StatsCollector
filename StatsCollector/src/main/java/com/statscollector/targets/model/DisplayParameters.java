package com.statscollector.targets.model;

import lombok.Data;

@Data
public class DisplayParameters {

    private final GerritDisplayParameters gerritDisplayParameters;
    private final SonarDisplayParameters sonarDisplayParameters;

}
