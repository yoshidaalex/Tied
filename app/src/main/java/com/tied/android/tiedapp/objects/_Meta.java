package com.tied.android.tiedapp.objects;

import java.io.Serializable;

/**
 * Created by Emmanuel on 5/30/2016.
 */
public class _Meta implements Serializable {
    private int status_code;
    private String developer_message;
    private String user_message;
    private String response_time;
    private int page_count, page_number, total, page_size;

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public int getPage_count() {
        return page_count;
    }

    public int getPage_number() {
        return page_number;
    }

    public void setPage_number(int page_number) {
        this.page_number = page_number;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getDeveloper_message() {
        return developer_message;
    }

    public void setDeveloper_message(String developer_message) {
        this.developer_message = developer_message;
    }

    public String getUser_message() {
        return user_message;
    }

    public void setUser_message(String user_message) {
        this.user_message = user_message;
    }

    public String getResponse_time() {
        return response_time;
    }

    public void setResponse_time(String response_time) {
        this.response_time = response_time;
    }

    public _Meta() {
    }

    public _Meta(int status_code, String developer_message, String user_message, String response_time) {
        this.status_code = status_code;
        this.developer_message = developer_message;
        this.user_message = user_message;
        this.response_time = response_time;
    }

    @Override
    public String toString() {
        return "_Meta{" +
                "status_code=" + status_code +
                ", developer_message='" + developer_message + '\'' +
                ", user_message='" + user_message + '\'' +
                ", response_time='" + response_time + '\'' +
                '}';
    }
}
