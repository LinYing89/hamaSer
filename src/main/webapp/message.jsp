<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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

    <title></title>

    <!-- Bootstrap core CSS -->
	<link href="<%=path%>/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<script src="<%=path%>/jquery/jquery-2.1.4.min.js"></script>
	<script src="<%=path%>/bootstrap/js/bootstrap.min.js"></script>
  </head>
  
  <body>
    ${message}
  </body>
</html>
