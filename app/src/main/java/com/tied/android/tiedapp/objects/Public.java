package com.tied.android.tiedapp.objects;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by femi on 10/11/2016.
 */
public class Public implements Serializable {
    boolean sales=false;
    boolean goals=false;
    boolean activities=false, clients=false, territory=false, line=false, industry=false,schedules=false;

    public void setClients(boolean clients) {
        this.clients = clients;
    }
    public boolean getClients() {
        return this.clients;
    }
    public void setSales(boolean sales) {
        this.sales = sales;
    }

    public void setGoals(boolean goals) {
        this.goals = goals;
    }

    public void setActivities(boolean activities) {
        this.activities = activities;
    }

    public void setTerritory(boolean territory) {
        this.territory = territory;
    }

    public void setLine(boolean line) {
        this.line = line;
    }

    public void setIndustry(boolean industry) {
        this.industry = industry;
    }
    public boolean getSales() {
        return sales;
    }

    public boolean getGoals() {
        return this.goals;
    }

    public boolean getActivities() {
       return this.activities;
    }

    public boolean getTerritory() {
        return  this.territory;
    }

    public boolean getLine() {
        return  this.line;
    }

    public boolean getIndustry() {
        return this.industry;
    }
    public boolean getSchedules() {
        return schedules;
    }
    public void setSchedules(boolean schedules) {
        this.schedules = schedules;
    }
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
