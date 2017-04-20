package com.tied.android.tiedapp.objects.responses;

import java.io.Serializable;

/**
 * Created by Emmanuel on 7/8/2016.
 */
public class Count implements Serializable{
    private boolean success;
    private int count;
    private String message;
    private boolean authFailed;

    public Count(boolean success, int count, String message, boolean authFailed) {
        this.success = success;
        this.count = count;
        this.message = message;
        this.authFailed = authFailed;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAuthFailed() {
        return authFailed;
    }

    public void setAuthFailed(boolean authFailed) {
        this.authFailed = authFailed;
    }

    @Override
    public String toString() {
        return "Count{" +
                "success=" + success +
                ", count=" + count +
                ", message='" + message + '\'' +
                ", authFailed=" + authFailed +
                '}';
    }
}
