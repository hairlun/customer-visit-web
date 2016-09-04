<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/syspage/js_include_core.jsp"></jsp:include>
<script type="text/javascript" src="scripts/ext-3.4/haode/md5.js"></script>
<script type="text/javascript" src="department/index.js"></script>
<style>
.font {
	font-family:'微软雅黑';
	font-size:12px;
	color:#ffffff
}
.user {
    background-image: url(user/images/user.gif)
}
.edit {
	background-image: url(user/images/edit.gif)
}
.del {
	background-image: url(user/images/delete.gif)
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