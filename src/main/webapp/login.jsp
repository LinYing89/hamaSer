<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.bairock.iot.hamaser.servlet.*"%>
<%@ page import="com.bairock.iot.hamaser.dao.*"%>
<%@ page isELIgnored="false"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String username = "";
	Cookie[] cookies = request.getCookies();
	if(cookies != null){
		for(Cookie cookie : cookies){
			if(cookie.getName().equals("user")){
				username = cookie.getValue();
			}
		}
	}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
<meta name="description" content="">
<meta name="author" content="">

<title>登录</title>

<!-- Bootstrap core CSS -->
<script src="<%=path%>/jquery/jquery-2.1.4.min.js"></script>
<script src="<%=path%>/bootstrap/js/bootstrap.min.js"></script>
<link href="<%=path%>/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<!-- 	<link href="http://libs.baidu.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet"> -->
<!--    <script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script> -->
<!--    <script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script> -->

</head>
<body>
	<nav class="navbar navbar-default" role="navigation">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#example-navbar-collapse">
				<span class="sr-only">切换导航</span> <span class="icon-bar"></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
		</div>
		<div class="collapse navbar-collapse" id="example-navbar-collapse">
			<ul class="nav navbar-nav">
				<li><a href="<%=path %>/servlet/LoginServlet?name=ggsb">公共设备</a></li>
<!-- 				<li><a href="javascript:void(0)" onclick="post('<%=path %>/servlet/LoginServlet', 'GGSB', 'a123456')">公共设备</a></li> -->
				<li><a href="<%=path %>/DownloadApp">下载</a></li>
			</ul>
		</div>
	</nav>
	
	<div class="container">
		<div class="row clearfix">
			<div class="col-md-4 column"></div>
			<div class="col-md-4 column" style="margin-top: 10px">
				<form class="form-horizontal" role="form"
					action="<%=path%>/Login" method="post">
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-9">
							<h3>登录</h3>
						</div>
					</div>
					<div class="form-group">
						<label for="name" class="col-sm-3 control-label">用户名</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" id="name" name="name"
								placeholder="用户名" value="<%=username%>">
						</div>
					</div>
					<div class="form-group">
						<label for="psd" class="col-sm-3 control-label">密码</label>
						<div class="col-sm-9">
							<input type="password" class="form-control" id="psd" name="psd"
								placeholder="密码">
						</div>
					</div>
					<div class="form-group">
						<label class="label label-danger col-sm-offset-3 col-sm-9">${error}</label>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-9">
							<div class="checkbox">
								<label> <input type="checkbox" name="autoLogin">
									自动登录
								</label>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-9">
							<button type="submit" class="btn btn-default btn-block">登录</button>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-9">
							<a class="btn btn-warning btn-block" href="<%=path%>/register.jsp">注册</a>
						</div>
					</div>
				</form>
				<div class="col-sm-offset-3 col-sm-9">
					<a href="<%=path %>/servlet/LoginServlet?name=ggsb">公共设备</a>
				</div>
			</div>
			<div class="col-md-4 column"></div>
		</div>
	</div>
	<iframe name=upload_iframe width=0 height=0></iframe>
</body>
</html>
