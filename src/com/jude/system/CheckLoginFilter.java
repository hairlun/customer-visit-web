package com.jude.system;

import com.jude.entity.User;
import com.jude.json.JSONObject;
import com.jude.util.ExtJS;
import com.jude.util.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CheckLoginFilter implements Filter {
	private String redirectURL;
	private List<String> notCheckList;

	public CheckLoginFilter() {
		this.redirectURL = null;

		this.notCheckList = new ArrayList();
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.redirectURL = filterConfig.getInitParameter("redirectURL");
		String notCheckListStr = filterConfig.getInitParameter("notCheckList");
		if (notCheckListStr != null) {
			StringTokenizer st = new StringTokenizer(notCheckListStr, ",");
			this.notCheckList.clear();
			while (st.hasMoreTokens())
				this.notCheckList.add(st.nextToken());
		}
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String servletPath = request.getServletPath();
		boolean isCheckFile = (servletPath.endsWith(".jsp")) || (servletPath.endsWith(".do"))
				|| (servletPath.endsWith("Control"));
		if ((isCheckFile) && (!inNotCheckList(request)) && (!isLogin(request)))
			if (HttpUtils.isAsyncRequest(request)) {
				response.setStatus(408);

				JSONObject error = ExtJS.fail("会话已超时！").put("type", "sessiontimeout");
				HttpUtils.writeToResponse(response, error);
			} else {
				request.setAttribute("msg", "请先登录!");
				request.getRequestDispatcher(this.redirectURL).forward(request, response);
			}
		else
			filterChain.doFilter(request, response);
	}

	private boolean isLogin(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		return session != null && session.getAttribute("loginUser") != null;
	}

	private boolean inNotCheckList(HttpServletRequest request) {
		String uri = request.getServletPath()
				+ ((request.getPathInfo() == null) ? "" : request.getPathInfo());
		return this.notCheckList.contains(uri) || uri.indexOf("/mobile/") == 0;
	}

	public void destroy() {
		this.notCheckList.clear();
	}
}