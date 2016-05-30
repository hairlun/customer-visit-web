package com.jude.controller;

import com.jude.entity.Customer;
import com.jude.entity.CustomerManager;
import com.jude.entity.RecordDetail;
import com.jude.entity.Task;
import com.jude.entity.User;
import com.jude.json.JSONObject;
import com.jude.service.CustomerManagerService;
import com.jude.service.CustomerService;
import com.jude.service.TaskService;
import com.jude.util.ExtJS;
import com.jude.util.HttpUtils;
import com.jude.util.IdUtil;
import com.jude.util.PagingSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/task.do" })
public class TaskController {
	private static final Logger log = LoggerFactory.getLogger(TaskController.class);

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerManagerService managerService;

	@Autowired
	private TaskService taskService;
	private SimpleDateFormat sf;

	public TaskController() {
		this.sf = new SimpleDateFormat("yyyy年MM月dd日");
	}

	@RequestMapping(params = { "action=forwardIndex" })
	public String forwardIndex() {
		return "task/index.jsp";
	}

	@RequestMapping(params = { "action=normal" })
	@ResponseBody
	public JSONObject normalTask(HttpServletRequest request, HttpServletResponse response)
			throws ParseException {
		try {
			if ((LoginInfo.getUser(request) == null)
					|| (!"admin".equals(LoginInfo.getUser(request).getUsername()))) {
				return ExtJS.ok("非admin用户不能发放任务!");
			}

			Date start = this.sf.parse(request.getParameter("start"));
			Date end = this.sf.parse(request.getParameter("end"));
			if (start.after(end)) {
				return ExtJS.ok("起始时间不能大于任务完成时间！");
			}
			List tasks = new ArrayList();
			int idx = 0;
			for (Customer customer : this.customerService.getCustomers(0, 1000000).getList()) {
				if (customer.getCustomerManager() == null) {
					continue;
				}
				idx++;
				Task task = new Task();
				task.setContent(request.getParameter("content"));
				task.setCustomer(customer);
				task.setManager(customer.getCustomerManager());
				task.setStart(start);
				task.setEnd(end);
				task.setId(IdUtil.getId() + idx);
				tasks.add(task);
			}
			if (tasks.size() < 1) {
				return ExtJS.ok("无客户，请添加客户后重试！");
			}
			List cs = new ArrayList();
			Map map = HttpUtils.getRequestParam(request);
			Set<String> keySet = map.keySet();
			for (String s : keySet) {
				if ((s.matches("^content.+")) && (!s.equals("content"))) {
					RecordDetail detail = new RecordDetail();
					detail.setContent(map.get(s) + "");
					cs.add(detail);
				}
			}
			List subTasks = new ArrayList();
			subTasks.add(tasks.get(0));
			for (int i = 0; i < tasks.size();) {
				++i;
				if (i % 100 != 0) {
					if (i < tasks.size())
						subTasks.add(tasks.get(i));
				} else {
					this.taskService.addTask(subTasks, cs);
					subTasks.clear();
				}
				if (tasks.size() == i)
					;
				this.taskService.addTask(subTasks, cs);
			}

			return ExtJS.ok("发放成功!");
		} catch (Exception e) {
			log.error("error", e);
		}
		return ExtJS.ok("发放常规任务失败，请重试！");
	}

	@RequestMapping(params = { "action=indevi" })
	@ResponseBody
	public JSONObject indeviTask(String mids, String content, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if ((LoginInfo.getUser(request) == null)
					|| (!"admin".equals(LoginInfo.getUser(request).getUsername()))) {
				return ExtJS.ok("非admin用户不能发放任务!");
			}

			mids = mids.substring(1, mids.length());

			Date start = this.sf.parse(request.getParameter("start"));
			Date end = this.sf.parse(request.getParameter("end"));
			if (start.after(end)) {
				return ExtJS.ok("起始时间不能大于任务完成时间！");
			}

			List<Customer> list = this.customerService.getCustomersByManagerIds(mids);
			List tasks = new ArrayList();
			int idx = 0;
			for (Customer customer : list) {
				if (customer.getCustomerManager() == null) {
					continue;
				}
				idx++;
				Task task = new Task();
				task.setContent(request.getParameter("content"));
				task.setCustomer(customer);
				task.setManager(customer.getCustomerManager());
				task.setStart(start);
				task.setEnd(end);
				task.setId(IdUtil.getId() + idx);
				tasks.add(task);
			}

			if (tasks.size() < 1) {
				return ExtJS.ok("无客户，请添加客户后重试！");
			}
			List cs = new ArrayList();
			Map map = HttpUtils.getRequestParam(request);
			Set<String> keySet = map.keySet();
			for (String s : keySet) {
				if ((s.matches("^content.+")) && (!s.equals("content"))) {
					RecordDetail detail = new RecordDetail();
					detail.setContent(map.get(s) + "");
					cs.add(detail);
				}
			}
			this.taskService.addTask(tasks, cs);
			return ExtJS.ok("发放成功！");
		} catch (Exception e) {
			log.error("error", e);
		}
		return ExtJS.fail("发放个别任务失败，请重试！");
	}

	@RequestMapping(params = { "action=customerTask" })
	@ResponseBody
	public JSONObject customerTask(String cids, String content, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if ((LoginInfo.getUser(request) == null)
					|| (!"admin".equals(LoginInfo.getUser(request).getUsername()))) {
				return ExtJS.ok("非admin用户不能发放任务!");
			}

			cids = cids.substring(1, cids.length());

			Date start = this.sf.parse(request.getParameter("start"));
			Date end = this.sf.parse(request.getParameter("end"));
			if (start.after(end)) {
				return ExtJS.ok("起始时间不能大于任务完成时间！");
			}

			List<Customer> list = this.customerService.getCustomersByIds(cids);
			List tasks = new ArrayList();
			int idx = 0;
			for (Customer customer : list) {
				if (customer.getCustomerManager() == null) {
					continue;
				}
				idx++;
				Task task = new Task();
				task.setContent(request.getParameter("content"));
				task.setCustomer(customer);
				task.setManager(customer.getCustomerManager());
				task.setStart(start);
				task.setEnd(end);
				task.setId(IdUtil.getId() + idx);
				tasks.add(task);
			}

			if (tasks.size() < 1) {
				return ExtJS.ok("无客户，请添加客户后重试！");
			}
			List cs = new ArrayList();
			Map map = HttpUtils.getRequestParam(request);
			Set<String> keySet = map.keySet();
			for (String s : keySet) {
				if ((s.matches("^content.+")) && (!s.equals("content"))) {
					RecordDetail detail = new RecordDetail();
					detail.setContent(map.get(s) + "");
					cs.add(detail);
				}
			}
			this.taskService.addTask(tasks, cs);
			return ExtJS.ok("发放成功数量：" + tasks.size() + "， 失败数量：" + (list.size() - tasks.size()));
		} catch (Exception e) {
			log.error("error", e);
		}
		return ExtJS.fail("发放个别任务失败，请重试！");
	}

	@RequestMapping(params = { "action=newCustomerTask" })
	@ResponseBody
	public JSONObject newCustomerTask(String cids, String mid, String content, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if ((LoginInfo.getUser(request) == null)
					|| (!"admin".equals(LoginInfo.getUser(request).getUsername()))) {
				return ExtJS.ok("非admin用户不能发放任务!");
			}

			cids = cids.substring(1, cids.length());

			Date start = this.sf.parse(request.getParameter("start"));
			Date end = this.sf.parse(request.getParameter("end"));
			if (start.after(end)) {
				return ExtJS.ok("起始时间不能大于任务完成时间！");
			}

			String[] cIdArr = cids.split(",");
			int size = cIdArr.length;
			List tasks = new ArrayList();
			for (int idx = 1; idx <= size; idx++) {
				Task task = new Task();
				task.setContent(request.getParameter("content"));
				Customer customer = new Customer();
				customer.setId(Long.parseLong(cIdArr[idx - 1]));
				task.setCustomer(customer);
				CustomerManager manager = new CustomerManager();
				manager.setId(Long.parseLong(mid));
				task.setManager(manager);
				task.setStart(start);
				task.setEnd(end);
				task.setId(IdUtil.getId() + idx);
				tasks.add(task);
			}

			List cs = new ArrayList();
			Map map = HttpUtils.getRequestParam(request);
			Set<String> keySet = map.keySet();
			for (String s : keySet) {
				if ((s.matches("^content.+")) && (!s.equals("content"))) {
					RecordDetail detail = new RecordDetail();
					detail.setContent(map.get(s) + "");
					cs.add(detail);
				}
			}
			this.taskService.addTask(tasks, cs);
			return ExtJS.ok("发放成功！");
		} catch (Exception e) {
			log.error("error", e);
		}
		return ExtJS.fail("发放新客户任务失败，请重试！");
	}
}