package com.tied.android.tiedapp.objects.responses;

import com.tied.android.tiedapp.objects._Meta;
import com.tied.android.tiedapp.objects.user.User;

/**
 * Created by Emmanuel on 5/30/2016.
 */
public class SignUpLogin {
    private _Meta _meta;
    private User user;

    public SignUpLogin(_Meta _meta, User user) {
        this._meta = _meta;
        this.user = user;
    }

    public _Meta get_meta() {
        return _meta;
    }

    public void set_meta(_Meta _meta) {
        this._meta = _meta;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "SignUpLogin{" +
                "_meta=" + _meta +
                ", user=" + user +
                '}';
    }
}
