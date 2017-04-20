package com.tied.android.tiedapp.customs.model;

import com.google.gson.Gson;
import com.tied.android.tiedapp.objects.schedule.Schedule;

import java.util.ArrayList;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class ScheduleDataModel {

    private String day;
    private String week_day;
    private ArrayList<Schedule> schedules;
    private String temperature;
    private String weather;

    public ScheduleDataModel(String day, String week_day, ArrayList<Schedule> schedules, String temperature, String weather) {
        this.day = day;
        this.week_day = week_day;
        this.schedules = schedules;
        this.temperature = temperature;
        this.weather = weather;
    }

    public ScheduleDataModel() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeek_day() {
        return week_day;
    }

    public void setWeek_day(String week_day) {
        this.week_day = week_day;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(ArrayList<Schedule> schedules) {
        this.schedules = schedules;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "ScheduleDataModel{" +
                "day='" + day + '\'' +
                ", week_day='" + week_day + '\'' +
                ", schedules=" + schedules +
                ", temperature='" + temperature + '\'' +
                ", weather='" + weather + '\'' +
                '}';
    }

    public String toJSONString() {
        Gson gson=new Gson();
        return gson.toJson(this, ScheduleDataModel.class);
    }
}
