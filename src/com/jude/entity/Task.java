package com.jude.entity;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {
	private static final long serialVersionUID = 6130970904909366643L;
	private String id;
	private Date start;
	private Date end;
	private Date create;
	private Customer customer;
	private CustomerManager manager;
	private String content;
	private int complete;
	private String reply;
	private int reject;

	public Date getCreate() {
		return this.create;
	}

	public void setCreate(Date create) {
		this.create = create;
	}

	public Date getStart() {
		return this.start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return this.end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public CustomerManager getManager() {
		return this.manager;
	}

	public void setManager(CustomerManager manager) {
		this.manager = manager;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getComplete() {
		return complete;
	}

	public void setComplete(int complete) {
		this.complete = complete;
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