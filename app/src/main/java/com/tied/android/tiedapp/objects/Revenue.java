package com.tied.android.tiedapp.objects;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Femi on 7/28/2016.
 */
public class Revenue implements Serializable {
    String id, user_id, line_id, client_id, title;
    String date_sold;

    float value;
    String created;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setLine_id(String line_id) {
        this.line_id = line_id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float valu) {
        this.value = valu;
    }

    public String getLine_id() {
        return line_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreated() {
        return created;
    }

    public void setDate_sold(String date) {
        this.date_sold = date;
    }

    public String getDate_sold() {
        return date_sold;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Revenue fromString(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, Revenue.class);
    }
}
