package com.tied.android.tiedapp.objects.visit;

import java.io.Serializable;

/**
 * Created by Emmanuel on 7/7/2016.
 */
public class Duration implements Serializable {
    private int hours;
    private int minutes;

    public Duration(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    @Override
    public String toString() {
        return "Duration{" +
                "hours='" + hours +
                ", minutes='" + minutes +
                '}';
    }
}
