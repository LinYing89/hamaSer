<%@page import="com.bairock.iot.hamaser.listener.SessionHelper"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.bairock.iot.intelDev.user.User"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
User user = (User) request.getSession().getAttribute(
			SessionHelper.USER);
if(null != user){
	request.setAttribute("listGroup", user.getListDevGroup());
}

String ctrlable = (String)request.getSession().getAttribute("ctrlable");
if(null == ctrlable){
	ctrlable = "true";
}
%>

<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>组</title>
    
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
	<meta name="description" content="">
	<meta name="author" content="">
  
	<!-- Bootstrap core CSS -->
	<link href="<%=path%>/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<link href="<%=path%>/jquery/jquery-ui.min.css" rel="stylesheet">
	<script src="<%=path%>/jquery/jquery-2.1.4.min.js"></script>
	<script src="<%=path%>/jquery/jquery-ui.min.js"></script>
	<script src="<%=path%>/bootstrap/js/bootstrap.min.js"></script>

	<script type="text/javascript">
	var groupName = "";
	$(function() {
		$( "#dialog-confirm" ).dialog({
	      autoOpen: false,
	      modal: true,
	      show: {
	        effect: "explode",
	        duration: 500
	      },
	      hide: {
	        effect: "explode",
	        duration: 500
	      },
	      buttons: {
	        "确认": function() {
	          $( this ).dialog( "close" );
	          window.location.href="<%=path%>/DeleteGroupServlet?devGroupName=" + groupName; 
	        },
	        "取消": function() {
	          $( this ).dialog( "close" );
	        }
	      }
	    });
	    var ctrlable = "<%=ctrlable%>";
		if(ctrlable=="false"){
			$(".a_ctrl").hide();
			$(".div_addGroup").hide();
		}
	});
	function deleteGroup(group){
		groupName = group;
		$( "#dialog-confirm" ).dialog( "open" );
	}
	</script>
  </head>
  
  <body>
	<nav class="navbar navbar-default" role="navigation">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
				data-target="#example-navbar-collapse">
				<span class="sr-only">切换导航</span> <span class="icon-bar"></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#"><%=user.getName() %></a> 
		</div>
		<div class="collapse navbar-collapse" id="example-navbar-collapse">
			<ul class="nav navbar-nav">
				<li><a href="<%=path%>/Logout">注销</a></li>
				<li><a href="<%=path %>/ShowAppList?debug=false">下载</a></li>
			</ul>
		</div>
	</nav>
    <div class="container">
<!-- 		<div class="row clearfix"> -->
<!-- 			<div class="col-md-12 column"> -->
<!-- 				<ul class="nav nav-tabs"> -->
<!-- 					<li id="li_device" class="active"><a >我的设备</a></li> -->
<!-- 				</ul> -->
<!-- 			</div> -->
<!-- 		</div> -->
		<h4>组列表</h4>
		<div id="control_device">
		<ul class="nav nav-pills nav-stacked">
			<c:forEach items="${listGroup}" var="group">
				<li class="dropdown">
					<a class="dropdown-toggle" data-toggle="dropdown"
						href="<%=path%>/ChangeGroupServlet?groupName=${group.name}">${group.name} ${group.petName}
						<span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<li><a href="<%=path%>/ChangeGroupServlet?groupName=${group.name}">进入</a></li>
						<li class="divider"></li>
						<li><a class="a_ctrl" href="<%=path%>/page/editGroup.jsp?devGroupName=${group.name}">编辑</a></li>
						<li><a class="a_ctrl" href="javascript:void(0)" onclick="deleteGroup('${group.name }')">删除</a></li>
					</ul>
				</li>
			</c:forEach>
		</ul>
		</div>
		<div class="div_addGroup">
			<a class="btn btn-primary btn-block" href="<%=path%>/page/registerGroup.jsp">添加组</a>
		</div>
		<div id="dialog-confirm" title="确认删除吗？">
		  <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>这个组将被删除，并且无法恢复。您确定吗？</p>
		</div>
    </div>
  </body>
</html>
