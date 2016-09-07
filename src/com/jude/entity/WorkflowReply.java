package com.jude.entity;

import java.io.Serializable;

public class WorkflowReply implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9058757162696565479L;

	private long id;

	private long workflowId;

	private String reply;

	private int orderNum;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(long workflowId) {
		this.workflowId = workflowId;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

}
