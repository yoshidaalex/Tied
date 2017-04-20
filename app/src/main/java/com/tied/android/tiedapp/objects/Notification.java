package com.tied.android.tiedapp.objects;

import com.google.gson.Gson;
import com.tied.android.tiedapp.objects.schedule.Schedule;
import com.tied.android.tiedapp.objects.visit.Duration;

import java.io.Serializable;

/**
 * Created by Femi on 7/22/2016.
 */
public class Notification implements Serializable {

    public static final String TAG = Notification.class.getSimpleName();

    private String id;
    private String comment;
    private String object;
    private String object_id;
    private String type;
    private String group;
    private String user_id;
    private boolean seen;
    private long created;

    public Notification() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }



    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id +
                ", comment='" + comment + '\'' +
                ", object='" + object + '\'' +
                ", object_id='" + object_id + '\'' +
                ", type='" + type + '\'' +
                ", group='" + group + '\'' +
                ", user_id='" + user_id + '\'' +
                ", seen=" + seen +
                ", created=" + created +
                '}';
    }
    public String toJSONString() {
        Gson gson=new Gson();
        return gson.toJson(this, Notification.class);
    }
    public static Notification fromJSONString(String str) {
        Gson gson=new Gson();
        return gson.fromJson(str, Notification.class);
    }
}


