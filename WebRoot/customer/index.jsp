<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/syspage/js_include_core.jsp"></jsp:include>
<script type="text/javascript" src="customer/index.js"></script>
<style>
.font {
	font-family:'微软雅黑';
	font-size:12px;
	color:#ffffff
}
.user {
    background-image: url(user/images/user.gif)
}

.del {
	background-image: url(user/images/delete.gif)
}
.bind {
	background-image: url(user/images/collapse-all.gif)
}
.edit {
	background-image: url(user/images/edit.gif)
}
</style>
</head>
<body>
<script type="text/javascript">
String.prototype.endWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length)
	  return false;
	if(this.substring(this.length-str.length)==str)
	  return true;
	else
	  return false;
	return true;
	}
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