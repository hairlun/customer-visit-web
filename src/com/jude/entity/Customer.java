package com.jude.entity;

import java.io.Serializable;
import java.util.Date;

public class Customer extends GeneralObject implements Serializable {
	private static final long serialVersionUID = 8048127994276385480L;
	private String number;
	private String name;
	private String sellNumber;
	private String storeName;
	private String level;
	private String phoneNumber;
	private String backupNumber;
	private String address;
	private CustomerManager customerManager;
	private String orderType;
	private String gps;
	private Date lastVisitTime;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSellNumber() {
		return this.sellNumber;
	}

	public void setSellNumber(String sellNumber) {
		this.sellNumber = sellNumber;
	}

	public String getStoreName() {
		return this.storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getLevel() {
		return this.level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getBackupNumber() {
		return this.backupNumber;
	}

	public void setBackupNumber(String backupNumber) {
		this.backupNumber = backupNumber;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public CustomerManager getCustomerManager() {
		return this.customerManager;
	}

	public void setCustomerManager(CustomerManager customerManager) {
		this.customerManager = customerManager;
	}

	public String getOrderType() {
		return this.orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getGps() {
		return this.gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

	public Date getLastVisitTime() {
		return this.lastVisitTime;
	}

	public void setLastVisitTime(Date lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}