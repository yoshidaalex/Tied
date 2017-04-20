package com.tied.android.tiedapp.objects;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Emmanuel on 6/28/2016.
 */
public class Coordinate implements Serializable {
    private double lat;
    private double lon;

    public Coordinate(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }
    public String toJSONString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public static Coordinate fromJSONString(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Coordinate.class);
    }
}
