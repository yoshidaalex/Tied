package com.tied.android.tiedapp.objects.user;


import com.tied.android.tiedapp.objects.Location;

import java.io.Serializable;

/**
 * Created by Emmanuel on 5/28/2016.
 */
public class Boss implements Serializable {
    public static final String TAG = Boss.class.getSimpleName();

    private String email;
    private String first_name;
    private String last_name;
    private String phone;
    private String territory;

    public Location office_address;

    public Boss(String email, String first_name, String last_name, String phone, String territory, Location office_address) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.territory = territory;
        this.office_address = office_address;
    }

    public Boss(String email, String phone, String territory, Location office_address) {
        this.email = email;
        this.phone = phone;
        this.territory = territory;
        this.office_address = office_address;
    }

    public Boss(String email, String phone, Location office_address) {
        this.email = email;
        this.phone = phone;
        this.office_address = office_address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Location getOffice_address() {
        return office_address;
    }

    public void setOffice_address(Location office_address) {
        this.office_address = office_address;
    }

    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

    @Override
    public String toString() {
        return "Boss{" +
                "email='" + email + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", phone='" + phone + '\'' +
                ", territory='" + territory + '\'' +
                ", office_address=" + office_address +
                '}';
    }
}
