package com.tied.android.tiedapp.objects;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by femi on 10/5/2016.
 */
public class Territory implements Serializable {
    String id, county, city,state,country;
    int num_clients;

    public void setCounty(String county) {
        this.county = county;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCounty() {
        return county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getNum_clients() {
        return num_clients;
    }

    public void setNum_clients(int num_clients) {
        this.num_clients = num_clients;
    }


    @Override
    public String toString() {
        return "Territory{id="+id+", county="+county+", state="+state+"}";
    }

    public static Territory fromJSONString(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Territory.class);
    }

    public String toJSONString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
