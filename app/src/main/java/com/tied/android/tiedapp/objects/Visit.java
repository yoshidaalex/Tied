package com.tied.android.tiedapp.objects;

import com.google.gson.Gson;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.visit.Duration;

import java.io.Serializable;

/**
 * Created by Femi on 7/22/2016.
 */
public class Visit implements Serializable {

    public static final String TAG = Visit.class.getSimpleName();

    private String id;
    private String title;
    private String user_id;
    private String client_id;
    private String schedule_id;
    private Location address;
    private String visit_date;
    private String visit_time;
    private Duration duration;
    private float distance;
    private String unit;

    public Visit() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
    }

    public Location getAddress() {
        return address;
    }

    public void setAddress(Location address) {
        this.address = address;
    }

    public String getVisit_date() {
        return visit_date;
    }

    public void setVisit_date(String visit_date) {
        this.visit_date = visit_date;
    }

    public String getVisit_time() {
        return visit_time;
    }

    public void setVisit_time(String visit_time) {
        this.visit_time = visit_time;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Visit{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", user_id='" + user_id + '\'' +
                ", client_id='" + client_id + '\'' +
                ", schedule_id='" + schedule_id + '\'' +
                ", address='" + address +
                ", distance='" + distance + '\'' +
                ", unit='" + unit + '\'' +
                ", visit_date=" + visit_date + '\'' +
                ", visit_time=" + visit_time + '\'' +
                ", duration=" + duration +
                '}';
    }
    public String toJSONString() {
        Gson gson=new Gson();
        return gson.toJson(this, Visit.class);
    }
}


