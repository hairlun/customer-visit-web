package com.jude.controller;

import com.jude.entity.Customer;
import com.jude.entity.CustomerManager;
import com.jude.entity.Task;
import com.jude.entity.Workflow;
import com.jude.json.JSONArray;
import com.jude.json.JSONObject;
import com.jude.service.CustomerManagerService;
import com.jude.service.CustomerManagerServiceImpl;
import com.jude.service.CustomerService;
import com.jude.service.TaskService;
import com.jude.service.WorkflowService;
import com.jude.util.ExcelColumn;
import com.jude.util.ExcelExporter;
import com.jude.util.ExtJS;
import com.jude.util.HttpUtils;
import com.jude.util.PagingSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/workflow.do" })
public class WorkflowController {
    private static final SimpleDateFormat sf = new SimpleDateFormat(
            "yyyy-MM-dd hh:mm");

    @Autowired
    private WorkflowService workflowService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private CustomerManagerService customerManagerService;

    @RequestMapping(params = { "action=newWorkflow" })
    public String newWorkflow(HttpServletRequest request, HttpServletResponse response) {
    	request.getSession().setAttribute("loginUser", CustomerManagerServiceImpl.getLoginUser());
        return "workflow/new_workflow.jsp";
    }

    @RequestMapping(params = { "action=dealWorkflow" })
    public String dealWorkflow(HttpServletRequest request, HttpServletResponse response) {
    	request.getSession().setAttribute("loginUser", CustomerManagerServiceImpl.getLoginUser());
        return "workflow/deal_workflow.jsp";
    }

    @RequestMapping(params = { "action=viewWorkflow" })
    public String viewWorkflow() {
        return "workflow/view_workflow.jsp";
    }

    @RequestMapping(params = { "action=queryAll" })
    public void queryList(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Map param = HttpUtils.getRequestParam(request);
            int start = NumberUtils.toInt((String) param.get("start"), 0);
            int limit = NumberUtils.toInt((String) param.get("limit"), 20);
            String sort = (String) param.get("sort");
            String dir = (String) param.get("dir");
            StringBuffer where = new StringBuffer("");

            if (StringUtils.hasLength((String) param.get("customer"))) {
                where.append(" and c.name like '%").append(
                        (String) param.get("customer") + "%'");
            }
            if (StringUtils.hasLength((String) param.get("problemFinder"))) {
                where.append(" and pf.name like '%").append(
                        (String) param.get("problemFinder") + "%'");
            }
            if (StringUtils.hasLength((String) param.get("startTime"))) {
                try {
                    String ss = (String) param.get("startTime");
                    where.append(" and ( receive_time >= '")
                            .append(ss.substring(0, 10)).append("' or handle_time >= '")
                            .append(ss.substring(0, 10)).append("' or solved_time >= '")
                            .append(ss.substring(0, 10)).append("' )");
                } catch (Exception e) {
                }
            }
            if (StringUtils.hasLength((String) param.get("endTime"))) {
                try {
                    String ss = (String) param.get("endTime");
                    where.append(" and ( receive_time <= '")
                            .append(ss.substring(0, 10)).append("' or handle_time <= '")
                            .append(ss.substring(0, 10)).append("' or solved_time <= '")
                            .append(ss.substring(0, 10)).append("' )");
                } catch (Exception e) {
                }
            }
            PagingSet set = this.workflowService.queryWorkflows(start, limit,
                    where.toString(), sort, dir);

            JSONArray rows = new JSONArray();
            JSONObject main = new JSONObject();
            List<Workflow> workflowList = set.getList();
            for (Workflow workflow : workflowList) {
                JSONObject row = new JSONObject();
                row.put("id", workflow.getId());
                row.put("address", workflow.getAddress());
                row.put("receive_time", sf.format(workflow.getReceiveTime()));
                row.put("handle_time", sf.format(workflow.getHandleTime()));
                row.put("solved_time", sf.format(workflow.getSolvedTime()));
                row.put("description", workflow.getDescription());
                row.put("remark", workflow.getRemark());
                if (workflow.getCustomer().getName() != null) {
                    row.put("cid", workflow.getCustomer().getId());
                    row.put("cname", workflow.getCustomer().getName());
                    row.put("cnumber", workflow.getCustomer().getNumber());
                    row.put("csell_number", workflow.getCustomer()
                            .getSellNumber());
                    row.put("cstore_name", workflow.getCustomer()
                            .getStoreName());
                    row.put("cphone_number", workflow.getCustomer()
                            .getPhoneNumber());
                    row.put("cbackup_number", workflow.getCustomer()
                            .getBackupNumber());
                    row.put("caddress", workflow.getCustomer().getAddress());
                    row.put("cgps", workflow.getCustomer().getGps());
                    CustomerManager cm = workflow.getCustomer().getCustomerManager();
                    if (cm.getName() != null) {
                        row.put("cmid", cm.getId());
                        row.put("cmname", cm.getName());
                        row.put("cmusername", cm.getUsername());
                        row.put("cmdepartment", cm.getDepartment());
                        row.put("cmarea", cm.getArea());
                    } else {
                        row.put("cmid", "");
                        row.put("cmname", "");
                        row.put("cmusername", "");
                        row.put("cmdepartment", "");
                        row.put("cmarea", "");
                    }
                } else {
                    row.put("cid", "");
                    row.put("cname", "");
                    row.put("cnumber", "");
                    row.put("csell_number", "");
                    row.put("cstore_name", "");
                    row.put("cphone_number", "");
                    row.put("cbackup_number", "");
                    row.put("caddress", "");
                    row.put("cgps", "");
                    row.put("cmid", "");
                    row.put("cmname", "");
                    row.put("cmusername", "");
                    row.put("cmdepartment", "");
                    row.put("cmarea", "");
                }
                CustomerManager pf = workflow.getProblemFinder();
                if (pf.getName() != null) {
                    row.put("pfid", pf.getId());
                    row.put("pfname", pf.getName());
                    row.put("pfusername", pf.getUsername());
                    row.put("pfdepartment", pf.getDepartment());
                    row.put("pfarea", pf.getArea());
                    StringBuffer problemFinder = new StringBuffer(pf.getName());
                    if (StringUtils.hasLength(pf.getDepartment())) {
                        problemFinder.append("-" + pf.getDepartment());
                    }
                    if (StringUtils.hasLength(pf.getArea())) {
                        problemFinder.append("-" + pf.getArea());
                    }
                    row.put("problem_finder", problemFinder.toString());
                } else {
                    row.put("pfid", "");
                    row.put("pfname", "");
                    row.put("pfusername", "");
                    row.put("pfdepartment", "");
                    row.put("pfarea", "");
                    row.put("problem_finder", "");
                }
                CustomerManager h = workflow.getHandler();
                if (h.getName() != null) {
                    row.put("hid", h.getId());
                    row.put("hname", h.getName());
                    row.put("husername", h.getUsername());
                    row.put("hdepartment", h.getDepartment());
                    row.put("harea", h.getArea());
                    StringBuffer handler = new StringBuffer(h.getName());
                    if (StringUtils.hasLength(h.getDepartment())) {
                        handler.append("-" + h.getDepartment());
                    }
                    if (StringUtils.hasLength(h.getArea())) {
                        handler.append("-" + h.getArea());
                    }
                    row.put("handler", handler.toString());
                } else {
                    row.put("hid", "");
                    row.put("hname", "");
                    row.put("husername", "");
                    row.put("hdepartment", "");
                    row.put("harea", "");
                    row.put("handler", "");
                }
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

	@RequestMapping(params = { "action=delWorkflows" })
	@ResponseBody
	public JSONObject delWorkflows(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String ids = request.getParameter("ids");
			ids = ids.substring(1);
			this.workflowService.deleteWorkflows(ids);
		} catch (Exception e) {
			return ExtJS.fail("删除任务失败，请刷新页面后重试！");
		}
		return ExtJS.ok("删除任务成功！");
	}

    @RequestMapping(params = { "action=newWorkflowSubmit" })
    @ResponseBody
    public JSONObject newWorkflowSubmit(HttpServletRequest request, HttpServletResponse response) {
        long customerId = Long.parseLong(request.getParameter("customer"));
        Customer customer = customerService.getCustomer((int)customerId);
        String address = request.getParameter("address");
        String sellNumber = request.getParameter("sellNumber");
        Date receiveTime = null;
        try {
			receiveTime = sf.parse(request.getParameter("receiveTime"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        CustomerManager problemFinder = customerManagerService.getCustomerManager(LoginInfo.getUser(request).getUsername());
        Date handleTime = new Date();
        String description = request.getParameter("description");
        String remark = request.getParameter("remark");
        Workflow workflow = new Workflow();
        workflow.setCustomer(customer);
        workflow.setAddress(address);
        workflow.setReceiveTime(receiveTime);
        workflow.setProblemFinder(problemFinder);
        workflow.setHandleTime(handleTime);
        workflow.setDescription(description);
        workflow.setRemark(remark);
        workflowService.addWorkflow(workflow);
        
        return ExtJS.ok("发起任务成功！");
    }
}