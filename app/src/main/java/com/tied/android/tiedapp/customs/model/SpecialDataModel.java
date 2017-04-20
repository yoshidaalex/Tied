package com.tied.android.tiedapp.customs.model;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class SpecialDataModel {

    private String title;
    private String date_range;
    private String client_count;


    public SpecialDataModel(String title, String date_range, String client_count) {
        this.title = title;
        this.date_range = date_range;
        this.client_count = client_count;
    }

    public SpecialDataModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate_range() {
        return date_range;
    }

    public void setDate_range(String date_range) {
        this.date_range = date_range;
    }

    public String getClient_count() {
        return client_count;
    }

    public void setClient_count(String client_count) {
        this.client_count = client_count;
    }
}
