<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>呱呱物联测试版版下载</title>
</head>
<body>
	<h1>呱呱物联app测试版下载(Android)</h1>
	<em>发布版更新为测试版或测试版更新为发布版，都需要将之前的版本卸载再安装，否则会导致安装失败</em>
	<br/>
	<a id="a_release" href="">发布版</a>
	<ul>
	<c:forEach items="${listApp}" var="app">
		<li><a class="a_debug" href="#">${app.appName}</a> 发布时间：${app.releaseTime}</li>
	</c:forEach>
	</ul>
	
	<script type="text/javascript" src="js/appDownloadDebug.js"></script>
</body>
</html>