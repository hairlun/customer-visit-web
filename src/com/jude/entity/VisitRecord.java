package com.jude.entity;

import java.io.Serializable;
import java.util.Date;

public class VisitRecord implements Serializable {
	private static final long serialVersionUID = 4045141914433175779L;
	private String id;
	private Customer customer;
	private CustomerManager customerManager;
	private int type;
	private String content;
	private Date visitTime;
	private Date leaveTime;
	private String gps;
	private String city;
	private int images;
	private String taskId;
	private int resultCode;
	private int costTime;
	private String reply;
	private int reject;

	public int getCostTime() {
		return this.costTime;
	}

	public void setCostTime(int costTime) {
		this.costTime = costTime;
	}

	public int getResultCode() {
		return this.resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public int getImages() {
		return this.images;
	}

	public void setImages(int images) {
		this.images = images;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getGps() {
		return this.gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public CustomerManager getCustomerManager() {
		return this.customerManager;
	}

	public void setCustomerManager(CustomerManager customerManager) {
		this.customerManager = customerManager;
	}

	public Date getVisitTime() {
		return this.visitTime;
	}

	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}

	public Date getLeaveTime() {
		return this.leaveTime;
	}

	public void setLeaveTime(Date leaveTime) {
		this.leaveTime = leaveTime;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public int getReject() {
		return reject;
	}

	public void setReject(int reject) {
		this.reject = reject;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}