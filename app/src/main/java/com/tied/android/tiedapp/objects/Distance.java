package com.tied.android.tiedapp.objects;

/**
 * Created by Emmanuel on 7/15/2016.
 */
public class Distance {
    private String lower_bound;
    private String upper_bound;
    private String measurement;
    public static final String UNIT_MILES="m", UNIT_KILOMETERS="km";
    public Distance() {
    }

    public Distance(String lower_bound, String upper_bound, String measurement) {
        this.lower_bound = lower_bound;
        this.upper_bound = upper_bound;
        this.measurement = measurement;
    }

    public String getLower_bound() {
        return lower_bound;
    }

    public void setLower_bound(String lower_bound) {
        this.lower_bound = lower_bound;
    }

    public String getUpper_bound() {
        return upper_bound;
    }

    public void setUpper_bound(String upper_bound) {
        this.upper_bound = upper_bound;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    @Override
    public String toString() {
        return "Distance{" +
                "lower_bound='" + lower_bound + '\'' +
                ", upper_bound='" + upper_bound + '\'' +
                ", measurement='" + measurement + '\'' +
                '}';
    }
}
