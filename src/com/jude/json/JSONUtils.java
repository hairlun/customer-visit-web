package com.jude.json;

import java.lang.reflect.Field;
import java.util.Iterator;

public class JSONUtils {
	public static void apply(Object bean, JSONObject json) {
		Iterator it = json.keys();
		while (it.hasNext()) {
			String key = (String) it.next();
			Class c = bean.getClass();
			Field field = null;
			try {
				field = c.getDeclaredField(key);
			} catch (NoSuchFieldException e) {
				try {
					field = c.getField(key);
				} catch (Exception e1) {
				}
			}
			try {
				field.setAccessible(true);
				field.set(bean, json.opt(key));
			} catch (Exception e) {
			}
			continue;
		}
	}
}