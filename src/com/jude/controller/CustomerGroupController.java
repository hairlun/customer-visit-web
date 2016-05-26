package com.jude.controller;

import com.jude.entity.CustomerGroup;
import com.jude.json.JSONArray;
import com.jude.json.JSONObject;
import com.jude.service.CustomerGroupService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "customerGroup.do" })
public class CustomerGroupController {
	public static final Logger log = LoggerFactory.getLogger(CustomerGroupController.class);

	@Autowired
	private CustomerGroupService customerGroupService;

	@RequestMapping(params = { "action=forwardIndex" })
	public String forwardIndex() {
		return "customerGroup/index.jsp";
	}

	@RequestMapping(params = { "action=queryAll" })
	public void queryAll(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> param = HttpUtils.getRequestParam(request);
			int start = NumberUtils.toInt((String) param.get("start"), 0);
			int limit = NumberUtils.toInt((String) param.get("limit"), 100);
			int all = NumberUtils.toInt((String) param.get("all"), 1);
			String sort = (String) param.get("sort");
			String dir = (String) param.get("dir");
			PagingSet<CustomerGroup> set = this.customerGroupService.getCustomerGroups(start, limit,
					sort, dir);

			JSONArray rows = new JSONArray();
			JSONObject main = new JSONObject();
			List<CustomerGroup> groupList = set.getList();
			JSONObject row;
			if (all == 1) {
				row = new JSONObject();
				row.put("id", Long.valueOf(-1L));
				row.put("name", "全部客户");
				rows.put(row);
			}
			for (CustomerGroup group : groupList) {
				row = new JSONObject();
				row.put("id", group.getId());
				row.put("name", group.getName());
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

	@RequestMapping(params = { "action=addGroup" })
	@ResponseBody
	public JSONObject addCustomerGroup(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String name = request.getParameter("name");
			if (this.customerGroupService.nameExist(name)) {
				return ExtJS.fail("分组已存在，请重新输入!");
			}

			CustomerGroup group = new CustomerGroup();
			group.setName(name);
			this.customerGroupService.addCustomerGroup(group);
			return ExtJS.ok("新增客户分组成功！");
		} catch (Exception e) {
			log.error("add customer ex", e);
		}
		return ExtJS.fail("新增客户分组失败，请刷新页面重试！");
	}

	@RequestMapping(params = { "action=editGroup" })
	@ResponseBody
	public JSONObject editCustomerGroup(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			int id = Integer.parseInt(request.getParameter("id"));
			String name = request.getParameter("name");
			String oname = request.getParameter("oname");
			if ((!name.equals(oname))
					&& (this.customerGroupService.nameExist(name))) {
				return ExtJS.fail("分组已存在，请重新输入!");
			}
			CustomerGroup group = this.customerGroupService.getCustomerGroup(id);
			if (group == null) {
				return ExtJS.fail("编辑客户分组失败，请刷新页面重试！");
			}
			group.setName(name);
			this.customerGroupService.updateCustomerGroup(group);
			return ExtJS.ok("修改成功！");
		} catch (Exception e) {
		}
		return ExtJS.fail("编辑客户分组失败，请刷新页面重试！");
	}

	@RequestMapping(params = { "action=delGroups" })
	@ResponseBody
	public JSONObject delCustomerGroups(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String ids = request.getParameter("ids");
			ids = ids.substring(1);
			this.customerGroupService.deleteCustomerGroups(ids);
		} catch (Exception e) {
			return ExtJS.fail("删除客户分组失败，请刷新页面后重试！");
		}
		return ExtJS.ok("删除客户分组成功！");
	}
}