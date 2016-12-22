package com.jude.entity;

import java.io.Serializable;
import java.util.Date;

public class WorkflowReply implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9058757162696565479L;

	private long id;

	private long workflowId;

	private String reply;
	
	private String remark;

	private int orderNum;
	
	private CustomerManager handler;
	
	private CustomerManager currentHandler;
	
	private Date handleTime;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public CustomerManager getHandler() {
        return handler;
    }

    public void setHandler(CustomerManager handler) {
        this.handler = handler;
    }

    public CustomerManager getCurrentHandler() {
		return currentHandler;
	}

	public void setCurrentHandler(CustomerManager currentHandler) {
		this.currentHandler = currentHandler;
	}

	public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

}
