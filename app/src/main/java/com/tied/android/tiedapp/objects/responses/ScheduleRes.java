package com.tied.android.tiedapp.objects.responses;

import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects._Meta;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Emmanuel on 6/28/2016.
 */
public class ScheduleRes implements Serializable {
    private String id;
    private boolean success;
    private String message;
    private boolean authFailed;
    private _Meta _meta;
    private ArrayList<Schedule> schedules;
    private Schedule schedule;

    public ScheduleRes(String id, boolean success, String message, boolean authFailed, _Meta _meta, ArrayList<Schedule> schedules, Schedule schedule) {
        this.id = id;
        this.success = success;
        this.message = message;
        this.authFailed = authFailed;
        this._meta = _meta;
        this.schedules = schedules;
        this.schedule = schedule;
    }

    public _Meta get_meta() {
        return _meta;
    }

    public void set_meta(_Meta _meta) {
        this._meta = _meta;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(ArrayList<Schedule> schedules) {
        this.schedules = schedules;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAuthFailed() {
        return authFailed;
    }

    public void setAuthFailed(boolean authFailed) {
        this.authFailed = authFailed;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ScheduleRes{" +
                "id='" + id + '\'' +
                ", success=" + success +
                ", message='" + message + '\'' +
                ", authFailed=" + authFailed +
                ", _meta=" + _meta +
                ", schedules=" + schedules +
                ", schedule=" + schedule +
                '}';
    }
}
