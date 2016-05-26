package com.jude.controller;

import com.jude.entity.ModelSwitch;
import com.jude.json.JSONObject;
import com.jude.service.SwitchService;
import com.jude.util.ExtJS;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/switch.do" })
public class SwitchController {

	@Autowired
	private SwitchService switchService;

	@RequestMapping(params = { "action=forwardIndex" })
	public String forwardIndex(HttpServletRequest request) {
		ModelSwitch ms = this.switchService.query();
		request.setAttribute("qrcode", ms.getQrcode());
		request.setAttribute("timest", ms.getTimestamp());
		return "switch/index.jsp";
	}

	@RequestMapping(params = { "action=configSwitch" })
	@ResponseBody
	public JSONObject config(HttpServletRequest request) {
		try {
			if (!LoginInfo.isAdmin(request)) {
				return ExtJS.fail("非admin用户不能执行此操作！");
			}
			String qrcode = request.getParameter("qrcode");
			String timest = request.getParameter("timest");
			ModelSwitch ms = new ModelSwitch();
			ms.setQrcode(qrcode);
			ms.setTimestamp(timest);
			this.switchService.update(ms);
			return ExtJS.ok("设置成功！");
		} catch (Exception e) {
		}
		return ExtJS.fail("设置失败，请刷新页面后重试！");
	}
}