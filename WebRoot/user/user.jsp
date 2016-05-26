<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="theme/default/css/style.css" />
<jsp:include page="/syspage/js_include_core.jsp"></jsp:include>
<script type="text/javascript" src="scripts/ext-3.4/haode/md5.js"></script>
<script type="text/javascript" src="user/index.js"></script>
<style>
.user {
    background-image: url(user/images/user.gif)
}

.del {
	background-image: url(user/images/delete.gif)
}
</style>
</head>
<body>
	<script type="text/javascript">
		var panel;
		Ext.onReady(function(){
			var config = {
					
			};
			panel = new Ext.haode.UserPanel(config);
		});
	</script>
</body>
</html>