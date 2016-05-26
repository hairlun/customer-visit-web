<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="/syspage/js_include_core.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="record/menuicon_win.css"/>
<link rel="stylesheet" type="text/css" href="record/custom.css"/>
<!-- <script type="text/javascript" src="record/menuicon_win.js"></script> -->
<script type="text/javascript" src="record/index.js"></script>
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
.imge {
	background-image: url(user/images/view.png)
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
		}
		control = new Ext.haode.Control(config);
	});
</script>

</body>
</html>