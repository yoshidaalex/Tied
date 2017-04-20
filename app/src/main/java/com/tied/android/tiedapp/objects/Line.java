package com.tied.android.tiedapp.objects;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.tied.android.tiedapp.customs.Constants;

import java.io.Serializable;

/**
 * Created by Femi on 7/22/2016.
 */
public class Line implements Serializable {

    public static final String TAG = Line.class.getSimpleName();

    private String id;
    private String user_id;
    private String description;
    private Location address;
    private String name;

    private String website;
    private String opening;
    private String reorder;
    private String request;

   // private int dis_from;
    private String _score;

    private String sales;
    //boolean check_status;
    private float total_revenue=0f;
    //private int num_clients, num_goals, num_territories=0;

    public Line() {
    }

    public Line(String id, String name, String sales) {
        this.id = id;
        this.name = name;
        this.sales = sales;
       // this.check_status = check_status;
    }

    public void setRelevantInfo(String website, String request, String opening, String reorder){
        this.request = request;
        this.website = website;
        this.opening = opening;
        this.reorder = reorder;
    }

    public String getRequest() {
        return request;
    }

    public String getWebsite() {
        return website;
    }

    public String getOpening() {
        return opening;
    }

    public String getReorder() {
        return reorder;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static void lineCreated(Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putBoolean(Constants.LINE_CREATED, true);
        prefsEditor.apply();
    }

    public static boolean isLineCreated(Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getBoolean(Constants.LINE_CREATED, false);
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


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Location getAddress() {
        return address;
    }

    public void setAddress(Location address) {
        this.address = address;
    }

    public String get_score() {
        return _score;
    }

    public void set_score(String _score) {
        this._score = _score;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

  /*  public boolean isCheck_status() {
        return check_status;
    }

    public void setCheck_status(boolean check_status) {
        this.check_status = check_status;
    }

    */

    public float getTotal_revenue() {
        return total_revenue;
    }

    public void setTotal_revenue(float total_revenue) {
        this.total_revenue = total_revenue;
    }
    /*

    public void setNum_clients(int num_clients) {
        this.num_clients = num_clients;
    }

    public int getNum_clients() {
        return num_clients;
    }

    public int getNum_goals() {
        return num_goals;
    }

    public void setNum_goals(int num_goals) {
        this.num_goals = num_goals;
    }

    public int getNum_territories() {
        return num_territories;
    }

    public void setNum_territories(int num_territories) {
        this.num_territories = num_territories;
    } */

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}


