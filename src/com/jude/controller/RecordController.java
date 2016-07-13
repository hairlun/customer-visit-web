package com.jude.controller;

import com.jude.entity.Customer;
import com.jude.entity.CustomerManager;
import com.jude.entity.Task;
import com.jude.entity.VisitRecord;
import com.jude.json.JSONArray;
import com.jude.json.JSONObject;
import com.jude.service.CustomerManagerService;
import com.jude.service.TaskService;
import com.jude.service.VisitRecordService;
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
@RequestMapping({ "/record.do" })
public class RecordController {
	private static final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	@Autowired
	private VisitRecordService visitRecordService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private CustomerManagerService customerManagerService;

	@RequestMapping(params = { "action=forwardIndex" })
	public String forwardIndex() {
		return "record/index.jsp";
	}

	@RequestMapping(params = { "action=queryAll" })
	public void queryList(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map param = HttpUtils.getRequestParam(request);
			int start = NumberUtils.toInt((String) param.get("start"), 0);
			int limit = NumberUtils.toInt((String) param.get("limit"), 20);
			String sort = (String)param.get("sort");
			String dir = (String)param.get("dir");
			StringBuffer where = new StringBuffer("");

			if (StringUtils.hasLength((String) (String) param.get("customer"))) {
				where.append(" and c.name like '%").append((String) param.get("customer") + "%'");
			}
			if (StringUtils.hasLength((String) (String) param.get("manager"))) {
				where.append(" and m.name like '%").append((String) param.get("manager") + "%'");
			}
			if (StringUtils.hasLength((String) (String) param.get("startTime")))
				try {
					String ss = (String) param.get("startTime");
					where.append(" and visit_time > '").append(ss.substring(0, 10)).append("'");
				} catch (Exception e) {
				}
			if (StringUtils.hasLength((String) (String) param.get("endTime")))
				try {
					String ss = (String) param.get("endTime");
					where.append(" and visit_time < '").append(ss.substring(0, 10)).append("'");
				} catch (Exception e) {
				}
			PagingSet set = this.visitRecordService.queryVisitRecords(start, limit,
					where.toString(), sort, dir);

			JSONArray rows = new JSONArray();
			JSONObject main = new JSONObject();
			List<VisitRecord> recordList = set.getList();
			for (VisitRecord record : recordList) {
				JSONObject row = new JSONObject();
				row.put("id", record.getId());
				row.put("cname", (record.getCustomer() == null) ? "" : record.getCustomer()
						.getName());
				row.put("mname", (record.getCustomerManager() == null) ? "" : record
						.getCustomerManager().getName());
				row.put("content", record.getContent());
				row.put("reject", record.getReject() == 1 ? "驳回" : "");
				row.put("type", (record.getType() == 1) ? "签到未签退" : (record.getType() == 0) ? "未签到"
						: "已签退");
				row.put("result_code", (record.getResultCode() == 0) ? "未评价" : "满意");
				row.put("city", record.getCity());
				String cgps = record.getCustomer() == null ? null : record.getCustomer().getGps();
				String rgps = record.getGps();
				row.put("gps", rgps);
				double cLatF = 0;
				double cLngF = 0;
				double rLatF = 0;
				double rLngF = 0;
				int gpsDist = 0;
				if (cgps != null && !cgps.equals("")) {
					cLatF = Double.parseDouble(cgps.substring(cgps.indexOf(",") + 1));
					cLngF = Double.parseDouble(cgps.substring(0, cgps.indexOf(",")));
					if (rgps != null && !rgps.equals("")) {
						rLatF = Double.parseDouble(rgps.substring(rgps.indexOf(",") + 1));
						rLngF = Double.parseDouble(rgps.substring(0, rgps.indexOf(",")));
						double lngDist = Math.abs(rLngF - cLngF) * 1110000;
						double latDist = Math.abs(rLatF - cLatF) * 1110000 * Math.cos(Math.PI * cLatF / 180);
						gpsDist = (int) Math.sqrt(lngDist * lngDist + latDist * latDist);
					}
				}
				if (rgps == null || rgps.equals("")) {
					row.put("gps_dist", "");
				} else if (gpsDist == 0) {
					row.put("gps_dist", "0米");
				} else {
					row.put("gps_dist",  "" + gpsDist / 10 + (gpsDist % 10 != 0 ? "." + gpsDist % 10 : "") + "米");
				}
				if (gpsDist > 2000) {
					row.put("gps_flag", "1");
				} else {
					row.put("gps_flag", "0");
				}
				if (record.getVisitTime() != null) {
					row.put("visit_time", sf.format(record.getVisitTime()));
				}
				if (record.getLeaveTime() != null) {
					row.put("leave_time", sf.format(record.getLeaveTime()));
				}
				if (record.getCostTime() != 0) {
					row.put("cost", record.getCostTime());
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

	@RequestMapping(params = { "action=delRecords" })
	@ResponseBody
	public JSONObject delRecords(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String ids = request.getParameter("ids");
			ids = ids.substring(1);
			List<Task> tasks = this.taskService.getTasksByVisitRecordIds(ids);
			for (Task task : tasks) {
				this.taskService.delete(task.getId());
			}
			this.visitRecordService.deleteVisitRecord(ids,
					request.getServletContext().getRealPath("/recordImages"));
		} catch (Exception e) {
			return ExtJS.fail("删除记录失败，请刷新页面后重试！");
		}
		return ExtJS.ok("删除记录成功！");
	}
	
	@RequestMapping(params = { "action=rejectRecord" })
	@ResponseBody
	public JSONObject reject(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String ids = request.getParameter("ids");
			ids = ids.substring(1);
			String reason = request.getParameter("reason");
			List<VisitRecord> visitRecords = this.visitRecordService.queryVisitRecordsByIds(ids);
			StringBuffer sb = new StringBuffer("");
			for (VisitRecord record : visitRecords) {
				sb.append(",").append(record.getTaskId());
				record.setType(0);
				this.visitRecordService.update(record);
			}
			this.taskService.reject(sb.substring(1), reason);
		} catch (Exception e) {
			return ExtJS.fail("驳回失败，请刷新页面后重试！");
		}
		return ExtJS.ok("驳回成功！");
	}

	@RequestMapping(params = { "action=getImages" })
	public void getImages(String recordId, HttpServletRequest request, HttpServletResponse response) {
		try {
			JSONObject json = new JSONObject();
			JSONArray rows = new JSONArray();
			VisitRecord record = this.visitRecordService.getVisitRecord(Long.parseLong(recordId));
			if (record != null) {
				for (int i = 0; i < 6; ++i) {
					JSONObject jo = new JSONObject();
					String path = request.getServletContext().getRealPath("/recordImages")
							+ File.separator + recordId + "_" + i + ".jpg";
					File f = new File(path);
					if (f.exists()) {
						jo.put("title", i);
						jo.put("url", request.getContextPath() + File.separator
								+ "record.do?action=getImage&path=" + path);
						rows.put(jo);
					}
				}
			}
			System.out.println(request.getContextPath());
			json.put("rows", rows);

			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.write(json.toString(4));
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(params = { "action=getImage" })
	public void getImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String path = request.getParameter("path");
		File file = new File(path);
		if (!file.exists()) {
			file = new File(request.getServletContext().getRealPath("/recordImages")
					+ File.separator + path);
		}
		InputStream is = new FileInputStream(file);
		IOUtils.copy(is, response.getOutputStream());
	}
	
	@RequestMapping(params = { "action=zip" })
	@ResponseBody
	public JSONObject zipPhotos(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String path = request.getServletContext().getRealPath("/recordImages") + File.separator;
			String res = zip(null, null, path);
			if ("error".equals(res)) {
				return ExtJS.fail("打包失败!");
			}
			return ExtJS.ok(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ExtJS.fail("打包失败！");
	}
	
	public String zip(Map<String, Object> param, String name, String path) {
		byte[] buf = new byte[1024];
		name = "zipfile";
		List<CustomerManager> customerManagers = this.customerManagerService.getCustomerManagers(0, 10000, null, null).getList();
		File zipFile = new File(System.getProperty("java.io.tmpdir"), name + ".zip");
		try {
			ZipOutputStream zos = new ZipOutputStream(zipFile);
			zos.setEncoding("GBK");
			for (CustomerManager customerManager : customerManagers) {
				String outPath = customerManager.getName() + "/";
				zos.putNextEntry(new ZipEntry(outPath));
				List<VisitRecord> list = this.visitRecordService.queryVisitRecords(0, 100000, " and m.id = " + customerManager.getId() + " ", null, null).getList();
				for (VisitRecord visitRecord : list) {
					for (int i = 0; i < 6; i++) {
						File file = new File(path + visitRecord.getId() + "_" + i + ".jpg");
						if (file.exists()) {
							zos.putNextEntry(new ZipEntry(outPath + visitRecord.getId() + "_" + i + ".jpg"));
							FileInputStream in = new FileInputStream(file);
							int len;
							while ((len = in.read(buf)) > 0) {
								zos.write(buf, 0, len);
							}
							in.close();
							zos.closeEntry();
						} else {
							break;
						}
					}
				}
			}
			zos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return zipFile.length() > 0L ? zipFile.getAbsolutePath() : "error";
	}
}