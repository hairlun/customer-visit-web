package com.jude.controller;

import com.jude.entity.Customer;
import com.jude.entity.InteractiveItem;
import com.jude.json.JSONArray;
import com.jude.json.JSONObject;
import com.jude.service.InteractiveItemService;
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
@RequestMapping({ "/interactiveItem.do" })
public class InteractiveItemController {
	public static final Logger log = LoggerFactory.getLogger(InteractiveItemController.class);

	@Autowired
	private InteractiveItemService interactiveItemService;

	@RequestMapping(params = { "action=forwardIndex" })
	public String forwardIndex() {
		return "interactiveItem/index.jsp";
	}

	@RequestMapping(params = { "action=queryAll" })
	public void queryAll(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> param = HttpUtils.getRequestParam(request);
			int start = NumberUtils.toInt((String) param.get("start"), 0);
			int limit = NumberUtils.toInt((String) param.get("limit"), 100);
			String sort = (String) param.get("sort");
			String dir = (String) param.get("dir");
			PagingSet<InteractiveItem> set = this.interactiveItemService.getInteractiveItems(start, limit,
					sort, dir);

			JSONArray rows = new JSONArray();
			JSONObject main = new JSONObject();
			List<InteractiveItem> interactiveItemList = set.getList();
			JSONObject row;
			for (InteractiveItem interactiveItem : interactiveItemList) {
				row = new JSONObject();
				row.put("id", interactiveItem.getId());
				row.put("name", interactiveItem.getName());
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

	@RequestMapping(params = { "action=addInteractiveItem" })
	@ResponseBody
	public JSONObject addInteractiveItem(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String name = request.getParameter("name");
			if (this.interactiveItemService.nameExist(name)) {
				return ExtJS.fail("交互事项已存在，请重新输入!");
			}

			InteractiveItem interactiveItem = new InteractiveItem();
			interactiveItem.setName(name);
			this.interactiveItemService.addInteractiveItem(interactiveItem);
			return ExtJS.ok("新增交互事项成功！");
		} catch (Exception e) {
			log.error("add interactiveItem ex", e);
		}
		return ExtJS.fail("新增交互事项失败，请刷新页面重试！");
	}

	@RequestMapping(params = { "action=editInteractiveItem" })
	@ResponseBody
	public JSONObject editInteractiveItem(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			int id = Integer.parseInt(request.getParameter("id"));
			String name = request.getParameter("name");
			String oname = request.getParameter("oname");
			if ((!name.equals(oname))
					&& (this.interactiveItemService.nameExist(name))) {
				return ExtJS.fail("交互事项已存在，请重新输入!");
			}
			InteractiveItem interactiveItem = this.interactiveItemService.getInteractiveItem(id);
			if (interactiveItem == null) {
				return ExtJS.fail("编辑交互事项失败，请刷新页面重试！");
			}
			interactiveItem.setName(name);
			this.interactiveItemService.updateInteractiveItem(interactiveItem);
			return ExtJS.ok("修改成功！");
		} catch (Exception e) {
		}
		return ExtJS.fail("编辑交互事项失败，请刷新页面重试！");
	}

	@RequestMapping(params = { "action=delInteractiveItems" })
	@ResponseBody
	public JSONObject delInteractiveItems(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String ids = request.getParameter("ids");
			ids = ids.substring(1);
			this.interactiveItemService.deleteInteractiveItems(ids);
		} catch (Exception e) {
			return ExtJS.fail("删除交互事项失败，请刷新页面后重试！");
		}
		return ExtJS.ok("删除交互事项成功！");
	}
}