<%@page import="com.bairock.iot.hamaser.listener.SessionHelper"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.bairock.iot.intelDev.user.User"%>
<%@ page import="com.bairock.iot.intelDev.user.DevGroup"%>
<%@ page import="com.bairock.iot.intelDev.device.*"%>
<%@ page import="com.bairock.iot.intelDev.device.devcollect.DevCollect"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	String managerName = ((User) request.getSession().getAttribute(SessionHelper.USER)).getName();
	DevGroup user = (DevGroup) request.getSession().getAttribute(SessionHelper.DEV_GROUP);
	List<Device> listDev = user.findListIStateDev(true);
	Collections.sort(listDev);
	List<DevCollect> listClimate = user.findListCollectDev(true);
	Collections.sort(listClimate);

	request.setAttribute("listDevice", listDev);
	request.setAttribute("listClimate", listClimate);
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css"
	integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB"
	crossorigin="anonymous">
<link href="<%=path%>/jquery/jquery-ui.min.css" rel="stylesheet">
<title>设备</title>
</head>

<body>
	<nav class="navbar navbar-expand-lg navbar-light bg-light">
		<span class="navbar-text"> <%=managerName%>-<%=user.getName()%>:<%=user.getPetName()%>
		</span> <a class="nav-link" href="<%=path%>/Logout">注销</a> <a id="a_refresh"
			class="nav-link" href="#">刷新状态</a> <span id="a_pad_state"
			class="navbar-text"> 终端状态 </span>
	</nav>

	<div class="container">
		<div class="row">
			<div class="col-md-auto">
				<div class="nav flex-column nav-pills" id="v-pills-tab"
					role="tablist" aria-orientation="vertical">
					<a class="nav-link active" id="v-pills-dev-tab" data-toggle="pill"
						href="#v-pills-dev" role="tab" aria-controls="v-pills-dev"
						aria-selected="true">设备</a> <a class="nav-link"
						id="v-pills-alarm-tab" data-toggle="pill" href="#v-pills-alarm"
						role="tab" aria-controls="v-pills-alarm" aria-selected="false">报警记录</a>
				</div>
			</div>
			<div class="col">
				<div class="tab-content" id="v-pills-tabContent">
					<div class="tab-pane fade show active" id="v-pills-dev"
						role="tabpanel" aria-labelledby="v-pills-dev-tab">
						<div class="row">
							<div class="col">
								<div class="row">
									<div class="card" style="width: 142px">
										<div class="btn-group">
											<button type="button" class="btn">开</button>
											<button type="button" class="btn btn-primary">自动</button>
											<button type="button" class="btn">关</button>
										</div>
										<div class="card-body">
											<a class="card-link" href="#">开关</a>
										</div>
									</div>
									<div class="card" style="width: 142px">
										<div class="btn-group">
											<button type="button" class="btn">开</button>
											<button type="button" class="btn btn-primary">自动</button>
											<button type="button" class="btn">关</button>
										</div>
										<div class="card-body">
											<a class="card-link" href="#">开关</a>
										</div>
									</div>
									<div class="card" style="width: 142px">
										<div class="btn-group">
											<button type="button" class="btn">开</button>
											<button type="button" class="btn btn-primary">自动</button>
											<button type="button" class="btn">关</button>
										</div>
										<div class="card-body">
											<a class="card-link" href="#">开关</a>
										</div>
									</div>
									<div class="card" style="width: 142px">
										<div class="btn-group">
											<button type="button" class="btn">开</button>
											<button type="button" class="btn btn-primary">自动</button>
											<button type="button" class="btn">关</button>
										</div>
										<div class="card-body">
											<a class="card-link" href="#">开关</a>
										</div>
									</div>
									<div class="card" style="width: 142px">
										<div class="btn-group">
											<button type="button" class="btn">开</button>
											<button type="button" class="btn btn-primary">自动</button>
											<button type="button" class="btn">关</button>
										</div>
										<div class="card-body">
											<a class="card-link" href="#">开关</a>
										</div>
									</div>
								</div>
							</div>
							<div class="col-6">
								<div class="card" style="width: 200px">
									<img class="card-img-top" src="image/image.png"
										alt="Card image">
									<div class="card-body">
										<p class="card-text">Some example text.</p>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="tab-pane fade" id="v-pills-alarm" role="tabpanel"
						aria-labelledby="v-pills-alarm-tab">...</div>
				</div>
			</div>
		</div>
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
	<script type="text/javascript" src="js/group2.js"></script>

</body>
</html>
