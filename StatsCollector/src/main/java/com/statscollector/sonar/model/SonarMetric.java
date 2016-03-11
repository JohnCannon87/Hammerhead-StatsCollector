package com.statscollector.sonar.model;

import com.google.gson.annotations.SerializedName;

public class SonarMetric {

    public static final Object LINES_OF_CODE_KEY = "ncloc";
    public static final Object COMPLEXITY_KEY = "complexity";
    public static final Object FILES_KEY = "files";
    public static final Object METHODS_KEY = "functions";
    public static final Object BLOCKER_VIOLATIONS_KEY = "blocker_violations";
    public static final Object CRITICAL_VIOLATIONS_KEY = "critical_violations";
    public static final Object MAJOR_VIOLATIONS_KEY = "major_violations";
    public static final Object MINOR_VIOLATIONS_KEY = "minor_violations";
    public static final Object INFO_VIOLATIONS_KEY = "info_violations";
    public static final Object LINES_TO_COVER_KEY = "lines_to_cover";
    public static final Object UNCOVERED_LINES = "uncovered_lines";

    @SerializedName("key")
    private String key;
    @SerializedName("val")
    private String value;
    @SerializedName("frmt_val")
    private String formattedValue;

    public SonarMetric() {
    };

    public SonarMetric(final String key, final String value, final String formattedValue) {
        super();
        this.key = key;
        this.value = value;
        this.formattedValue = formattedValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getFormattedValue() {
        return formattedValue;
    }

    public void setFormattedValue(final String formattedValue) {
        this.formattedValue = formattedValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((formattedValue == null) ? 0 : formattedValue.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        SonarMetric other = (SonarMetric) obj;
        if(formattedValue == null) {
            if(other.formattedValue != null) {
                return false;
            }
        } else if(!formattedValue.equals(other.formattedValue)) {
            return false;
        }
        if(key == null) {
            if(other.key != null) {
                return false;
            }
        } else if(!key.equals(other.key)) {
            return false;
        }
        if(value == null) {
            if(other.value != null) {
                return false;
            }
        } else if(!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SonarMetric [key=" + key + ", value=" + value + ", formattedValue=" + formattedValue + "]";
    }

}
