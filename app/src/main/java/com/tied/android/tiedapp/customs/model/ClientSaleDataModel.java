package com.tied.android.tiedapp.customs.model;

/**
 * Created by Emmanuel on 7/1/2016.
 */
public class ClientSaleDataModel {

    private String price;
    private String date;
    private String summary;

    public ClientSaleDataModel(String price, String date, String summary) {
        this.date = date;
        this.summary = summary;
        this.price = price;
    }

    public ClientSaleDataModel() {
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "ClientSaleDataModel{" +
                "date='" + date + '\'' +
                ", price='" + price + '\'' +
                ", summary=" + summary + '\'' +
                '}';
    }
}
