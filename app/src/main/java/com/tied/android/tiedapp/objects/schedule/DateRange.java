package com.tied.android.tiedapp.objects.schedule;

import java.io.Serializable;

/**
 * Created by Emmanuel on 7/7/2016.
 */
public class DateRange implements Serializable {
    private String start_date;
    private String end_date;

    public DateRange(String start_date, String end_date) {
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    @Override
    public String toString() {
        return "DateRange{" +
                "start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                '}';
    }
}
