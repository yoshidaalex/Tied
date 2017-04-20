package com.tied.android.tiedapp.objects.schedule;

import java.io.Serializable;

/**
 * Created by Emmanuel on 7/7/2016.
 */
public class ScheduleDate implements Serializable{

    private TimeRange time_range;
    private DateRange date_range;

    public ScheduleDate() {
    }

    public ScheduleDate(TimeRange time_range, DateRange date_range) {
        this.time_range = time_range;
        this.date_range = date_range;
    }

    public TimeRange getTime_range() {
        return time_range;
    }

    public void setTime_range(TimeRange time_range) {
        this.time_range = time_range;
    }

    public DateRange getDate_range() {
        return date_range;
    }

    public void setDate_range(DateRange date_range) {
        this.date_range = date_range;
    }

    @Override
    public String toString() {
        return "ScheduleDate{" +
                "time_range=" + time_range +
                ", date_range=" + date_range +
                '}';
    }
}
