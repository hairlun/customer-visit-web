package com.jude.entity;

import com.jude.json.JSONObject;

public class User extends GeneralObject {
	private static final long serialVersionUID = 2315990513065798004L;
	public String username;
	public String password;
	public String fullname;

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return this.fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		json.put("id", "\"" + getId() + "\"");
		json.put("username", getUsername());
		json.put("password", getPassword());
		json.put("fullname", (getFullname() == null) ? "" : getFullname());
		return json;
	}
}