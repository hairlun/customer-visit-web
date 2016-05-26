<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<% String contextName = this.getServletContext().getServletContextName(); %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>404页面</title>
<style>
body{
	border: solid #98c0f4 1px;
	margin:0px;
	padding:0px;
	overflow: auto;
}
</style>
</head>
<body>
<img src = "${contextPath}/theme/default/common/404.gif"/>
</body>
</html>