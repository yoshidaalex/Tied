package com.tied.android.tiedapp.customs.model;

import android.graphics.Bitmap;

/**
 * Created by Emmanuel on 6/15/2016.
 */
public class SelectContact {
    String name;
    Bitmap thumb;
    String phone;
    String email;
    Boolean checkStatus = false;

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Boolean checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "SelectContact{" +
                "name='" + name + '\'' +
                ", thumb=" + thumb +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", checkStatus=" + checkStatus +
                '}';
    }
}
