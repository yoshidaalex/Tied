package com.tied.android.tiedapp.objects.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.tied.android.tiedapp.customs.Constants;
import com.tied.android.tiedapp.customs.model.TerritoryModel;
import com.tied.android.tiedapp.objects.Line;
import com.tied.android.tiedapp.objects.Location;
import com.tied.android.tiedapp.objects.Territory;
import com.tied.android.tiedapp.util.HelperMethods;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Emmanuel on 6/28/2016.
 */
public class Client implements Serializable, Comparable<Client>{

    public static final String TAG = Client.class.getSimpleName();

    private String id;
    private String user_id;
    private String line_id;
    private String full_name;
    private String company;
    private String logo;
    private String description;
    private String phone;
    private String email;
    private Location address;
    private String birthday;
    private String fax;
    private String note;
    private String revenue;
    private String ytd_revenue;
    private double total_revenue;
    private String territory_id;
    private Territory territory;

    private int Industry_id;
    private int visit_id;
    private String last_visited;

    private int dis_from;
    private ArrayList _score;
    Boolean checkStatus = false;
   // ArrayList<TerritoryModel> territories = new ArrayList<>();
    private ArrayList<Line> lines =  new ArrayList<>();

    public Client() {
    }


    public static void clientCreated(Context context){
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putBoolean(Constants.CLIENT_CREATED, true );
        prefsEditor.apply();
    }

    public String getLast_visited() {
        return last_visited;
    }

    public void setTerritory_id(String territory_id) {
        this.territory_id = territory_id;
    }

    public String getTerritory_id() {
        return territory_id;
    }

    public void setLast_visited(String last_visited) {
        this.last_visited = last_visited;
    }

    public static boolean isClientCreated(Context context){
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getBoolean(Constants.CLIENT_CREATED, false);
    }

    public double getTotal_revenue() {
        return total_revenue;
    }

    public void setTotal_revenue(double total_revenue) {
        this.total_revenue = total_revenue;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }

    /*public ArrayList<TerritoryModel> getTerritories() {
        return territories;
    }
*/
    /*
    public void setTerritories(ArrayList<TerritoryModel> territories) {
        this.territories = territories;
    }
*/
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public String getYtd_revenue() {
        return ytd_revenue;
    }

    public void setYtd_revenue(String ytd_revenue) {
        this.ytd_revenue = ytd_revenue;
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

    public String getLine_id() {
        return line_id;
    }

    public void setLine_id(String line_id) {
        this.line_id = line_id;
    }

    public String getFull_name() {
        Client client=this;
        return   (client.getCompany()==null || client.getCompany().isEmpty())?client.getFull_name():client.getCompany();
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Location getAddress() {
        return address;
    }

    public void setAddress(Location address) {
        this.address = address;
    }

    public int getIndustry_id() {
        return Industry_id;
    }

    public void setIndustry_id(int industry_id) {
        Industry_id = industry_id;
    }

    public int getVisit_id() {
        return visit_id;
    }

    public void setVisit_id(int visit_id) {
        this.visit_id = visit_id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getDis_from() {

        double result = 0.0;
        if(get_score() != null && get_score().size() > 0) {
            result = (double) get_score().get(0);
        }
        result = HelperMethods.kilometerToMile(result);
        return (int) result;
    }

    public void setDis_from(int dis_from) {
        this.dis_from = dis_from;
    }

    public ArrayList get_score() {
        return _score;
    }

    public void set_score(ArrayList _score) {
        this._score = _score;
    }

    public Boolean getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Boolean checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Territory getTerritory() {
        return territory;
    }

    public void setTerritory(Territory territory) {
        this.territory = territory;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", line_id='" + line_id + '\'' +
                ", full_name='" + full_name + '\'' +
                ", company='" + company + '\'' +
                ", logo='" + logo + '\'' +
                ", description='" + description + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address +
                ", birthday='" + birthday + '\'' +
                ", fax='" + fax + '\'' +
                ", note='" + note + '\'' +
                ", revenue='" + revenue + '\'' +
                ", ytd_revenue='" + ytd_revenue + '\'' +
                ", Industry_id=" + Industry_id +
                ", visit_id=" + visit_id +
                ", dis_from=" + dis_from +
                ", _score=" + _score +
                ", checkStatus=" + checkStatus +
               // ", territories=" + territories +
                ", total_revenue=" + total_revenue +
                ", territory=" + territory +
                '}';
    }

    @Override
    public int compareTo(Client client) {
        return getFull_name().toUpperCase().compareTo(client.getFull_name().toUpperCase());
    }
    public String toJSONString() {
        Gson gson=new Gson();
        return gson.toJson(this, Client.class);
    }
}
