<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true"%>
<html>
<head>
<title>错误页面</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<style type="text/css">
body {
	border: solid #98c0f4 1px;
	margin: 0;
	padding: 0;
	overflow: auto;
	background: #fff;
}
#msg {
	margin: 0 auto;
	padding: 0;
	overflow: auto;
	width: 481px;
	height: 95px;
	background: #fff url('${contextPath}/theme/default/images/error.png') no-repeat top center;
}
#msg span {
	position: absolute;
	top: 90px;
	width: 481px;
	text-align: center;
	font: normal 16px/20px '微软雅黑';
	color: #333;
}
</style>
</head>
<body>
	<div id="msg">
		<span>${errorMsg}</span>
	</div>
</body>
</html>