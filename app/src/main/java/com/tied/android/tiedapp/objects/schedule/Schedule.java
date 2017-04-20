package com.tied.android.tiedapp.objects.schedule;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.ScheduleDataModel;
import com.tied.android.tiedapp.objects.Location;

import java.io.Serializable;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class Schedule implements Serializable{

    private String id;
    private String user_id;
    private String client_id;
    private String title;
    private String description;
    private int reminder;
    private boolean visited;
    private String date;
    private TimeRange time_range;
    private String end_time;
    private Location location;
    private int status;

    public Schedule(String id) {
        this.id = id;
    }

    public Schedule(String id, String user_id, String client_id, String title, String description,
                    int reminder, boolean visited, String date, TimeRange time_range,
                    String end_time, Location location, int status) {
        this.id = id;
        this.user_id = user_id;
        this.client_id = client_id;
        this.title = title;
        this.description = description;
        this.reminder = reminder;
        this.visited = visited;
        this.date = date;
        this.time_range = time_range;
        this.end_time = end_time;
        this.location = location;
        this.status = status;
    }

    public Schedule() {
    }


    public static void scheduleCreated(Context context){
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putBoolean(Constants.SCHEDULE_CREATED, true );
        prefsEditor.apply();
    }

    public static boolean isScheduleCreated(Context context){
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getBoolean(Constants.SCHEDULE_CREATED, false);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReminder() {
        return reminder;
    }

    public void setReminder(int reminder) {
        this.reminder = reminder;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public TimeRange getTime_range() {
        return time_range;
    }

    public void setTime_range(TimeRange time_range) {
        this.time_range = time_range;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", client_id='" + client_id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", reminder=" + reminder +
                ", visited=" + visited +
                ", date='" + date + '\'' +
                ", time_range=" + time_range +
                ", end_time='" + end_time + '\'' +
                ", location=" + location +
                ", status=" + status +
                '}';
    }
    public String toJSONString() {
        Gson gson=new Gson();
        return gson.toJson(this, Schedule.class);
    }
}
