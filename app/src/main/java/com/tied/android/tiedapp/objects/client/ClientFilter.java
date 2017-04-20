package com.tied.android.tiedapp.objects.client;

import com.tied.android.tiedapp.objects.Coordinate;
import com.tied.android.tiedapp.objects.Territory;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Emmanuel on 6/30/2016.
 */
public class ClientFilter implements Serializable {
    private String name;
    private int distance;
    private String unit;
    private String group;
    private int last_visited;
    private String order_by;
    private String order;
    private ArrayList<Territory> territories;
    private ArrayList<String> lines;
    int page_size;
    private Coordinate coordinate;

    public ClientFilter() {
    }

    public ClientFilter(String name, int distance, String unit, String group, int last_visited, String order_by, String order, ArrayList<Territory> territories, ArrayList<String> lines, Coordinate coordinate) {
        this.name = name;
        this.distance = distance;
        this.unit = unit;
        this.group = group;
        this.last_visited = last_visited;
        this.order_by = order_by;
        this.order = order;
        this.territories = territories;
        this.lines = lines;

        this.coordinate = coordinate;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public int getPage_size() {
        return page_size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getLast_visited() {
        return last_visited;
    }

    public void setLast_visited(int last_visited) {
        this.last_visited = last_visited;
    }

    public String getOrder_by() {
        return order_by;
    }

    public void setOrder_by(String order_by) {
        this.order_by = order_by;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public ArrayList<Territory> getTerritories() {
        return territories;
    }

    public void setTerritories(ArrayList<Territory> territories) {
        this.territories = territories;
    }

    public ArrayList<String> getLines() {
        return lines;
    }

    public void setLines(ArrayList<String> lines) {
        this.lines = lines;
    }


    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public String toString() {
        return "ClientFilter{" +
                "name='" + name + '\'' +
                ", distance=" + distance +
                ", unit='" + unit + '\'' +
                ", group='" + group + '\'' +
                ", last_visited=" + last_visited +
                ", order_by='" + order_by + '\'' +
                ", order='" + order + '\'' +
                ", territories=" + territories +
                ", line_ids=" + lines +
                ", coordinate=" + coordinate +
                '}';
    }
}
