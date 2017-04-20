package com.tied.android.tiedapp.objects.client;

import com.tied.android.tiedapp.objects.Coordinate;

import java.io.Serializable;

/**
 * Created by Emmanuel on 6/30/2016.
 */
public class ClientLocation implements Serializable {
    private String distance;
    private Coordinate coordinate;

    public ClientLocation() {
    }

    public ClientLocation(String distance, Coordinate coordinate) {
        this.distance = distance;
        this.coordinate = coordinate;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public String toString() {
        return "ClientLocation{" +
                "distance='" + distance + '\'' +
                ", coordinate=" + coordinate +
                '}';
    }
}
