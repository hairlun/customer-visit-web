package com.jude.controller;

import com.jude.entity.User;
import com.jude.json.JSONArray;
import com.jude.json.JSONObject;
import com.jude.service.UserService;
import com.jude.util.ExtJS;
import com.jude.util.HttpUtils;
import com.jude.util.PagingSet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/user.do" })
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(params = { "action=forwardIndex" })
	public String forwardIndex(HttpServletRequest request, HttpServletResponse response) {
		return "user/user.jsp";
	}

	@RequestMapping(params = { "action=queryAll" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST,
			org.springframework.web.bind.annotation.RequestMethod.GET })
	public void queryAll(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Map param = HttpUtils.getRequestParam(request);
		int start = NumberUtils.toInt((String) param.get("start"), 0);
		int limit = NumberUtils.toInt((String) param.get("limit"), 50);
		PagingSet set = this.userService.getUsers(start, limit);

		JSONArray rows = new JSONArray();
		JSONObject main = new JSONObject();
		List<User> userList = set.getList();
		for (User user : userList) {
			JSONObject row = new JSONObject();
			row.put("id", user.getId());
			row.put("username", user.getUsername());
			row.put("password", user.getPassword());
			row.put("fullname", user.getFullname());
			rows.put(row);
		}
		main.put("rows", rows);
		main.put("total", set.getTotal());

		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.write(main.toString(4));
		out.flush();
	}

	@RequestMapping(params = { "action=addUser" })
	@ResponseBody
	public JSONObject addUser(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			if (this.userService.isUserExist(request.getParameter("username").trim())) {
				return ExtJS.fail("用户名已存在，请重新输入！");
			}
			User user = new User();
			user.setUsername(request.getParameter("username").trim());
			user.setFullname(request.getParameter("fullname").trim());
			user.setPassword(request.getParameter("password"));
			this.userService.addUser(user);
		} catch (Exception e) {
			return ExtJS.fail("增加用户失败，请刷新页面后重试！");
		}
		return ExtJS.ok("新增用户成功！");
	}

	@RequestMapping(params = { "action=delUsers" })
	@ResponseBody
	public JSONObject delUser(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String ids = request.getParameter("ids");
			ids = ids.substring(1);
			String[] id = ids.split(",");
			for (String s : id)
				this.userService.deleteUser(Integer.parseInt(s));
		} catch (Exception e) {
			return ExtJS.fail("删除用户失败，请刷新页面后重试！");
		}
		return ExtJS.ok("删除用户成功！");
	}
}