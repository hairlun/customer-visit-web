package com.jude.controller;

import com.jude.entity.Customer;
import com.jude.entity.CustomerManager;
import com.jude.entity.Task;
import com.jude.entity.Workflow;
import com.jude.json.JSONArray;
import com.jude.json.JSONObject;
import com.jude.service.CustomerManagerService;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
            "yyyy-MM-dd hh:mm:ss");

    @Autowired
    private WorkflowService workflowService;

    @RequestMapping(params = { "action=newWorkflow" })
    public String newWorkflow() {
        return "workflow/new_workflow.jsp";
    }

    @RequestMapping(params = { "action=dealWorkflow" })
    public String dealWorkflow() {
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
            if (StringUtils.hasLength((String) param.get("address"))) {
                where.append(" and w.address like '%").append(
                        (String) param.get("address") + "%'");
            }
            if (StringUtils.hasLength((String) param.get("sellNumber"))) {
                where.append(" and c.sell_number like '%").append(
                        (String) param.get("sellNumber") + "%'");
            }
            if (StringUtils.hasLength((String) param.get("receiveTime"))) {
                try {
                    String ss = (String) param.get("receiveTime");
                    where.append(" and receive_time > '")
                            .append(ss.substring(0, 10)).append("'");
                } catch (Exception e) {
                }
            }
            if (StringUtils.hasLength((String) param.get("problemFinder"))) {
                where.append(" and pf.name like '%").append(
                        (String) param.get("problemFinder") + "%'");
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
                row.put("response_network", workflow.getResponseNetwork());
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
                    if (workflow.getCustomer().getCustomerManager().getName() != null) {
                        row.put("cmid", workflow.getCustomer()
                                .getCustomerManager().getId());
                        row.put("cmname", workflow.getCustomer()
                                .getCustomerManager().getName());
                        row.put("cmusername", workflow.getCustomer()
                                .getCustomerManager().getUsername());
                    } else {
                        row.put("cmid", "");
                        row.put("cmname", "");
                        row.put("cmusername", "");
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
                }
                if (workflow.getProblemFinder().getName() != null) {
                    row.put("pfid", workflow.getProblemFinder().getId());
                    row.put("pfname", workflow.getProblemFinder().getName());
                    row.put("pfusername", workflow.getProblemFinder()
                            .getUsername());
                    if (workflow.getProblemFinder().getDepartment().getName() != null) {
                        row.put("pfdid", workflow.getProblemFinder()
                                .getDepartment().getId());
                        row.put("pfdname", workflow.getProblemFinder()
                                .getDepartment().getName());
                        row.put("problem_finder", workflow.getProblemFinder()
                                .getName()
                                + "("
                                + workflow.getProblemFinder().getDepartment()
                                        .getName() + ")");
                    } else {
                        row.put("pfdid", "");
                        row.put("pfdname", "");
                        row.put("problem_finder", workflow.getProblemFinder()
                                .getName());
                    }
                } else {
                    row.put("pfid", "");
                    row.put("pfname", "");
                    row.put("pfusername", "");
                    row.put("pfdid", "");
                    row.put("pfdname", "");
                    row.put("problem_finder", "");
                }
                if (workflow.getHandler().getName() != null) {
                    row.put("hid", workflow.getHandler().getId());
                    row.put("hname", workflow.getHandler().getName());
                    row.put("husername", workflow.getHandler().getUsername());
                    if (workflow.getHandler().getDepartment().getName() != null) {
                        row.put("hdid", workflow.getHandler().getDepartment()
                                .getId());
                        row.put("hdname", workflow.getHandler().getDepartment()
                                .getName());
                        row.put("handler", workflow.getHandler().getName()
                                + "("
                                + workflow.getHandler().getDepartment()
                                        .getName() + ")");
                    } else {
                        row.put("hdid", "");
                        row.put("hdname", "");
                        row.put("handler", workflow.getHandler().getName());
                    }
                } else {
                    row.put("hid", "");
                    row.put("hname", "");
                    row.put("husername", "");
                    row.put("hdid", "");
                    row.put("hdname", "");
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
}