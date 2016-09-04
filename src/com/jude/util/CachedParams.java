package com.jude.util;

import com.jude.json.JSONArray;
import com.jude.json.JSONObject;

public class CachedParams {
	public static final String SUCCESS = "000000";
	public static final String REQUESTFAIL = "000001";
	public static final String CLOSED = "000002";
	public static final String ERROR = "000003";
	public static JSONArray menus = new JSONArray();

	public static void load() {
		JSONArray systemChidren = new JSONArray();
		JSONObject jo6 = new JSONObject();
		jo6.put("id", "6");
		jo6.put("leaf", true);
		jo6.put("text", "开关设置");
		jo6.put("src", "switch.do?action=forwardIndex");
		JSONObject jo7 = new JSONObject();
		jo7.put("id", "7");
		jo7.put("leaf", true);
		jo7.put("text", "账号管理");
		jo7.put("src", "user.do?action=forwardIndex");
		systemChidren.put(jo6);
		systemChidren.put(jo7);

		JSONObject jo1 = new JSONObject();
		jo1.put("id", "1");
		jo1.put("leaf", true);
		jo1.put("text", "客户基础信息");
		jo1.put("src", "customer.do?action=forwardIndex");
		menus.put(jo1);
		JSONObject jo8 = new JSONObject();
		jo8.put("id", "8");
		jo8.put("leaf", true);
		jo8.put("text", "客户分组管理");
		jo8.put("src", "customerGroup.do?action=forwardIndex");
		menus.put(jo8);
		JSONObject jo2 = new JSONObject();
		jo2.put("id", "2");
		jo2.put("leaf", true);
		jo2.put("text", "客户经理管理");
		jo2.put("src", "customerManager.do?action=forwardIndex");
		menus.put(jo2);
		JSONObject jo9 = new JSONObject();
		jo9.put("id", "9");
		jo9.put("leaf", true);
		jo9.put("text", "部门管理");
		jo9.put("src", "department.do?action=forwardIndex");
		menus.put(jo9);
		JSONObject jo3 = new JSONObject();
		jo3.put("id", "3");
		jo3.put("leaf", true);
		jo3.put("text", "任务发放");
		jo3.put("src", "task.do?action=forwardIndex");
		menus.put(jo3);
		JSONObject jo4 = new JSONObject();
		jo4.put("id", "4");
		jo4.put("leaf", true);
		jo4.put("text", "客户拜访管理");
		jo4.put("src", "record.do?action=forwardIndex");
		menus.put(jo4);
		JSONObject jo5 = new JSONObject();
		jo5.put("id", "5");
		jo5.put("leaf", false);
		jo5.put("text", "系统设置");
		jo5.put("children", systemChidren);
		menus.put(jo5);
	}

	public static JSONArray getMenus() {
		if (menus == null) {
			load();
		}
		return menus;
	}
}