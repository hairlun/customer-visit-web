package com.jude.json;

public abstract interface PropertyMapper<T> {
	public abstract void map(JSONObject paramJSONObject, T paramT);
}