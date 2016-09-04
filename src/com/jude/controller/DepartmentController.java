package com.jude.controller;

import com.jude.entity.Customer;
import com.jude.entity.Department;
import com.jude.json.JSONArray;
import com.jude.json.JSONObject;
import com.jude.service.DepartmentService;
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
@RequestMapping({ "/department.do" })
public class DepartmentController {
	public static final Logger log = LoggerFactory.getLogger(DepartmentController.class);

	@Autowired
	private DepartmentService departmentService;

	@RequestMapping(params = { "action=forwardIndex" })
	public String forwardIndex() {
		return "department/index.jsp";
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
			PagingSet<Department> set = this.departmentService.getDepartments(start, limit,
					sort, dir);

			JSONArray rows = new JSONArray();
			JSONObject main = new JSONObject();
			List<Department> departmentList = set.getList();
			JSONObject row;
			if (all == 1) {
				row = new JSONObject();
				row.put("id", Long.valueOf(-1L));
				row.put("name", "全部部门");
				rows.put(row);
			}
			for (Department department : departmentList) {
				row = new JSONObject();
				row.put("id", department.getId());
				row.put("name", department.getName());
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

	@RequestMapping(params = { "action=addDepartment" })
	@ResponseBody
	public JSONObject addDepartment(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String name = request.getParameter("name");
			if (this.departmentService.nameExist(name)) {
				return ExtJS.fail("部门已存在，请重新输入!");
			}

			Department department = new Department();
			department.setName(name);
			this.departmentService.addDepartment(department);
			return ExtJS.ok("新增部门成功！");
		} catch (Exception e) {
			log.error("add department ex", e);
		}
		return ExtJS.fail("新增部门失败，请刷新页面重试！");
	}

	@RequestMapping(params = { "action=editDepartment" })
	@ResponseBody
	public JSONObject editDepartment(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			int id = Integer.parseInt(request.getParameter("id"));
			String name = request.getParameter("name");
			String oname = request.getParameter("oname");
			if ((!name.equals(oname))
					&& (this.departmentService.nameExist(name))) {
				return ExtJS.fail("部门已存在，请重新输入!");
			}
			Department department = this.departmentService.getDepartment(id);
			if (department == null) {
				return ExtJS.fail("编辑部门失败，请刷新页面重试！");
			}
			department.setName(name);
			this.departmentService.updateDepartment(department);
			return ExtJS.ok("修改成功！");
		} catch (Exception e) {
		}
		return ExtJS.fail("编辑部门失败，请刷新页面重试！");
	}

	@RequestMapping(params = { "action=delDepartments" })
	@ResponseBody
	public JSONObject delDepartments(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String ids = request.getParameter("ids");
			ids = ids.substring(1);
			this.departmentService.deleteDepartments(ids);
		} catch (Exception e) {
			return ExtJS.fail("删除部门失败，请刷新页面后重试！");
		}
		return ExtJS.ok("删除部门成功！");
	}
	
	@RequestMapping(params = { "action=joinDepartment" })
	@ResponseBody
	public JSONObject joinDepartment(String mids, long did, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String[] ids = mids.split(",");
			for (int i = 1; i < ids.length; ++i) {
				long managerId = Long.parseLong(ids[i]);
				this.departmentService.joinDepartment(managerId, did);
			}
		} catch (Exception e) {
			return ExtJS.fail("移入失败，请刷新页面后重试！");
		}
		return ExtJS.ok("移入成功！");
	}
	
	@RequestMapping(params = { "action=exitDepartment" })
	@ResponseBody
	public JSONObject exitDepartment(String mids, long did, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String[] ids = mids.split(",");
			for (int i = 1; i < ids.length; ++i) {
				long managerId = Long.parseLong(ids[i]);
				this.departmentService.exitDepartment(managerId, did);
			}
		} catch (Exception e) {
			return ExtJS.fail("移出失败，请刷新页面后重试！");
		}
		return ExtJS.ok("移出成功！");
	}
}