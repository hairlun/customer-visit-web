package com.jude.controller;

import com.jude.entity.CustomerManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginInfo {
	public static CustomerManager getUser(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}

		CustomerManager loginUser = (CustomerManager) session.getAttribute("loginUser");
		return loginUser;
	}

	public static boolean isAdmin(HttpServletRequest request) {
		CustomerManager user = getUser(request);

		return (user != null) && ("admin".equals(user.getUsername()));
	}
}