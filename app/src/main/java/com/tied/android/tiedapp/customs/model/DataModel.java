package com.tied.android.tiedapp.customs.model;

import com.google.gson.Gson;

public class DataModel {

	int id;
	String name;
	boolean check_status;

	public DataModel(int id, String name, boolean check_status) {
		this.id = id;
		this.name = name;
		this.check_status = check_status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public boolean isCheck_status() {
		return check_status;
	}

	public void setCheck_status(boolean check_status) {
		this.check_status = check_status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "DataModel{" +
				"id=" + id +
				", name='" + name + '\'' +
				", check_status=" + check_status +
				'}';
	}

	public String toJSONString() {
		Gson gson = new Gson();
		return gson.toJson(this, DataModel.class);
	}
}
