package com.statscollector.sonar.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Cell {

    @Expose
    private String d;
    @Expose
    private List<String> v = new ArrayList<String>();

    /**
     *
     * @return The d
     */
    public String getD() {
        return d;
    }

    /**
     *
     * @param d
     *            The d
     */
    public void setD(final String d) {
        this.d = d;
    }

    /**
     *
     * @return The v
     */
    public List<String> getV() {
        return v;
    }

    /**
     *
     * @param v
     *            The v
     */
    public void setV(final List<String> v) {
        this.v = v;
    }

}
