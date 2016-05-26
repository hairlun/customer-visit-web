package com.jude.util;

public class ExcelColumn {
	private String title = "";
	private int width = 100;
	private Class<?> type = String.class;

	public ExcelColumn(String title, int width) {
		setTitle(title);
		setWidth(width);
	}

	public ExcelColumn(String title, int width, Class<?> type) {
		setTitle(title);
		setWidth(width);
		setType(type);
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Class<?> getType() {
		return this.type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}