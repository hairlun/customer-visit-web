package com.jude.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Workflow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4204559602557079177L;

	private Long id;

	private Customer customer;

	private String address;

	private Date receiveTime;

	private CustomerManager problemFinder;

	private Date handleTime;

	private CustomerManager handler;

	private Date solvedTime;

	private String description;

	private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public CustomerManager getProblemFinder() {
		return problemFinder;
	}

	public void setProblemFinder(CustomerManager problemFinder) {
		this.problemFinder = problemFinder;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public CustomerManager getHandler() {
		return handler;
	}

	public void setHandler(CustomerManager handler) {
		this.handler = handler;
	}

	public Date getSolvedTime() {
		return solvedTime;
	}

	public void setSolvedTime(Date solvedTime) {
		this.solvedTime = solvedTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
