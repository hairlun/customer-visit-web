package com.jude.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpUtils {
	private static Log log = LogFactory.getLog(HttpUtils.class);

	public static void printRequestInfo(HttpServletRequest request) {
		StringBuffer buf = new StringBuffer();
		buf.append("++++++++++++++++++++ print request info ++++++++++++++++++++++++++\n");
		buf.append(request.getRequestURL());
		String queryStr = request.getQueryString();
		if (queryStr != null) {
			buf.append("?").append(queryStr);
		}

		buf.append("\n");

		Map map = request.getParameterMap();
		Set<Map.Entry<String, Object>> mapEntrySet = map.entrySet();
		for (Map.Entry entry : mapEntrySet) {
			buf.append((String) entry.getKey());
			buf.append(" = ");
			buf.append(Arrays.asList((Object[]) entry.getValue()));
			buf.append("\n");
		}

		log.debug("\n" + buf);
	}

	public static Map<String, Object> getRequestParam(HttpServletRequest request) {
		Enumeration params = request.getParameterNames();
		Map result = new HashMap();
		while (params.hasMoreElements()) {
			String key = (String) params.nextElement();
			result.put(key, request.getParameter(key));
		}
		return result;
	}

	public static boolean isAsyncRequest(HttpServletRequest request) {
		return request.getHeader("X-Requested-With") != null;
	}

	public static void writeToResponse(HttpServletResponse response, Object object) {
		try {
			log.debug("write to response: " + object);

			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.write(String.valueOf(object));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}