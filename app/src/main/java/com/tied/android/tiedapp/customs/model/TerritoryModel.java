package com.tied.android.tiedapp.customs.model;

import java.io.Serializable;

public class TerritoryModel implements Serializable{
	
	String territory_name;
	boolean iNew;

    private int no_clients;
    boolean check_status;

    public TerritoryModel() {

    }

    public TerritoryModel(String territory_name) {
        this.territory_name = territory_name;
    }

    public TerritoryModel(String territory_name, int no_clients, boolean check_status) {
        this.territory_name = territory_name;
        this.no_clients = no_clients;
        this.check_status = check_status;
    }

    public boolean isiNew() {
		return iNew;
	}
	public void setiNew(boolean iNew) {
		this.iNew = iNew;
	}

	public String getTerritory_name() {
		return territory_name;
	}

	public void setTerritory_name(String territory_name) {
		this.territory_name = territory_name;
	}

    public int getNo_clients() {
        return no_clients;
    }

    public void setNo_clients(int no_clients) {
        this.no_clients = no_clients;
    }

    public boolean isCheck_status() {
        return check_status;
    }

    public void setCheck_status(boolean check_status) {
        this.check_status = check_status;
    }

    @Override
	public String toString() {
		return "TerritoryModel{" +
				"territory_name='" + territory_name + '\'' +
				", iNew=" + iNew +
				'}';
	}
}
