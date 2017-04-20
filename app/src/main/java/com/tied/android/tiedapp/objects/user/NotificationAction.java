package com.tied.android.tiedapp.objects.user;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by femi on 11/22/2016.
 */
public class NotificationAction implements  Serializable {
    boolean email, push;
    public NotificationAction (boolean receiveEmail, boolean receivePush) {
        this.push =receivePush;
        this.email =receiveEmail;
    }
    public NotificationAction() {

    }
    public void setEmail(boolean email) {
        this.email = email;
    }

    public void setPush(boolean push) {
        this.push = push;
    }
    public boolean getEmail() {
        return this.email;
    }
    public boolean getPush() {
        return this.push;
    }

    @Override
    public String toString() {
        return "Action={"+ email +","+ push +"}";
    }
    public String toJSONString() {
        Gson gson=new Gson();
        return gson.toJson(this, NotificationAction.class);
    }
}