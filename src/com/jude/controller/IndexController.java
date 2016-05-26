package com.jude.controller;

import com.jude.entity.User;
import com.jude.json.JSONArray;
import com.jude.service.UserService;
import com.jude.util.CachedParams;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@Autowired
	private UserService userService;
	private static final Logger log = LoggerFactory.getLogger(IndexController.class);

	@RequestMapping({ "/index.do" })
	public String forwardIndex(HttpServletRequest request, HttpServletResponse response) {
		User user = LoginInfo.getUser(request);
		if (user == null) {
			return "loginview.do";
		}
		return "main.do";
	}

	@RequestMapping({ "/loginview.do" })
	public String forwardLogin(HttpServletRequest request, HttpServletResponse response) {
		setConfig(request);
		return "login/index.jsp";
	}

	@RequestMapping({ "/login.do" })
	public String submitLogin(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		setConfig(request);
		if (request.getAttribute("msg") != null && !request.getAttribute("msg").equals("")) {
			return "login/index.jsp";
		}

		String name = request.getParameter("name");
		String password = request.getParameter("pwd");

		boolean b = this.userService.isUserExist(name);
		if (!b) {
			request.setAttribute("msg", "用户名不存在!");
			request.setAttribute("name", name);
			return "login/index.jsp";
		}

		User user = this.userService.getUser(name);
		String userPwd = user.getPassword();
		if (!password.equals(userPwd)) {
			setConfig(request);
			request.setAttribute("pwdmsg", "密码不正确!");
			request.setAttribute("name", name);
			return "login/index.jsp";
		}

		request.getSession().setAttribute("loginUser", user);
		System.out.println(log.isInfoEnabled());
		log.debug(user.getUsername() + " 登陆了系统");
		response.sendRedirect("index.do");
		return "main.do";
	}

	@RequestMapping({ "/main.do" })
	public String forwardMain(HttpServletRequest request, HttpServletResponse response) {
		setConfig(request);

		return "main/index.jsp";
	}

	@RequestMapping({ "/logout.do" })
	public void forwardLogout(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		request.getSession().invalidate();
		response.sendRedirect("loginview.do");
	}

	@RequestMapping({ "/portal.do" })
	public String portal(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		return "portal/index.jsp";
	}

	@RequestMapping({ "/menu.do" })
	public void menu(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.write(CachedParams.getMenus().toString(4));
		out.flush();
	}

	private void setConfig(HttpServletRequest request) {
		String version = "1.0";
		String appName = "客户拜访后台管理系统";
		String copyright = "使用firefox,chrome,IE10以上版本浏览器效果更佳";
		request.setAttribute("version", version);
		request.setAttribute("appName", appName);
		request.setAttribute("copyright", copyright);
	}
}