<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>呱呱物联app下载</title>
</head>
<body>
	<h1>呱呱物联app下载(Android)</h1>
	<ul>
	<c:forEach items="${listApp}" var="app">
		<li><a href="${app.appPath}" download="guagua.apk">${app.appV}</a></li>
	</c:forEach>
	</ul>
</body>
</html>