package com.jude.controller;

import com.jude.entity.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginInfo {
	public static User getUser(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}

		User loginUser = (User) session.getAttribute("loginUser");
		return loginUser;
	}

	public static boolean isAdmin(HttpServletRequest request) {
		User user = getUser(request);

		return (user != null) && ("admin".equals(user.getUsername()));
	}
}