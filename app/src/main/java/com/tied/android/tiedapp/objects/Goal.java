package com.tied.android.tiedapp.objects;

import java.io.Serializable;

/**
 * Created by Emmanuel on 9/4/2016.
 */
public class Goal implements Serializable {

    public static final String SALES_TYPE="sales", VISIT_TYPE="visits", CLIENT_TYPE="clients";
    private String id;
    private String item_id;
    
    private String user_id;
    private String title;
    private String description;
    private String goal_type;
    private String target = "00.00";
    private String value;
    private String date;
    private String created;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGoal_type() {
        return goal_type;
    }

    public void setGoal_type(String goal_type) {
        this.goal_type = goal_type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getProgress(){
        return target +" of "+value +" Goals";
    }

    public String getExpirationDate(){
        return date;
    }

    public String getClientLinesCountString(){
        return "Expires in 4 days";
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String Item_id) {
        this.item_id = Item_id;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "Goal{" +
                "id='" + id + '\'' +
                ", item_id='" + item_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", goal_type='" + goal_type + '\'' +
                ", target='" + target + '\'' +
                ", value='" + value + '\'' +
                ", date='" + date + '\'' +
                ", created='" + created + '\'' +
                '}';
    }
}
