<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=request.getAttribute("appName") %></title>
<jsp:include page="/syspage/js_include_core.jsp"></jsp:include>
<script type="text/javascript" src="main/index.js"></script>
<style>
#iframe {
	width:100%;
	height:100%;
}
.font {
	font-family:'微软雅黑';
	font-size:12px;
	color:#ffffff
}
</style>
</head>
<body>
<div id="north-zone" style="background-image:url(theme/default/main/top_bg_bg.png);background-color:#2083c0">
	<table style="border:0px;height:75px; width:100%;background-repeat:no-repeat; background-image:url(${contextPath}/theme/default/main/top_bg.png)">
	  <tr>
<!-- 	    <td height="40" width="320"><img src="theme/default/main/title.png" width="320" style="margin-top:5px"/></td> -->
	    <td></td>
	    <td>
	    	<table style="width:100%;border:0px">
			  <tr>
			    <td></td>
			    <td></td>
			  </tr>
			</table>
	    </td>
	    <td width="180">
	    	<table border="0" style="width:100%">
			  <tr>
			    <td width="10" height="36"><audio id="audio" loop="loop"></audio></td>
			    <td width="90" class="font" valign="top">欢迎&nbsp;<span id="userLogin"><a href="#" id='v' class="font"></a></span></td>
			    <td width="40" class="font" valign="top"><a href="logout.do" target="_top" class="font">退出</a></td>
<!-- 			    <td width="40" valign="top"><a href="#" onclick="control.about()" class="font">关于</a></td> -->
			  </tr>
			</table>
	    </td>
	  </tr>
	</table>
</div>
<!-- <iframe src="main/sub_index.jsp" width="100%" height="100%" frameborder="0"></iframe> -->
<script type="text/javascript">
	document.getElementById('v').innerHTML = Ext.util.Format.ellipsis('${loginUser.username}', 8);
	var control;
	Ext.onReady(function(){
		var config = {
				userName : "${loginUser.username}",
				version : "${version}",
				app_name : "${appName}",
				copyright : "${copyright}"
		}
		control = new Ext.haode.Control(config);
	});
</script>

</body>
</html>