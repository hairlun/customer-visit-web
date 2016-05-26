package com.jude.controller;

import com.jude.entity.Customer;
import com.jude.entity.CustomerManager;
import com.jude.entity.ModelSwitch;
import com.jude.entity.RecordDetail;
import com.jude.entity.Task;
import com.jude.entity.VisitRecord;
import com.jude.json.JSONArray;
import com.jude.json.JSONObject;
import com.jude.service.CustomerManagerService;
import com.jude.service.CustomerService;
import com.jude.service.SwitchService;
import com.jude.service.TaskService;
import com.jude.service.VisitRecordService;
import com.jude.util.MD5;
import com.jude.util.PagingSet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping({ "/mobile/*" })
public class MobileController {

	@Autowired
	private CustomerManagerService managerService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private VisitRecordService recordService;

	@Autowired
	private SwitchService switchService;
	private static final SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddhhmmss");
	private static final SimpleDateFormat DATETIME_FMT = new SimpleDateFormat("yyyy-MM-dd hh:mm");

	@RequestMapping({ "login" })
	public void login(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			if (!this.managerService.usernameExist(username)) {
				json.put("retcode", "000003");
				json.put("retinfo", "用户名或密码错误");
				write(response, json);
				return;
			}

			CustomerManager manager = this.managerService.getCustomerManager(username);
			if (!MD5.encrypt(password).equals(manager.getPassword())) {
				json.put("retcode", "000003");
				json.put("retinfo", "用户名或密码错误");
				write(response, json);
				return;
			}

			json.put("retcode", "000000");
			json.put("retinfo", "success");
			json.put("userId", manager.getId());
		} catch (Exception e) {
			e.printStackTrace();
			json.put("retcode", "000001");
			json.put("retinfo", "exception");
		}
		write(response, json);
	}

	@RequestMapping({ "codeSign" })
	public void codeSign(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			if (isSwitchOpen().getQrcode().equals("0")) {
				json.put("retcode", "000003");
				json.put("retinfo", "二维码开关未开！");
				write(response, json);
				return;
			}
			String flag = request.getParameter("flag");
			String userId = request.getParameter("userId");
			String serviceId = request.getParameter("serviceId");
			if ((StringUtils.isBlank(flag)) || (StringUtils.isBlank(userId))
					|| (StringUtils.isBlank(serviceId))) {
				json.put("retcode", "000003");
				json.put("retinfo", "请求参数不正确！");
				write(response, json);
				return;
			}
			
			String taskId = request.getParameter("serviceId");
			VisitRecord record = this.recordService.getVisitRecordByTaskId(taskId);
			Customer customer = record.getCustomer();
			String code = request.getParameter("code");
			if (code != null) {
				code = code.trim();
			}
			if (code == null ||
					!(code.equals("满意") ||
					(customer.getNumber() != null && code.equals(customer.getNumber())) ||
					(customer.getSellNumber() != null && code.equals(customer.getSellNumber())))) {
				json.put("retcode", "000003");
				json.put("retinfo", "二维码不正确！");
				write(response, json);
				return;
			}

			if ("0".equals(flag)) {
				// 签到
				String city = request.getParameter("city");
				String lat = request.getParameter("lat");
				String lng = request.getParameter("lng");
				double latF = Double.parseDouble(lat);
				double lngF = Double.parseDouble(lng);
				String gps = customer.getGps();
				double cLatF = 0;
				double cLngF = 0;
				if (gps != null && !gps.equals("")) {
					cLatF = Double.parseDouble(gps.substring(gps.indexOf(",") + 1));
					cLngF = Double.parseDouble(gps.substring(0, gps.indexOf(",")));
					if (lng == null || lat == null ||
							!(Math.abs(latF - cLatF) < 0.002 && Math.abs(lngF - cLngF) < 0.002)) {
						json.put("retcode", "000003");
						json.put("retinfo", "没有定位信息或定位不正确！");
						write(response, json);
						return;
					}
				} else {
					// 客户没有GPS信息，添加第一次签到的位置为客户的GPS位置
					if (lng != null && lat != null && lngF != 0 && latF != 0) {
						customer.setGps(lng + "," + lat);
						this.customerService.updateGps(customer.getGps(), customer.getId());
					}
				}
				record.setCity(city);
				if (lng != null && lat != null) {
					record.setGps(lng + "," + lat);
				}
				record.setType(1);
				record.setVisitTime(new Date());
				this.recordService.update(record);
				this.customerService.updateLastVisitTime(new Date(), customer.getId());
			} else if ("1".equals(flag)) {
				// 签退
				Long s = Long.valueOf(new Date().getTime() - record.getVisitTime().getTime());
				record.setCostTime((int) (s.longValue() / 60000L));
				record.setLeaveTime(new Date());
				record.setType(2);
				this.recordService.update(record);
				this.taskService.complete(taskId);
			}
			json.put("retcode", "000000");
			json.put("retinfo", "success");
		} catch (Exception e) {
			e.printStackTrace();
			json.put("retcode", "000001");
			json.put("retinfo", "系统异常");
		}
		write(response, json);
	}

	@RequestMapping({ "queryServiceContent" })
	public void queryServiceContent(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		Long userId = Long.parseLong(request.getParameter("userId"));
		int page = NumberUtils.toInt((String) request.getParameter("page"), 1);
		int limit = NumberUtils.toInt((String) request.getParameter("limit"), -1);
		if (limit == -1) {
			page = 1;
			limit = 1000000;
		}
		int start = page * limit - limit;
		int type = NumberUtils.toInt((String) request.getParameter("type"), 0);
		String keyword = request.getParameter("keyword");
		if (keyword == null) {
			keyword = "";
		}
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		try {
			PagingSet<Task> set = this.taskService.getManagerTask(userId, start, limit, type, keyword, startTime, endTime);
			List<Task> ts = set.getList();
			if ((ts != null) && (ts.size() > 0)) {
				for (Task t : ts) {
					JSONObject js = new JSONObject();
					js.put("title", t.getContent());
					js.put("createTime", sf.format(t.getCreate()));
					js.put("name", t.getCustomer().getName());
					js.put("serviceId", t.getId());
					js.put("reject", t.getReject());
					js.put("reason", t.getReply());
					ja.put(js);
				}
			}
			json.put("dataInfo", ja);
			json.put("total", set.getTotal());
			json.put("retcode", "000000");
			json.put("retinfo", "success");
		} catch (Exception e) {
			e.printStackTrace();
			json.put("retcode", "000001");
			json.put("retinfo", "exception");
		}

		write(response, json);
	}
	
	@RequestMapping({ "queryServiceStatus" })
	public void queryServiceStatus(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			String serviceId = request.getParameter("serviceId");
			VisitRecord vr = this.recordService.getVisitRecordByTaskId(serviceId);
			int codeSignFlag = 1;
			int codeExitFlag = 1;
			int contentFlag = 1;
			int praiseFlag = 1;
			int takePhotoFlag = 1;
//			if (vr.getReject() != 1) {
				if (vr.getType() == 0) {
					contentFlag = 0;
					takePhotoFlag = 0;
					praiseFlag = 0;
					codeExitFlag = 0;
				} else if (vr.getType() == 1) {
					codeSignFlag = 0;
					if (vr.getResultCode() == 1) {
						praiseFlag = 0;
					}
				} else if (vr.getType() == 2) {
					codeSignFlag = 0;
					if (vr.getResultCode() == 1) {
						praiseFlag = 0;
					}
					codeExitFlag = 0;
				}
//			}
			json.put("codeSignFlag", codeSignFlag);
			json.put("codeExitFlag", codeExitFlag);
			json.put("contentFlag", contentFlag);
			json.put("takePhotoFlag", takePhotoFlag);
			json.put("praiseFlag", praiseFlag);
			json.put("retcode", "000000");
			json.put("retinfo", "success");
		} catch(Exception e) {
			e.printStackTrace();
			json.put("retcode", "000001");
			json.put("retinfo", "exception");
		}

		write(response, json);
	}

	@RequestMapping({ "queryContent" })
	public void queryContent(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		try {
			List<RecordDetail> ts = this.recordService.getDetails(request
					.getParameter("serviceId"));
			if ((ts != null) && (ts.size() > 0)) {
				for (RecordDetail t : ts) {
					JSONObject js = new JSONObject();
					js.put("name", t.getContent());
					js.put("id", t.getId());
					ja.put(js);
				}
			}
			json.put("dataInfo", ja);
			json.put("retcode", "000000");
			json.put("retinfo", "success");
		} catch (Exception e) {
			e.printStackTrace();
			json.put("retcode", "000001");
			json.put("retinfo", "exception");
		}

		write(response, json);
	}

	@RequestMapping({ "submitServiceContent" })
	public void submitServiceContent(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			String ids = request.getParameter("ids");
			String serviceId = request.getParameter("serviceId");
			String[] idArr = ids.split(",");
			StringBuffer sb = new StringBuffer();
			for (String id : idArr) {
				sb.append(",'").append(id).append("'");
			}

			this.recordService.submit(sb.substring(1), serviceId);

			json.put("retcode", "000000");
			json.put("retinfo", "success");
		} catch (Exception e) {
			e.printStackTrace();
			json.put("retcode", "000001");
			json.put("retinfo", "exception");
		}

		write(response, json);
	}

	@RequestMapping({ "submitServiceResult" })
	public void submitServiceResult(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			List mfs = multipartRequest.getFiles("file");
			String taskId = request.getParameter("serviceId");
			VisitRecord record = this.recordService.getVisitRecordByTaskId(taskId);
			String path = request.getServletContext().getRealPath("/recordImages") + File.separator
					+ record.getId() + "_";
			for (int i = 0; i < mfs.size(); ++i) {
				File f = new File(path + i + ".jpg");
				IOUtils.copy(((MultipartFile) mfs.get(i)).getInputStream(), new FileOutputStream(f));
			}
			json.put("retcode", "000000");
			json.put("retinfo", "success");
		} catch (Exception e) {
			e.printStackTrace();
			json.put("retcode", "000001");
			json.put("retinfo", "exception");
		}

		write(response, json);
	}

	@RequestMapping({ "praise" })
	public void praise(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try {
			String code = request.getParameter("code");
			String taskId = request.getParameter("serviceId");
			VisitRecord record = this.recordService.getVisitRecordByTaskId(taskId);
			Customer customer = record.getCustomer();
			if (code != null) {
				code = code.trim();
			}
			if (code == null ||
					!(code.equals("满意") ||
					(customer.getNumber() != null && code.equals(customer.getNumber())) ||
					(customer.getSellNumber() != null && code.equals(customer.getSellNumber())))) {
				json.put("retcode", "000003");
				json.put("retinfo", "二维码不正确！");
				write(response, json);
				return;
			}
			record.setResultCode(1);
			this.recordService.update(record);
			json.put("retcode", "000000");
			json.put("retinfo", "success");
		} catch (Exception e) {
			e.printStackTrace();
			json.put("retcode", "000001");
			json.put("retinfo", "系统异常");
		}

		write(response, json);
	}

	@RequestMapping({ "getVisitInfo" })
	public void getVisitInfo(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		Long userId = Long.parseLong(request.getParameter("userId"));
		int page = NumberUtils.toInt((String) request.getParameter("page"), 1);
		int limit = NumberUtils.toInt((String) request.getParameter("limit"), -1);
		if (limit == -1) {
			page = 1;
			limit = 1000000;
		}
		int start = page * limit - limit;
		int type = NumberUtils.toInt((String) request.getParameter("type"), 2);
		String keyword = request.getParameter("keyword");
		if (keyword == null) {
			keyword = "";
		}
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		StringBuffer where = new StringBuffer();
		if (type == 2) {
			where.append(" and t.complete = ").append(type);
		}
		if (userId != 1) {
			where.append(" and m.id = ").append(userId);
		}
		if (keyword.length() > 0) {
			where.append(" and (c.name like '%").append(keyword).append("%'");
			where.append(" or c.sell_number like '%").append(keyword).append("%'");
			where.append(" or c.phone_number like '%").append(keyword).append("%'");
			where.append(" or c.backup_number like '%").append(keyword).append("%')");
		}
		try {
			PagingSet pset = this.recordService.queryVisitRecords(start, limit, where.toString(), "visit_time", "DESC");
			List<VisitRecord> list = pset.getList();
			JSONArray ja = new JSONArray();
			if ((list != null) && (list.size() > 0)) {
				for (VisitRecord record : list) {
					JSONObject js = new JSONObject();
					js.put("username", record.getCustomerManager().getUsername());
					if (record.getVisitTime() != null) {
						js.put("arriveTime", sf.format(record.getVisitTime()));
					}
					if (record.getLeaveTime() != null) {
						js.put("leaveTime", sf.format(record.getLeaveTime()));
					}
					js.put("city", record.getCity());
					js.put("praise", record.getResultCode());
					JSONArray jar = new JSONArray();

					String path = request.getServletContext().getRealPath("/recordImages")
							+ File.separator;
					for (int i = 0; i < 6; ++i) {
						String url = path + record.getId() + "_" + i + ".jpg";
						File f = new File(url);
						if (f.exists()) {
							JSONObject job = new JSONObject();
							job.put("imageUrl",
									request.getRequestURL().substring(0,
											request.getRequestURL().length() - 23)
											+ "/record.do?action=getImage&path="
											+ record.getId()
											+ "_" + i + ".jpg");
							jar.put(job);
						}
					}
					js.put("imageList", jar);
					ja.put(js);
				}
			}
			json.put("dataInfo", ja);
			json.put("total", pset.getTotal());
			json.put("retcode", "000000");
			json.put("retinfo", "success");
		} catch (Exception e) {
			e.printStackTrace();
			json.put("retcode", "000001");
			json.put("retinfo", "exception");
		}
		write(response, json);
	}

	private ModelSwitch isSwitchOpen() {
		ModelSwitch s = this.switchService.query();
		if (s == null) {
			s = new ModelSwitch();
			s.setQrcode("1");
		}
		return s;
	}

	private void write(HttpServletResponse response, JSONObject json) {
		try {
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.write(json.toString(4));
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}