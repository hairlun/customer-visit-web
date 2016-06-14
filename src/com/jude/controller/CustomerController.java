package com.jude.controller;

import com.jude.entity.Customer;
import com.jude.entity.CustomerGroup;
import com.jude.entity.CustomerManager;
import com.jude.json.JSONArray;
import com.jude.json.JSONObject;
import com.jude.service.CustomerGroupService;
import com.jude.service.CustomerManagerService;
import com.jude.service.CustomerService;
import com.jude.util.ExcelColumn;
import com.jude.util.ExcelExporter;
import com.jude.util.ExtJS;
import com.jude.util.HttpUtils;
import com.jude.util.PagingSet;
import com.jude.util.QrCodeUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping({ "/customer.do" })
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerManagerService customerManagerService;

	@Autowired
	private CustomerGroupService customerGroupService;

	public static final Logger log = LoggerFactory.getLogger(CustomerController.class);

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@RequestMapping(params = { "action=forwardIndex" })
	public String forwardIndex() {
		return "customer/index.jsp";
	}

	@RequestMapping(params = { "action=queryAll" })
	public void queryAll(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> param = HttpUtils.getRequestParam(request);
			int start = NumberUtils.toInt((String) param.get("start"), 0);
			int limit = NumberUtils.toInt((String) param.get("limit"), 20);
			int groupId = NumberUtils.toInt((String) param.get("group"), -1);
			String sort = (String) param.get("sort");
			String dir = (String) param.get("dir");
			log.debug("groupId = " + groupId);
			PagingSet<Customer> set = this.customerService.getCustomers(start, limit, groupId, sort, dir);

			JSONArray rows = new JSONArray();
			JSONObject main = new JSONObject();
			List<Customer> customerList = set.getList();
			String gps;
			for (Customer customer : customerList) {
				JSONObject row = new JSONObject();
				row.put("id", customer.getId());
				row.put("number", customer.getNumber());
				row.put("name", customer.getName());
				row.put("sell_number", customer.getSellNumber());
				row.put("store_name", customer.getStoreName());
				row.put("level", customer.getLevel());
				row.put("phone_number", customer.getPhoneNumber());
				row.put("backup_number", customer.getBackupNumber());
				row.put("address", customer.getAddress());
				row.put("order_type", customer.getOrderType());
				gps = customer.getGps();
				row.put("gps", gps);
				if (gps != null && !gps.equals("")) {
					row.put("lng", gps.substring(0, gps.indexOf(",")));
					row.put("lat", gps.substring(gps.indexOf(",") + 1));
				} else {
					row.put("lng", "");
					row.put("lat", "");
				}
				CustomerManager cm = customer.getCustomerManager();
				if (cm != null && cm.getName() != null && cm.getUsername() != null) {
					row.put("mname", customer.getCustomerManager().getName() + "("
							+ customer.getCustomerManager().getUsername() + ")");
				} else {
					row.put("mname", "");
				}

				Date lastVisitTime = customer.getLastVisitTime();
				if (lastVisitTime != null)
					row.put("last_visit_time", getDateFormat(lastVisitTime));
				else {
					row.put("last_visit_time", "");
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

	@RequestMapping(params = { "action=addCustomer" })
	@ResponseBody
	public JSONObject addCustomer(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String number = request.getParameter("number");
			if (this.customerService.checkExist(number)) {
				return ExtJS.fail("客户编号已存在，请重新输入!");
			}
			Customer customer = new Customer();
			String name = request.getParameter("name");
			String phoneNumber = request.getParameter("phoneNumber");
			String sellNumber = request.getParameter("sellNumber");
			String storeName = request.getParameter("storeName");
			String backupNumber = request.getParameter("backupNumber");
			String gps = request.getParameter("gps");
			String level = request.getParameter("level");
			String orderType = request.getParameter("orderType");
			String address = request.getParameter("address");

			customer.setName(name);
			customer.setNumber(number);
			customer.setPhoneNumber(phoneNumber);
			customer.setSellNumber(sellNumber);
			customer.setStoreName(storeName);
			customer.setBackupNumber(backupNumber);
			customer.setGps(gps);
			customer.setLevel(level);
			customer.setOrderType(orderType);
			customer.setAddress(address);
			this.customerService.addCustomer(customer);
			return ExtJS.ok("新增客户成功！");
		} catch (Exception e) {
			log.error("add customer ex", e);
		}
		return ExtJS.fail("新增客户失败，请刷新页面重试！");
	}

	@RequestMapping(params = { "action=editCustomer" })
	@ResponseBody
	public JSONObject editCustomer(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String number = request.getParameter("number");
			String onumber = request.getParameter("onumber");
			if ((!number.equals(onumber)) && (this.customerService.checkExist(number))) {
				return ExtJS.fail("客户编号已存在，请重新输入!");
			}
			int id = Integer.parseInt(request.getParameter("id"));
			Customer customer = this.customerService.getCustomer(id);
			customer.setName(request.getParameter("name"));
			customer.setNumber(number);
			customer.setPhoneNumber(request.getParameter("phoneNumber"));
			customer.setSellNumber(request.getParameter("sellNumber"));
			customer.setStoreName(request.getParameter("storeName"));
			customer.setBackupNumber(request.getParameter("backupNumber"));
			customer.setGps(request.getParameter("gps"));
			customer.setLevel(request.getParameter("level"));
			customer.setOrderType(request.getParameter("orderType"));
			customer.setAddress(request.getParameter("address"));
			this.customerService.update(customer);
			return ExtJS.ok("编辑成功！");
		} catch (Exception e) {
			log.error("edit customer ex", e);
		}
		return ExtJS.fail("编辑客户失败，请刷新页面重试！");
	}

	@RequestMapping(params = { "action=delCustomers" })
	@ResponseBody
	public JSONObject delCustomers(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String ids = request.getParameter("ids");
			ids = ids.substring(1);
			this.customerService.deleteCustomers(ids);
		} catch (Exception e) {
			return ExtJS.fail("删除客户失败，请刷新页面后重试！");
		}
		return ExtJS.ok("删除客户成功！");
	}

	@RequestMapping(params = { "action=bindCustomer" })
	@ResponseBody
	public JSONObject bind(String cids, long mid, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String[] ids = cids.split(",");
			for (int i = 1; i < ids.length; ++i) {
				long id = Long.parseLong(ids[i]);
				CustomerManager manager = new CustomerManager();
				manager.setId(Long.valueOf(mid));
				Customer customer = new Customer();
				customer.setId(Long.valueOf(id));
				customer.setCustomerManager(manager);
				this.customerService.updateCustomerManager(customer);
			}
		} catch (Exception e) {
			return ExtJS.fail("绑定客户经理失败，请刷新页面后重试！");
		}
		return ExtJS.ok("绑定客户成功！");
	}

	@RequestMapping(params = { "action=export" })
	@ResponseBody
	public JSONObject exportExcel(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String res = export(null, null);
			if ("error".equals(res)) {
				return ExtJS.fail("导出失败!");
			}
			return ExtJS.ok(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ExtJS.fail("导出失败！");
	}

	@RequestMapping(params = { "action=downloadExport" })
	public void downloadExport(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return;
			}
			String res = request.getParameter("res");
			if (StringUtils.isBlank(res)) {
				return;
			}

			response.setHeader("Content-Disposition", "attachment; filename=file.zip");
			response.setContentType("application/octet-stream; charset=utf-8");
			InputStream is = new FileInputStream(res);
			OutputStream os = response.getOutputStream();
			IOUtils.copy(is, os);
			os.flush();
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(params = { "action=import" })
	@ResponseBody
	public JSONObject importExcel(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile mf = multipartRequest.getFile("file");
			Workbook excel = null;
			try {
				excel = new XSSFWorkbook(mf.getInputStream());
			} catch (Exception ex) {
				excel = new HSSFWorkbook(mf.getInputStream());
			}
			List<List<String>> datas = getDatas(excel);
			List customers = new ArrayList();
			for (List list : datas) {
				if ((list.get(0) == null) || (list.get(1) == null))
					continue;
				if (list.get(2) == null) {
					continue;
				}
				String number = (String) list.get(1);
				if (this.customerService.checkExist(number)) {
					continue;
				}
				Customer cus = new Customer();
				cus.setName((String) list.get(0));
				cus.setNumber(number);
				cus.setSellNumber((String) list.get(2));
				cus.setStoreName((String) list.get(3));
				cus.setLevel((String) list.get(4));
				cus.setPhoneNumber((String) list.get(5));
				cus.setBackupNumber((String) list.get(7));
				cus.setAddress((String) list.get(8));
				cus.setOrderType((String) list.get(9));
				String lng = (String) list.get(10);
				String lat = (String) list.get(11);
				if (lng != null && !lng.equals("") && lat != null && !lat.equals("")) {
					cus.setGps(lng + "," + lat);
				} else {
					cus.setGps("");
				}
				Date lastVisitTime = null;
				try {
					lastVisitTime = sdf.parse((String) list.get(12));
					cus.setLastVisitTime(lastVisitTime);
				} catch (Exception e) {
				}
				if (list.get(6) != null) {
					String musername = (String) list.get(6);
					if (musername.split("\\(").length > 1) {
						CustomerManager manager = this.customerManagerService
								.getCustomerManager(musername.split("\\(")[1].substring(0,
										musername.split("\\(")[1].length() - 1));
						if (manager != null) {
							cus.setCustomerManager(manager);
						}
					} else if (musername.split("（").length > 1) {
						CustomerManager manager = this.customerManagerService
								.getCustomerManager(musername.split("（")[1].substring(0,
										musername.split("（")[1].length() - 1));
						if (manager != null) {
							cus.setCustomerManager(manager);
						}
					}
				}

				customers.add(cus);
				try {
					this.customerService.addCustomer(cus);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return ExtJS.ok("导入客户成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ExtJS.ok("导入客户失败！");
	}

	@RequestMapping(params = { "action=qrcode" })
	public void getQrcode(HttpServletRequest request, HttpServletResponse response) {
		String number = request.getParameter("number");
		Customer customer = this.customerService.getCustomerByNumber(number);
		try {
			QrCodeUtils.toStream(customer.getSellNumber(), "jpg", response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<List<String>> getDatas(Workbook excel) {
		if (excel instanceof HSSFWorkbook) {
			return getDatas((HSSFWorkbook) excel);
		}
		return getDatas((XSSFWorkbook) excel);
	}

	public List<List<String>> getDatas(HSSFWorkbook excel) {
		List datas = new ArrayList();
		for (int i = 0; i < excel.getNumberOfSheets(); ++i) {
			HSSFSheet sheet = excel.getSheetAt(i);
			if (sheet.getLastRowNum() < 1) {
				continue;
			}
			for (int k = 1; k < sheet.getLastRowNum() + 1; ++k) {
				HSSFRow row = sheet.getRow(k);
				int cels = row.getLastCellNum();
				if (cels < 3) {
					continue;
				}
				cels = 13;
				List rrow = new LinkedList();
				for (short j = 0; j < cels; j = (short) (j + 1)) {
					HSSFCell cell = row.getCell(j);
					inject(rrow, cell);
				}
				datas.add(rrow);
			}
		}
		return datas;
	}

	public List<List<String>> getDatas(XSSFWorkbook excel) {
		List datas = new ArrayList();
		for (int i = 0; i < excel.getNumberOfSheets(); ++i) {
			XSSFSheet sheet = excel.getSheetAt(i);
			if (sheet.getLastRowNum() < 1) {
				continue;
			}
			for (int k = 1; k < sheet.getLastRowNum() + 1; ++k) {
				XSSFRow row = sheet.getRow(k);
				int cels = row.getLastCellNum();
				if (cels < 3) {
					continue;
				}
				cels = 13;
				List rrow = new LinkedList();
				for (short j = 0; j < cels; j = (short) (j + 1)) {
					XSSFCell cell = row.getCell(j);
					inject(rrow, cell);
				}
				datas.add(rrow);
			}
		}
		return datas;
	}

	private void inject(List<String> rrow, HSSFCell cell) {
		if (cell == null) {
			rrow.add("");
			return;
		}
		int r = cell.getCellType();
		switch (r) {
		case 1:
			rrow.add((cell.getRichStringCellValue().getString() == null) ? "" : cell
					.getRichStringCellValue().getString());
			break;
		case 4:
			rrow.add((cell.getBooleanCellValue()) ? "true" : "false");
			break;
		case 5:
			rrow.add(cell.getRichStringCellValue().getString());
			break;
		case 2:
			rrow.add(cell.getCellFormula());
			break;
		case 0:
			rrow.add(new Double(cell.getNumericCellValue()).intValue() + "");
			break;
		case 3:
			rrow.add("");
		}
	}

	private void inject(List<String> rrow, XSSFCell cell) {
		if (cell == null) {
			rrow.add("");
			return;
		}
		int r = cell.getCellType();
		switch (r) {
		case 1:
			rrow.add(cell.getRichStringCellValue().getString());
			break;
		case 4:
			rrow.add((cell.getBooleanCellValue()) ? "true" : "false");
			break;
		case 5:
			rrow.add(cell.getRichStringCellValue().getString());
			break;
		case 2:
			rrow.add(cell.getCellFormula());
			break;
		case 0:
			rrow.add(new Double(cell.getNumericCellValue()).intValue() + "");
			break;
		case 3:
			rrow.add("");
		}
	}

	public String export(Map<String, Object> param, String name) {
		name = "exportfile";
		List<Customer> list = this.customerService.getCustomers(0, 1000000).getList();

		List cols = new ArrayList();
		cols.add(new ExcelColumn("客户名称", 100));
		cols.add(new ExcelColumn("客户编号", 130));
		cols.add(new ExcelColumn("专卖证号", 130));
		cols.add(new ExcelColumn("店铺名称", 200));
		cols.add(new ExcelColumn("客户级别", 90));
		cols.add(new ExcelColumn("电话号码", 100));
		cols.add(new ExcelColumn("客户经理", 120));
		cols.add(new ExcelColumn("备用号码", 100));
		cols.add(new ExcelColumn("经营地址", 240));
		cols.add(new ExcelColumn("订货类型", 60));
		cols.add(new ExcelColumn("GPS经度", 80));
		cols.add(new ExcelColumn("GPS纬度", 80));
		cols.add(new ExcelColumn("最后一次拜访时间", 180));

		List rows = new ArrayList();
		String gps;
		String lng;
		String lat;
		for (Customer cus : list) {
			gps = cus.getGps();
			if (gps != null && !gps.equals("")) {
				lng = gps.substring(0, gps.indexOf(","));
				lat = gps.substring(gps.indexOf(",") + 1);
			} else {
				lng = "";
				lat = "";
			}
			Object[] row = {
					cus.getName(),
					cus.getNumber(),
					cus.getSellNumber(),
					cus.getStoreName(),
					cus.getLevel(),
					cus.getPhoneNumber(),
					cus.getCustomerManager() != null ? cus.getCustomerManager().getName() + "("
							+ cus.getCustomerManager().getUsername() + ")" : "",
					cus.getBackupNumber(),
					cus.getAddress(),
					cus.getOrderType(),
					lng,
					lat,
					getDateFormat(cus.getLastVisitTime()) };

			rows.add(row);
		}

		ExcelExporter builder = null;
		File zip = null;
		try {
			zip = new File(System.getProperty("java.io.tmpdir"), name + ".zip");
			builder = new ExcelExporter(new FileOutputStream(zip), name, cols, rows);
			builder.write();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ((builder != null) && (zip.length() > 0L)) ? zip.getAbsolutePath() : "error";
	}

	private String getDateFormat(Date date) {
		try {
			return sdf.format(date);
		} catch (Exception e) {
		}
		return "";
	}
}