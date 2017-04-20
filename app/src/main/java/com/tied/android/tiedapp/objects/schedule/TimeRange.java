package com.tied.android.tiedapp.objects.schedule;

import java.io.Serializable;

/**
 * Created by Emmanuel on 7/7/2016.
 */
public class TimeRange implements Serializable{
    private String start_time;
    private String end_time;

    public TimeRange(String start_time, String end_time) {
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getRange(){
        return start_time +"-"+end_time;
    }

    @Override
    public String toString() {
        return "TimeRange{" +
                "start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                '}';
    }
}
