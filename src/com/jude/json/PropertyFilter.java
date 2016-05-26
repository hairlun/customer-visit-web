package com.jude.json;

public abstract interface PropertyFilter<T> {
	public abstract boolean apply(T paramT, String paramString, Object paramObject);
}