package com.statscollector.neo.sonar.model;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class RawSonarProject {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("k")
    @Expose
    private String key;
    @SerializedName("nm")
    @Expose
    private String name;
    @SerializedName("sc")
    @Expose
    private String scope;
    @SerializedName("qu")
    @Expose
    private String qualifier;

    /**
     *
     * @return
     *         The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     *            The id
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     *
     * @return
     *         The k
     */
    public String getKey() {
        return key;
    }

    /**
     *
     * @param k
     *            The k
     */
    public void setKey(final String k) {
        this.key = k;
    }

    /**
     *
     * @return
     *         The nm
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param nm
     *            The nm
     */
    public void setName(final String nm) {
        this.name = nm;
    }

    /**
     *
     * @return
     *         The sc
     */
    public String getScope() {
        return scope;
    }

    /**
     *
     * @param sc
     *            The sc
     */
    public void setScope(final String sc) {
        this.scope = sc;
    }

    /**
     *
     * @return
     *         The qu
     */
    public String getQualifier() {
        return qualifier;
    }

    /**
     *
     * @param qu
     *            The qu
     */
    public void setQualifier(final String qu) {
        this.qualifier = qu;
    }

}
