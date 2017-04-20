package com.tied.android.tiedapp.objects;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by femi on 9/28/2016.
 */
public class RevenueFilter  implements Serializable {
   // String start_date, end_date, sort, client_id, line_id;
    //int quarter;
    int month, year, quarter; String client_id, line_id, sort;

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

  /*  public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }



    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
*/

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
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


    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setLine_id(String line_id) {
        this.line_id = line_id;
    }

    public String getLine_id() {
        return line_id;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public String toJSONString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static RevenueFilter fromString(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, RevenueFilter.class);
    }
}
