package com.tied.android.tiedapp.objects;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Femi on 7/22/2016.
 */
public class CoWorker implements Serializable {

    public static final String TAG = CoWorker.class.getSimpleName();

    private String id;
    private String user_id;
    private String coworker_id;
    private boolean verified;
    private String created;
    private Public can_see;

    public CoWorker() {
    }

    public CoWorker(String id, String user_id, String coworker_id, boolean verified, String created) {
        this.id = id;
        this.user_id = user_id;
        this.coworker_id = coworker_id;
        this.verified = verified;
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCoworker_id() {
        return coworker_id;
    }

    public void setCoworker_id(String coworker_id) {
        this.coworker_id = coworker_id;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setCan_see(Public is_public) {
        this.can_see = is_public;
    }

    public Public getCan_see() {
        return can_see;
    }

    @Override
    public String toString() {
        return "CoWorker{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", coworker_id='" + coworker_id + '\'' +
                ", verified=" + verified +
                ", created='" + created + '\'' +
                '}';
    }

    public String toJSONString() {
        return new Gson().toJson(this);
    }


}


