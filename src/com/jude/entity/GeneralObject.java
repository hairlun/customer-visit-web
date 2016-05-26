package com.jude.entity;

import java.io.Serializable;

public class GeneralObject implements Serializable {
	private static final long serialVersionUID = 103606252165022182L;
	public Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}