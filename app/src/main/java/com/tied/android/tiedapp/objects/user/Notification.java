package com.tied.android.tiedapp.objects.user;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by femi on 11/22/2016.
 */
public class Notification implements Serializable {
    NotificationAction account_update, coworker, special, proximity;

    public void setAccount_update(NotificationAction account_update) {
        this.account_update = account_update;
    }

    public NotificationAction getAccount_update() {
        return account_update;
    }

    public void setCoworker(NotificationAction coworker) {
        this.coworker = coworker;
    }

    public NotificationAction getCoworker() {
        return coworker;
    }

    public void setProximity(NotificationAction proximity) {
        this.proximity = proximity;
    }

    public NotificationAction getProximity() {
        return proximity;
    }

    public void setSpecial(NotificationAction special) {
        this.special = special;
    }

    public NotificationAction getSpecial() {
        return special;
    }

    @Override
    public String toString() {
        return "Notification={"+account_update+","+coworker+","+proximity+","+special+"}";
    }
    public String toJSONString() {
        Gson gson=new Gson();
        return gson.toJson(this, Notification.class);
    }


}
