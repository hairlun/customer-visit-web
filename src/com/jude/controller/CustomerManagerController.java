package com.jude.controller;

import com.jude.entity.CustomerManager;
import com.jude.json.JSONArray;
import com.jude.json.JSONObject;
import com.jude.service.CustomerManagerService;
import com.jude.util.ExtJS;
import com.jude.util.HttpUtils;
import com.jude.util.PagingSet;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "customerManager.do" })
public class CustomerManagerController {
	public static final Logger log = LoggerFactory.getLogger(CustomerManagerController.class);

	@Autowired
	private CustomerManagerService customerManagerService;

	@RequestMapping(params = { "action=forwardIndex" })
	public String forwardIndex() {
		return "customerManager/index.jsp";
	}

	@RequestMapping(params = { "action=queryAll" })
	public void queryAll(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map param = HttpUtils.getRequestParam(request);
			int start = NumberUtils.toInt((String) param.get("start"), 0);
			int limit = NumberUtils.toInt((String) param.get("limit"), 20);
			String sort = (String)param.get("sort");
			String dir = (String)param.get("dir");
			String department = (String)param.get("department");
			String area = (String)param.get("area");
			StringBuffer where = new StringBuffer("");
			if (StringUtils.hasLength(department)) {
			    where.append(" and department like '%" + department + "%'");
			}
			if (StringUtils.hasLength(area)) {
			    where.append(" and area like '%" + area + "%'");
			}
			PagingSet set = this.customerManagerService.getCustomerManagers(start, limit, sort, dir, where.toString());

			JSONArray rows = new JSONArray();
			JSONObject main = new JSONObject();
			List<CustomerManager> managerList = set.getList();
			for (CustomerManager manager : managerList) {
				JSONObject row = new JSONObject();
				row.put("id", manager.getId());
				row.put("name", manager.getName());
				row.put("username", manager.getUsername());
				row.put("password", manager.getPassword());
				row.put("department", manager.getDepartment());
				row.put("area", manager.getArea());
				rows.put(row);
			}
			main.put("rows", rows);
			main.put("total", set.getTotal());

			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.write(main.toString(4));
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(params = { "action=addManager" })
	@ResponseBody
	public JSONObject addCustomerManager(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String username = request.getParameter("username");
			if (this.customerManagerService.usernameExist(username)) {
				return ExtJS.fail("用户名已存在，请重新输入!");
			}

			CustomerManager manager = new CustomerManager();
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String department = request.getParameter("department");
			String area = request.getParameter("area");
			manager.setName(name);
			manager.setUsername(username);
			manager.setPassword(password);
			manager.setDepartment(department);
			manager.setArea(area);
			this.customerManagerService.addCustomerManager(manager);
			return ExtJS.ok("新增客户经理成功！");
		} catch (Exception e) {
			log.error("add customer ex", e);
		}
		return ExtJS.fail("新增客户经理失败，请刷新页面重试！");
	}

	@RequestMapping(params = { "action=editManager" })
	@ResponseBody
	public JSONObject editCustomerManager(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			int id = Integer.parseInt(request.getParameter("id"));
			String name = request.getParameter("name");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String ousername = request.getParameter("ousername");
			String department = request.getParameter("department");
			String area = request.getParameter("area");
			if ((!username.equals(ousername))
					&& (this.customerManagerService.usernameExist(username))) {
				return ExtJS.fail("用户名已存在，请重新输入!");
			}
			CustomerManager manager = this.customerManagerService.getCustomerManager(id);
			if (manager == null) {
				return ExtJS.fail("编辑客户经理失败，请刷新页面重试！");
			}
			manager.setName(name);
			manager.setUsername(username);
			manager.setPassword(password);
			manager.setDepartment(department);
			manager.setArea(area);
			this.customerManagerService.updateCustomerManager(manager);
			return ExtJS.ok("修改成功！");
		} catch (Exception e) {
		}
		return ExtJS.fail("编辑客户经理失败，请刷新页面重试！");
	}

	@RequestMapping(params = { "action=delManagers" })
	@ResponseBody
	public JSONObject delCustomerManagers(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String ids = request.getParameter("ids");
			ids = ids.substring(1);
			this.customerManagerService.deleteCustomerManagers(ids);
		} catch (Exception e) {
			return ExtJS.fail("删除客户经理失败，请刷新页面后重试！");
		}
		return ExtJS.ok("删除客户经理成功！");
	}
}