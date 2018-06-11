<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>呱呱物联app下载</title>
</head>
<body>
	<h1>呱呱物联app下载(Android)</h1>
	<ul>
	<c:forEach items="${listApp}" var="app">
		<li><a href="#">${app.appName}</a></li>
	</c:forEach>
	</ul>
	
	<script type="text/javascript" src="js/appList.js"></script>
</body>
</html>