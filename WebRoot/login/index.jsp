<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<title>${appName}</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="theme/default/css/style.css" />
<jsp:include page="/syspage/js_include_core.jsp"></jsp:include>
<script type="text/javascript" src="scripts/ext-3.4/haode/md5.js"></script>
<script type="text/javascript">
//登录页面作为顶级窗口
if (self != top) {
	top.location = "loginview.do";
}
</script>
<style type="text/css">
body{
 background-image:url("theme/default/login/login_bg.png");
 background-color:#2082bf;
 background-repeat:repeat-x;
}
</style>
</head>
<body>
<div style="text-align:center">
<table id="zone" style="margin:0px auto; width:925px; height:600px; margin-top:0px; background-image:url(theme/default/login/login_form_bg.png)">
  <tr>
    <td valign="top">
        <form action="login.do" id="form1" method="post">
        <input type="hidden" name="pwd" id="pwd"/>
		<div id="name-zone" style="position:relative; color:#ff0000; width: 332px; height: 28px; left: 413px; top: 240px;"><input id="name" name="name" type="text" value="${name}" style="width:188px;height:22px;"/> &nbsp;&nbsp;<%=request.getAttribute("msg") == null ? "":request.getAttribute("msg") %></div>
		<div id="password-zone" style="position:relative; color:#ff0000; width: 332px; height: 28px; left: 413px; top: 266px;"><input id="password" name="password" type="password" style="width:188px;height:22px;"/> &nbsp;&nbsp;<%=request.getAttribute("pwdmsg") == null ? "":request.getAttribute("pwdmsg") %></div>
		<input id="image" style="position:relative;left: 415px; top: 280px;width:94px;height:37px;" type="image" src="theme/default/login/login_btn.png" onclick="login(event)"/>
		<div id="copyright" style="position:relative; text-align:center; width: 290px; line-height: 24px; left: 317px; top:393px; color:#ffffff">${copyright}</div>
        </form>
    </td>
  </tr>
</table>
</div>
<script type="text/javascript">

	function login(e) {
		e = e || window.event;

		if (e.preventDefault) {
			e.preventDefault();
		} else {
			e.returnValue = false;
		}

		var form = document.getElementById('form1');
		var p = document.getElementById('password');
		var pwd = p.value;
		document.getElementById('pwd').value = hex_md5(pwd);
		form.submit();
	}
	
	Ext.onReady(function() {
// 		new Ext.KeyMap(Ext.getBody(), [{
// 			key: "y",
// 			ctrl: true, 
// 			fn: function() { 
// 				var src = 'version.do?action=forwardVersion';
// 		    	window.showModalDialog(src, null, 'dialogWidth : 455px; dialogHeight : 300px; resizable:no; status:no;');
// 			}
// 		}]);
		
		if (!Ext.isIE){
			Ext.getDom("copyright").style.top = "393px";
		}
		
		Ext.getDom("name").focus();
	});
</script>
</body>
</html>
