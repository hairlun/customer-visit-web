package com.jude.util;

import com.jude.json.FilterPropertyMapper;
import com.jude.json.JSONArray;
import com.jude.json.JSONObject;
import com.jude.json.PropertyFilter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ExtJS {
	public static <T> JSONObject buildGrid(PagingSet<T> set) {
		JSONArray ja = new JSONArray(set.getList());
		JSONObject root = new JSONObject();
		root.put("rows", ja);
		root.put("total", set.getTotal());
		return root;
	}

	public static <T> JSONObject buildGrid(PagingSet<T> set, FilterPropertyMapper<T> mapper) {
		PropertyFilter filter = mapper.getFilter();

		JSONArray ja = new JSONArray();
		for (Iterator i$ = set.getList().iterator(); i$.hasNext();) {
			Object t = i$.next();
			JSONObject jo = new JSONObject(t, filter);
			mapper.map(jo, (T) t);
			ja.put(jo);
		}

		JSONObject root = new JSONObject();
		root.put("rows", ja);
		root.put("total", set.getTotal());
		return root;
	}

	public static <T> JSONObject buildGrid(Collection<T> list) {
		return buildGrid(list, list.size());
	}

	public static <T> JSONObject buildGrid(Collection<T> list, int total) {
		JSONArray ja = new JSONArray(list);
		JSONObject root = new JSONObject();
		root.put("rows", ja);
		root.put("total", total);
		return root;
	}

	public static <T> JSONObject buildGrid(Collection<T> list, FilterPropertyMapper<T> mapper) {
		PropertyFilter filter = mapper.getFilter();

		JSONArray ja = new JSONArray();
		for (Iterator i$ = list.iterator(); i$.hasNext();) {
			Object t = i$.next();
			JSONObject jo = new JSONObject(t, filter);
			mapper.map(jo, (T) t);
			ja.put(jo);
		}

		return buildGrid(ja);
	}

	public static <T> JSONObject buildGrid(JSONArray rows) {
		return buildGrid(rows, rows.length());
	}

	public static <T> JSONObject buildGrid(JSONArray rows, int total) {
		JSONObject root = new JSONObject();
		root.put("rows", rows);
		root.put("total", total);
		return root;
	}

	public static <T> JSONArray buildTree(Collection<T> list, FilterPropertyMapper<T> mapper) {
		JSONArray ja = new JSONArray();
		PropertyFilter filter = mapper.getFilter();
		for (Iterator i$ = list.iterator(); i$.hasNext();) {
			Object t = i$.next();
			JSONObject jo = new JSONObject(t, filter);
			mapper.map(jo, (T) t);
			ja.put(jo);
		}

		return ja;
	}

	public static JSONObject buildStatus(boolean success) {
		return buildStatus(success, null);
	}

	public static JSONObject buildStatus(boolean success, String msg) {
		JSONObject jo = new JSONObject();
		jo.put("success", success);
		jo.put("msg", msg);
		return jo;
	}

	public static JSONObject ok() {
		return buildStatus(true, null);
	}

	public static JSONObject ok(String msg) {
		return buildStatus(true, msg);
	}

	public static JSONObject fail() {
		return buildStatus(false, null);
	}

	public static JSONObject fail(String msg) {
		return buildStatus(false, msg);
	}
}