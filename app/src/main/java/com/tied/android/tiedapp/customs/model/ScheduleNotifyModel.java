package com.tied.android.tiedapp.customs.model;

/**
 * Created by Emmanuel on 7/6/2016.
 */
public class ScheduleNotifyModel {

    int id;
    String txt_notify;
    Boolean checkStatus = false;

    public ScheduleNotifyModel(int id, String txt_notify, Boolean checkStatus) {
        this.id = id;
        this.txt_notify = txt_notify;
        this.checkStatus = checkStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTxt_notify() {
        return txt_notify;
    }

    public void setTxt_notify(String txt_notify) {
        this.txt_notify = txt_notify;
    }

    public Boolean getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Boolean checkStatus) {
        this.checkStatus = checkStatus;
    }
}
