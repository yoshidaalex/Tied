package com.tied.android.tiedapp.objects.visit;

import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Territory;
import com.tied.android.tiedapp.objects.client.Client;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Emmanuel on 6/30/2016.
 */
public class VisitFilter implements Serializable {
    private Client client;
    private int month;
    private int year;
    private int distance;
    private String unit;
    private String sort;

    public VisitFilter() {
    }

    public VisitFilter(Client client, int month, int year, int distance, String unit, String sort) {
        this.client = client;
        this.month = month;
        this.year = year;
        this.distance = distance;
        this.unit = unit;
        this.sort = sort;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "VisitFilter{" +
                "client='" + client +
                ", month=" + month +
                ", year=" + year +
                ", sort='" + sort + '\'' +
                ", distance=" + distance +
                ", unit='" + unit + '\'' +
                '}';
    }
}
