package com.tied.android.tiedapp.objects;

import com.google.gson.Gson;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.visit.Duration;

import java.io.Serializable;

/**
 * Created by Femi on 7/22/2016.
 */
public class ReportFilter implements Serializable {

    public static final String TAG = ReportFilter.class.getSimpleName();

    private int year;
    private int month;
    private String type;
    private String content;

    public ReportFilter() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}


