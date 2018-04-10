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

    <title>注册组</title>

    <!-- Bootstrap core CSS -->
	<link href="<%=path%>/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<script src="<%=path%>/js/jquery-2.1.4.min.js"></script>
	<script src="<%=path%>/bootstrap/js/bootstrap.min.js"></script>
  </head>
  
  <body>
<div class="container">

	<div class="row clearfix">
		<div class="col-md-4 column">

		</div>
		<div class="col-md-4 column" style="margin-top: 10px">
			<form class="form-horizontal" role="form" action="<%=path%>/RegisterGroupServlet" method="post">
			   
			   <!-- groupname -->
			   <div class="form-group">
			      <label for="groupName" class="col-sm-3 control-label">组名</label>
			      <div class="col-sm-9">
			         <input type="text" class="form-control" id="groupName" name="groupName"
			            placeholder="组名" value="${formbean.groupName }">
			      </div>
			   </div>
			   <div class="form-group">
			      <label for="groupName" class="label label-danger col-sm-offset-3 col-sm-9">${formbean.errors.groupName}</label>
			   </div>
			   
			   <!-- pet name -->
			   <div class="form-group">
			      <label for="petName" class="col-sm-3 control-label">昵称</label>
			      <div class="col-sm-9">
			         <input type="text" class="form-control" id="petName" name="petName"
			            placeholder="昵称" value="${formbean.petName }">
			      </div>
			   </div>
			   
			   <!-- group password -->
			   <div class="form-group">
			      <label for="groupPsd" class="col-sm-3 control-label">组密码</label>
			      <div class="col-sm-9">
			         <input type="password" class="form-control" id="groupPsd" name="groupPsd"
			            placeholder="组密码" value="${formbean.groupPsd }">
			      </div>
			   </div>
			   <div class="form-group">
			      <label for="groupPsd" class="label label-danger col-sm-offset-3 col-sm-9">${formbean.errors.groupPsd}</label>
			   </div>
			   
			   <!-- group password -->
			   <div class="form-group">
			      <label for="groupConfirmPsd" class="col-sm-3 control-label">重复组密码</label>
			      <div class="col-sm-9">
			         <input type="password" class="form-control" id="groupConfirmPsd" name="groupConfirmPsd"
			            placeholder="组重复密码" value="${formbean.groupConfirmPsd }">
			      </div>
			   </div>
			   <div class="form-group">
			      <label for="groupConfirmPsd" class="label label-danger col-sm-offset-3 col-sm-9">${formbean.errors.groupConfirmPsd}</label>
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
