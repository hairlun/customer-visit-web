HAODE = {
	version : "2.2"
};

/*
 * 显示模式窗口,规范了各个浏览器窗口的大小
 */
HAODE.showModalDlg = function(url, vArguments, sFeatures, width, height) {
	if (!width) {
		width = screen.availWidth - 10;
	}

	if (!height) {
		height = screen.availHeight - 30;
	}
	
	if (HAODE.getBrowser() == "msie 6.0") {
		width = width + 8;
		height = height + 52;
	} else if (HAODE.getBrowser() == "msie 7.0") {
		width = width + 8;
		height = height + 5;
	} else if (HAODE.getBrowser() == "firefox") {
		width = width + 8;
		height = height + 30;
	}
	if (sFeatures) {
		if (sFeatures.substring(sFeatures.length - 1) != ";") {
			sFeatures += ";";
		}
	}
	sFeatures += ";status=0;resizable=1;";
	sFeatures += "dialogWidth=" + width + "px;dialogHeight=" + height + "px";
	return window.showModalDialog(url, vArguments, sFeatures);
};

/*
 * 显示模式窗口,规范了各个浏览器窗口的大小
 */
HAODE.openMax = function(url, vArguments) {
	var w = screen.availWidth - 15;
	var h = screen.availHeight - 35;
	if (HAODE.getBrowser() == "safari"){
		w = screen.availWidth - 10;
		h = screen.availHeight - 55;
	}
	if (!vArguments) {
		vArguments = "_blank";
	}
	window.open(url, vArguments,
			"toolbar=0, menubar=0, status=0, location=0, left=0, top=0, width="
					+ w + ",height=" + h + ",resizable=yes");
};

/**
 * 打开一个居中的窗口
 */
HAODE.openWindow = function(url, title, w, h) {
	if (!w) {
		w = screen.availWidth - 10;
	}

	if (!h) {
		h = screen.availHeight - 30;
	}
	var l = Math.ceil((window.screen.width - w) / 2) - 5;
	var t = Math.ceil((window.screen.height - h) / 2) - 30;
	var features = 'width= '
			+ w
			+ ', '
			+ 'height= '
			+ h
			+ ', '
			+ 'left= '
			+ l
			+ ', '
			+ 'top= '
			+ t
			+ ', '
			+ 'location=no, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, status=no';
	var popWin = window.open(url, title, features);
	popWin.focus();
};

/**
 * 获得顶层窗口
 * @returns
 */
HAODE.getTopWindow = function() {
	var win = window;
	while (win != parent) {
		win = parent;
	}
	return win;
};

/**
 * 获取浏览器类型
 */
HAODE.getBrowser = function() {
	var s = navigator.userAgent.toLowerCase();
	var a = new Array("msie 6.0", "msie 7.0", "firefox", "safari", "opera",
			"netscape");
	for ( var i = 0; i < a.length; i++) {
		if (s.indexOf(a[i]) != -1) {
			return a[i];
		}
	}
	return "other";
};

/**
 * 创建文本选择框 rootPath 根目录名
 */
HAODE.initCalendar = function(el, arg) {
	var rootPath = arg.rootPath;
	var format = arg.format;
	if (!format) {
		format = "yyyy-M-dd";
	}
	var obj = Ext.getDom(el);

	var p = obj.parentNode;

	p.onclick = function() {
		var width = parseInt(this.style.width.replace("px", ""));
		var offsetX = event.offsetX;
		var x = event.screenX;
		var y = event.screenY;
		if ((width - offsetX) < 20) {
			var feature = "dialogWidth:259px;dialogHeight:223px;resizable:yes;dialogLeft:"
					+ x + "px;dialogTop:" + y + "px;";
			var url = "/" + rootPath + "/scripts/haode/calendar.htm";
			var v = window.showModalDialog(url, "", feature);
			if (v && v != "") {
				this.firstChild.value = HAODE.dateFormat(new Date(v.split("-")
						.join("/")), format);
			}
		}
	};
};

/**
 * 日期格式化
 * @param obj
 * @param fmt
 * @returns
 */
HAODE.dateFormat = function(obj, fmt) {
	// 对Date的扩展，将 Date 转化为指定格式的String
	// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
	// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
	// 例子：
	// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
	// (new Date()).Format("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18
	var o = {
		"M+" : obj.getMonth() + 1, // 月份
		"d+" : obj.getDate(), // 日
		"h+" : obj.getHours(), // 小时
		"m+" : obj.getMinutes(), // 分
		"s+" : obj.getSeconds(), // 秒
		"q+" : Math.floor((obj.getMonth() + 3) / 3), // 季度
		"S" : obj.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (obj.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
};

/**
 * 格式化一个数字
 */
HAODE.formatNumber = function(number, pattern) {
	var str = Math.abs(number).toString();
	var strInt;
	var strFloat;
	var formatInt;
	var formatFloat;
	var fixedStr = HAODE.getFixedStr(pattern);
	var thinPattern = HAODE.removeFixedStr(pattern);

	if (/\./g.test(thinPattern)) {
		formatInt = thinPattern.split('.')[0];
		formatFloat = thinPattern.split('.')[1];
	} else {
		formatInt = thinPattern;
		formatFloat = null;
	}

	// 如果格式化字符串有"%"，需要乘以100
	if (pattern.indexOf("%") > 0 && pattern.indexOf("'%'") < 0) {
		str = (parseFloat(str) * 100).toString();
	}

	if (/\./g.test(str)) {
		if (formatFloat != null) {
			var tempFloat = Math.round(parseFloat('0.' + str.split('.')[1])
					* Math.pow(10, formatFloat.length))
					/ Math.pow(10, formatFloat.length);
			strInt = (Math.floor(parseFloat(str)) + Math.floor(tempFloat))
					.toString();
			strFloat = /\./g.test(tempFloat.toString()) ? tempFloat.toString()
					.split('.')[1] : '0';
		} else {
			strInt = Math.round(parseFloat(str)).toString();
			strFloat = '0';
		}
	} else {
		strInt = str;
		strFloat = '0';
	}

	if (formatInt != null) {
		var outputInt = '';
		var zero = formatInt.match(/0*$/)[0].length;
		var comma = null;
		if (/,/g.test(formatInt)) {
			comma = formatInt.match(/,[^,]*/)[0].length - 1;
		}
		var newReg = new RegExp('(\\d{' + comma + '})', 'g');

		if (strInt.length < zero) {
			outputInt = new Array(zero + 1).join('0') + strInt;
			outputInt = outputInt.substr(outputInt.length - zero, zero);
		} else {
			outputInt = strInt;
		}

		var outputInt = outputInt.substr(0, outputInt.length % comma)
				+ outputInt.substring(outputInt.length % comma).replace(newReg,
						(comma != null ? ',' : '') + '$1');
		outputInt = outputInt.replace(/^,/, '');
		strInt = outputInt;
	}

	if (formatFloat != null) {
		var outputFloat = '';
		var zero = formatFloat.match(/^0*/)[0].length;
		if (strFloat.length < zero) {
			outputFloat = strFloat + new Array(zero + 1).join('0');
			var outputFloat1 = outputFloat.substring(0, zero);
			var outputFloat2 = outputFloat.substring(zero, formatFloat.length);
			outputFloat = outputFloat1 + outputFloat2.replace(/0*$/, '');
		} else {
			outputFloat = strFloat.substring(0, formatFloat.length);
		}
		strFloat = outputFloat;
	} else {
		if (thinPattern != '' || (thinPattern == '' && strFloat == '0')) {
			strFloat = '';
		}
	}

	var returnValue = strInt;

	// 如果格式化的值为负数，需在返回值前加"-";
	if (number < 0) {
		returnValue = "-" + returnValue;
	}

	returnValue += (strFloat == '' ? '' : '.' + strFloat);

	// 在返回值后加后缀字符，如”元“，”万“等;
	if (fixedStr != "") {
		if (fixedStr.indexOf("'%'") >= 0) {
			fixedStr = fixedStr.replace(new RegExp("'", "gm"), "");
		}
		returnValue += fixedStr;
	}

	return returnValue;
};


HAODE.getFixedStr = function(str) {
	str = str.replace(new RegExp("#", "gm"), "");
	str = str.replace(new RegExp("0", "gm"), "");
	str = str.replace(new RegExp(",", "gm"), "");
	str = str.replace(".", "");
	return str;
};

HAODE.removeFixedStr = function(str) {
	var s = HAODE.getFixedStr(str);
	str = str.replace(new RegExp(s, "gm"), "");
	return str;
};

/**
 * 用于产生一个随机数字
 */
HAODE.random = function() {
	var d = new Date();
	var s = "haode-" + d.getYear() + d.getDate() + (d.getMonth() + 1)
			+ d.getTime();
	return s;
};

/**
 * 使容器中的文字闪烁
 * @param id
 * @returns
 */
HAODE.blinkById = function(id) {
	if (!id) {
		id = "blink";
	}
	;
	var dom = document.getElementById(id);
	if (!dom)
		return;

	if (!dom.style.color) {
		dom.style.color = "red";
		dom.style.fontSize = "12px";
		dom.style.fontWeight = "bold";
	}
	if (dom.style.color == "red") {
		dom.style.color = "black";
		dom.style.fontSize = "12px";
		dom.style.fontWeight = "";
	} else {
		dom.style.color = "red";
		dom.style.fontWeight = "bold";
		dom.style.fontSize = "12px";
	}
	var timer = setTimeout("HAODE.blinkById()", 200);
	return timer;
};