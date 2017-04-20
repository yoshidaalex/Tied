package com.tied.android.tiedapp.objects.responses;

import java.io.Serializable;

/**
 * Created by Emmanuel on 5/29/2016.
 */
public class CheckEmail implements Serializable{
    private boolean success;
    private String message;

    public CheckEmail() {
    }

    public CheckEmail(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "CheckEmail{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
