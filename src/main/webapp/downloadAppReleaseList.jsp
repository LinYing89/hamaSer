<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css"
	integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB"
	crossorigin="anonymous">
<title>呱呱物联app下载</title>
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-light bg-light">
		<a id="a_debug" class="nav-link" href="#">转到测试版</a>
	</nav>
	<div class="container">
		<div class="alert alert-info" style="font-size:12px">发布版更新为测试版或测试版更新为发布版，都需要将之前的版本卸载再安装，否则会导致安装失败</div>
		<table class="table table-striped">
			<caption style="caption-side:top">呱呱物联app发布版下载(Android)</caption>
			<thead>
				<tr>
					<th>名称</th>
					<th>发布时间</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${listApp}" var="app">
					<tr>
						<td><a class="a_release" href="#">${app.appName}</a><br />版本说明:${app.appInfo}</td>
						<td>${app.releaseTime}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
		integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
		crossorigin="anonymous"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
		integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
		crossorigin="anonymous"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"
		integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T"
		crossorigin="anonymous"></script>
	<script type="text/javascript" src="js/appDownloadRelease.js"></script>

</body>
</html>