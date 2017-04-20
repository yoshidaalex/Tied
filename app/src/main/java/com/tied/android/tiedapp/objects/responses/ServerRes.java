package com.tied.android.tiedapp.objects.responses;

import com.tied.android.tiedapp.objects.user.User;

/**
 * Created by Emmanuel on 5/30/2016.
 */
public class ServerRes {

    private boolean success;
    private String message;
    private boolean authFailed;
    private int code;
    private User user;

    public ServerRes() {
    }

    public ServerRes(boolean success, String message, boolean authFailed, int code, User user) {
        this.success = success;
        this.message = message;
        this.authFailed = authFailed;
        this.code = code;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ServerRes{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", authFailed=" + authFailed +
                ", code=" + code +
                ", user=" + user +
                '}';
    }
}
