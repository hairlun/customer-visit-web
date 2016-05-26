package com.jude.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class PagingSet<T> {
	private int total;
	private List<T> list = new ArrayList();

	private Map<Object, Object> map = new HashMap();

	public void put(Object key, Object value) {
		this.map.put(key, value);
	}

	public void putAll(Map<Object, Object> params) {
		Set<Entry<Object, Object>> set = params.entrySet();
		for (Map.Entry e : set)
			put(e.getKey(), e.getValue());
	}

	public Object get(String key) {
		return this.map.get(key);
	}

	public Map<Object, Object> getAll() {
		return this.map;
	}

	public PagingSet(List<T> list) {
		this(list, 0, (list == null) ? 0 : list.size());
	}

	public PagingSet(List<T> list, int start, int limit) {
		if (list == null) {
			list = Collections.emptyList();
		}
		setTotal(list.size());

		if (start < 0) {
			start = 0;
		}
		int endIndex = start + limit;
		if (endIndex > this.total) {
			endIndex = this.total;
		}
		for (int i = start; i < endIndex; ++i)
			this.list.add(list.get(i));
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotal() {
		return this.total;
	}

	public List<T> getList() {
		return this.list;
	}
}