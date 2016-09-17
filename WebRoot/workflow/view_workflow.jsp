<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/syspage/js_include_core.jsp"></jsp:include>
<script type="text/javascript" src="workflow/view_workflow.js"></script>
<style>
.font {
	font-family:'微软雅黑';
	font-size:12px;
	color:#ffffff
}
.add {
    background-image: url(user/images/user.gif)
}
</style>
</head>
<body>
<script type="text/javascript">
	Ext.onReady(function(){
		var config = {
		}
		control = new Ext.haode.Control(config);
	});
</script>

</body>
</html>