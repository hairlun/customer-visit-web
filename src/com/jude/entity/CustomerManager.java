package com.jude.entity;

import java.io.Serializable;

public class CustomerManager extends GeneralObject implements Serializable {
	private static final long serialVersionUID = -7642705883120525886L;
	private String username;
	private String password;
	private String name;
	private Department department;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
}