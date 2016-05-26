package com.jude.entity;

import java.io.Serializable;

public class ModelSwitch implements Serializable {
	private static final long serialVersionUID = -2176298898283931507L;
	private String qrcode;
	private String timestamp;

	public String getQrcode() {
		return this.qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public String getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}