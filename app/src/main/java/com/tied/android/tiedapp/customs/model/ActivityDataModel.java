package com.tied.android.tiedapp.customs.model;

import java.util.ArrayList;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class ActivityDataModel {

    private String day;
    private String month;
    private String time_range;
    private String title;
    private String description;
    private ArrayList arr_img;

    public ActivityDataModel(String day, String month, String time_range, String title, String description, ArrayList arr_img) {
        this.day = day;
        this.month = month;
        this.time_range = time_range;
        this.title = title;
        this.description = description;
        this.arr_img = arr_img;
    }

    public ActivityDataModel() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTime_range() {
        return time_range;
    }

    public void setTime_range(String time_range) {
        this.time_range = time_range;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList getArr_img() {
        return arr_img;
    }

    public void setArr_img(ArrayList arr_img) {
        this.arr_img = arr_img;
    }

    @Override
    public String toString() {
        return "ActivityDataModel{" +
                "day='" + day + '\'' +
                ", month='" + month + '\'' +
                ", time_range=" + time_range + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
