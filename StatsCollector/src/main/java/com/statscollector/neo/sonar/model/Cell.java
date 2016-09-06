package com.statscollector.neo.sonar.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Generated("org.jsonschema2pojo")
@Data
public class Cell {

    @Expose
    private String d;
    @Expose
    private final List<String> v = new ArrayList<>();
    private LocalDateTime convertedDate;

}
