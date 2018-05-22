<%@page import="com.bairock.iot.hamaser.listener.SessionHelper"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.bairock.iot.intelDev.user.User"%>
<%@ page import="com.bairock.iot.intelDev.user.DevGroup"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String groupName = request.getParameter(SessionHelper.DEV_GROUP_NAME);
	User manager = (User)request.getSession().getAttribute(SessionHelper.USER);
	DevGroup group = manager.findDevGroupByName(groupName);
	request.getSession().setAttribute(SessionHelper.DEV_GROUP, group);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
<meta name="description" content="">
<meta name="author" content="">

<title>编辑组</title>

<!-- Bootstrap core CSS -->
<link href="<%=path%>/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<script src="<%=path%>/jquery/jquery-2.1.4.min.js"></script>
<script src="<%=path%>/bootstrap/js/bootstrap.min.js"></script>
</head>

<body>
	<div class="container">
		<form class="bs-example bs-example-form" role="form" action="<%=path%>/EditGroupServlet" method="post">
			<div class="input-group">
				<span class="input-group-addon">组名</span> 
				<input type="text" class="form-control" name="devGroupName" value="<%=group.getName()%>">
			</div>
			<br>
			<div class="input-group">
				<span class="input-group-addon">组昵称</span> 
				<input type="text" class="form-control" name="petName" value="<%=group.getPetName()%>">
			</div>
			<br>
			<div class="input-group">
				<span class="input-group-addon">组密码</span>
				<input type="password" class="form-control" name="psd" value="<%=group.getPsd()%>">
			</div>
			<br>
			<!-- submit -->
		   <div class="form-group">
		        <button type="submit" class="btn btn-warning btn-block ">提交</button>
		   </div>
		   <div class="form-group">
		        <a type="button" class="btn btn-default btn-block" href="<%=path%>/groupList.jsp">取消</a>
		   </div>
		</form>
	</div>
</body>
</html>
