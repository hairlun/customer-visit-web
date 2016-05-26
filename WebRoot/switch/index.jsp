<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/syspage/js_include_core.jsp"></jsp:include>
<script type="text/javascript" src="switch/index.js"></script>
</head>
<body>
<script type="text/javascript">
var control;
Ext.onReady(function(){
	var config = {
		qrcode: "${qrcode}",
		timest: "${timest}"
	};
	control = new Ext.haode.Control(config);
});
</script>
</body>
</html>