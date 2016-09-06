package com.statscollector.neo.sonar.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Generated("org.jsonschema2pojo")
@Data
public class RawSonarMetrics {

    @Expose
    private final List<Col> cols = new ArrayList<>();
    @Expose
    private final List<Cell> cells = new ArrayList<>();

}
