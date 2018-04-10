<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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

    <title>注册用户</title>

    <!-- Bootstrap core CSS -->
	<link href="<%=path%>/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<script src="<%=path%>/jquery/jquery-2.1.4.min.js"></script>
	<script src="<%=path%>/bootstrap/js/bootstrap.min.js"></script>
  </head>
  
  <body>
<div class="container">

	<div class="row clearfix">
		<div class="col-md-4 column">

		</div>
		<div class="col-md-4 column" style="margin-top: 10px">
			<form class="form-horizontal" role="form" action="<%=path%>/RegisterServlet" method="post">
				<!-- user name -->
			   <div class="form-group">
			      <label for="name" class="col-sm-3 control-label">用户名</label>
			      <div class="col-sm-9">
			         <input type="text" class="form-control" id="name" name="name"
			            placeholder="用户名" value="${formbean.name }">
			      </div>
			   </div>
			   <div class="form-group">
			      <label for="name" class="label label-danger col-sm-offset-3 col-sm-9">${formbean.errors.name}</label>
			   </div>
			   
			   <!-- password -->
			   <div class="form-group">
			      <label for="psd" class="col-sm-3 control-label">密码</label>
			      <div class="col-sm-9">
			         <input type="password" class="form-control" id="psd" name="psd"
			            placeholder="密码" value="${formbean.psd }">
			      </div>
			   </div>
			   <div class="form-group">
			      <label for="psd" class="label label-danger col-sm-offset-3 col-sm-9">${formbean.errors.psd}</label>
			   </div>
			   
			   <!-- confirm password -->
			   <div class="form-group">
			      <label for="confirmPsd" class="col-sm-3 control-label">确认密码</label>
			      <div class="col-sm-9">
			         <input type="password" class="form-control" id="confirmPsd" name="confirmPsd"
			            placeholder="确认密码" value="${formbean.confirmPsd }">
			      </div>
			   </div>
			   <div class="form-group">
			      <label for="confirmPsd" class="label label-danger col-sm-offset-3 col-sm-9">${formbean.errors.confirmPsd}</label>
			   </div>
			   
			   <!-- submit -->
			   <div class="form-group">
			      <div class="col-sm-offset-3 col-sm-9">
			         <button type="submit" class="btn btn-warning btn-block ">注册</button>
			      </div>
			   </div>
			</form>
		</div>
		<div class="col-md-4 column">

		</div>
	</div>
</div>
  </body>
</html>
