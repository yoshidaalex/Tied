package com.tied.android.tiedapp.objects.user;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.tied.android.tiedapp.MainApplication;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.ui.activities.MainActivity;
import com.tied.android.tiedapp.ui.activities.signups.WalkThroughActivity;
import com.tied.android.tiedapp.util.MyUtils;
import retrofit2.Retrofit;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Emmanuel on 5/28/2016.
 */
public class User implements Serializable {
    public static final String TAG = User.class.getSimpleName();

    private String id;
    private String email;
    private String first_name;
    private String last_name;
    private String phone;
    private String fax;
    private String password;
    private String avatar;
    private String  sub_expiration_date;
   // private String avatar_uri;

    private Notification notification;
    private String sale_type;
    private String co_workers;
    private String group_description;
    private ArrayList<String> territories;
    private ArrayList<String> industries;

    private String company_name;
    private int sign_up_stage;

    public Location home_address;
    public Location office_address;

    public Boss boss;

    private String createdAt;
    private String updatedAt;

    private String token;

    public User(String id, String email, String first_name, String last_name, String phone, String fax,
                String password, String avatar, String avatar_uri, String sale_type, String co_workers,
                String group_description, ArrayList<String> territories, ArrayList<String> industries,
                String company_name, int sign_up_stage, Location home_address,
                Location office_address, Boss boss, String createdAt, String updatedAt, String token) {
        this.id = id;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.fax = fax;
        this.password = password;
        this.avatar = avatar;
        //this.avatar_uri = avatar_uri;
        this.sale_type = sale_type;
        this.co_workers = co_workers;
        this.group_description = group_description;
        this.territories = territories;
        this.industries = industries;
        this.company_name = company_name;
        this.sign_up_stage = sign_up_stage;
        this.home_address = home_address;
        this.office_address = office_address;
        this.boss = boss;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.token = token;
    }

    public User(String id, String email, String token, int sign_up_stage) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.sign_up_stage = sign_up_stage;
    }

    public User() {
    }

    public static User getCurrentUser(Context context){
        Gson gson = new Gson();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = mPrefs.getString(Constants.CURRENT_USER, "");
        return gson.fromJson(json, User.class);
    }

    public String getSub_expiration_date() {
        return sub_expiration_date;
    }

    public void setSub_expiration_date(String sub_expiration_date) {
        this.sub_expiration_date = sub_expiration_date;
    }

    public boolean save(Context context){
        User user = this;
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString(Constants.CURRENT_USER,this.toJSONString());
        //prefsEditor.putString(Constants.IS_LOGGED_IN_USER, json);
        return prefsEditor.commit();

    }

    public static boolean isUserLoggedIn(Context context){
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getBoolean(Constants.IS_LOGGED_IN_USER, false);
    }


    public void LogIn(Context context){
        User user = this;
        boolean saved = user.save(context);
        if(saved){
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            SharedPreferences.Editor editor = sharedPref.edit();
            //editor.putString(Constants.CURRENT_USER,this.toJSONString());
            editor.putBoolean(Constants.IS_LOGGED_IN_USER,true);
            editor.commit();
            MainApplication.updateToken(getToken());
            MyUtils.startActivity(context, MainActivity.class);
        }
    }

    public static void LogOut(Context context){
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putBoolean(Constants.IS_LOGGED_IN_USER,false);
        prefsEditor.remove(Constants.CURRENT_USER);
        prefsEditor.commit();
        MainApplication.clearToken();

        MyUtils.startActivity(context, WalkThroughActivity.class);
        try{
            ((Activity)context).finish();
        }catch (Exception e) {

        }

    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getSign_up_stage() {
        return sign_up_stage;
    }

    public void setSign_up_stage(int sign_up_stage) {
        this.sign_up_stage = sign_up_stage;
    }

    public Location getHome_address() {
        return home_address;
    }

    public void setHome_address(Location home_address) {
        this.home_address = home_address;
    }

    public Location getOffice_address() {
        return office_address;
    }

    public Boss getBoss() {
        return boss;
    }

    public void setBoss(Boss boss) {
        this.boss = boss;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public void setOffice_address(Location office_address) {
        this.office_address = office_address;
    }

    public ArrayList<String> getTerritories() {
        return territories;
    }

    public void setTerritories(ArrayList<String> territories) {
        this.territories = territories;
    }

    public String getSale_type() {
        return sale_type;
    }

    public void setSale_type(String sale_type) {
        this.sale_type = sale_type;
    }

    public ArrayList<String> getIndustries() {
        return industries;
    }

    public void setIndustries(ArrayList<String> industries) {
        this.industries = industries;
    }

    public String getGroup_description() {
        return group_description;
    }

    public void setGroup_description(String group_description) {
        this.group_description = group_description;
    }

    public String getCo_workers() {
        return co_workers;
    }

    public void setCo_workers(String co_workers) {
        this.co_workers = co_workers;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Notification getNotification() {
        return notification;
    }

    /*
               public String getAvatar_uri() {
                   return avatar_uri;
               }

              public void setAvatar_uri(String avatar_uri) {
                   this.avatar_uri = avatar_uri;
               }
               public String getAvatarURL() {
                   return Constants.GET_AVATAR_ENDPOINT + "avatar_" + getId() + ".jpg";
               }
           */
    public String getFullName(){
        return first_name +" "+last_name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", phone='" + phone + '\'' +
                ", fax='" + fax + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
               // ", avatar_uri='" + avatar_uri + '\'' +
                ", sale_type='" + sale_type + '\'' +
                ", co_workers='" + co_workers + '\'' +
                ", group_description='" + group_description + '\'' +
                ", territories=" + territories +
                ", industries=" + industries +
                ", sign_up_stage=" + sign_up_stage +
                ", home_address=" + home_address +
                ", office_address=" + office_address +
                ", boss=" + boss +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public String toJSONString() {
        Gson gson=new Gson();
        return gson.toJson(this, User.class);
    }
}
