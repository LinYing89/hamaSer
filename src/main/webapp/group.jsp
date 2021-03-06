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
	DevGroup group = (DevGroup) request.getSession().getAttribute(SessionHelper.DEV_GROUP);
	String urlAlarm = path + "/FindGroupAlarmInfo?userName=" + managerName + "&groupName=" + group.getName();
	List<Device> listDev = group.findListIStateDev(true);
	Collections.sort(listDev);
	List<DevCollect> listClimate = group.findListCollectDev(true);
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
<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css"
	integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB"
	crossorigin="anonymous">

  
	<!-- Bootstrap core CSS -->
<%-- 	<link href="<%=path%>/bootstrap/css/bootstrap.min.css" rel="stylesheet"> --%>
	<link href="<%=path%>/jquery/jquery-ui.min.css" rel="stylesheet">
	
<title>设备</title>

<!-- Bootstrap core CSS -->
<link href="<%=path%>/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<script src="<%=path%>/jquery/jquery-2.1.4.min.js"></script>
<script src="<%=path%>/bootstrap/js/bootstrap.min.js"></script>

</head>

<body>
	<nav class="navbar navbar-expand-lg navbar-light bg-light">
		<span id="groupName" class="navbar-text">
    		<%=managerName%>-<%=group.getName()%>:<%=group.getPetName()%>
  		</span>
	  	<a class="nav-link" href="<%=path%>/Logout">注销</a>
	  	<a id="a_refresh" class="nav-link" href="#">刷新状态</a>
<%-- 	  	<a id="a_alarm" class="nav-link" href="<%=urlAlarm%>">报警记录</a> --%>
	</nav>

	<div class="container">
		
		<div class="row">
			<div class="col-md-12 column">
				<ul class="nav nav-tabs">
					<li id="li_device" class="active"><a href="#">电器列表</a></li>
					<li id="li_climate"><a href="#">仪表列表</a></li>
				</ul>
			</div>
		</div>

		<div id="control_device">
			<table class="table">
				<thead>
					<tr>
						<th>名称/位号</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody id="tbody_device">
					<c:forEach items="${listDevice}" var="device">
						<tr class="d${device.longCoding} danger">
							<td style="width: 40%">
								<div>${device.name}</div>
								<div>${device.alias}</div>
							</td>
							<td style="width: 45%">
								<div class="btn-group">
									<button style="width: 29%; float: left" type="button"
										class="btn btn-default btn_ctrl d${device.longCoding}_3"
										onclick="ctrlDev('${device.parent.coding}', '${device.subCode }', '3')">开
									</button>
									<button style="width: 42%; float: left" type="button"
										class="btn btn-default d${device.longCoding}_0 btn_ctrl active"
										onclick="ctrlDev('${device.parent.coding}', '${device.subCode }', '0')">自动
									</button>
									<button style="width: 29%; float: left" type="button"
										class="btn btn-default  btn_ctrl d${device.longCoding}_4"
										onclick="ctrlDev('${device.parent.coding}', '${device.subCode }', '4')">关
									</button>
								</div>
							</td>
							<td id="d${device.longCoding}_a" style="width: 15%">离线</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div id="climate_device">
			<table class="table table-striped">
				<thead>
					<tr>
						<th>位号</th>
						<th>名称</th>
						<th>值</th>
					</tr>
				</thead>
				<tbody id="tbody_climate">
					<c:forEach items="${listClimate}" var="climate">
						<tr class="d${climate.longCoding} danger">
							<td>${climate.alias}</td>
							<td>${climate.name}</td>
							<td class="d${climate.longCoding }v">${climate.collectProperty.currentValue}${climate.collectProperty.unitSymbol}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<iframe name=upload_iframe width=0 height=0></iframe>
	
	<script src="<%=path%>/jquery/jquery-2.1.4.min.js"></script>
	<script src="<%=path%>/jquery/jquery-ui.min.js"></script>
	<script src="<%=path%>/bootstrap/js/bootstrap.min.js"></script>
	
<!-- 	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" -->
<!-- 		integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" -->
<!-- 		crossorigin="anonymous"></script> -->
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
		integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
		crossorigin="anonymous"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"
		integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T"
		crossorigin="anonymous"></script>
		
		<script type="text/javascript">
	$(document).ready(function() {
		
		$("#groupName").css("color", "red");
		
		$("#climate_device").hide();
		$("#li_device").click(function() {
			$(this).attr("class", "active");
			$("#li_climate").removeAttr("class");
			$("#control_device").show();
			$("#climate_device").hide();
		});
		$("#li_climate").click(function() {
			$(this).attr("class", "active");
			$("#li_device").removeAttr("class");
			$("#control_device").hide();
			$("#climate_device").show();
		});
		$("#li_alarm").click(function() {
			$(this).attr("class", "active");
			$("#li_device").removeAttr("class");
			$("#control_device").hide();
			$("#climate_device").show();
		});
		$("#a_refresh").click(function() {
			post('{"jsonId":2}');
		});
		
	});
	
	function ctrlDev(coding, num, state){
		var json = new Object();
		json.jsonId = 3;
		json.coding = coding;
		json.num = num;
		json.state = state;
		send(JSON.stringify(json));
		return false; 
	}
	
	function post(params){
		//alert(params);
		send(params);
		return false; 
	}
	
	function refreshState2(states){
		//alert(states);
		var obj = JSON.parse(states);
		//alert(obj.jsonId);
		if(obj.jsonId == 3){
			//设备状态协议
			var devCoding = obj.devCoding;
			//‘0’关，‘1’开，‘2’停，‘3’正常，‘4’异常
			var state = obj.state;
			//alert(state);
// 			var trDevice = $("#tbody_device").find(".d"+devCoding);
			var trDevice = $(".container").find(".d"+devCoding);
			//alert(trDevice + states)
			if(state == "0"){
				//off
				trDevice[0].setAttribute("class", "d" + devCoding);
			}else if(state == "1"){
				//on
				trDevice[0].setAttribute("class", "success d" + devCoding);
			}else if(state == "3"){
				//normal
				trDevice[0].setAttribute("class", "d" + devCoding);
			}else if(state == "4"){
				//abnormal
				trDevice[0].setAttribute("class", "danger d" + devCoding);
				var tdModel = document.getElementById("d" + devCoding+"_a");
				if(tdModel != null){
					tdModel.innerHTML = "离线";
				}
				
			}
		}else if(obj.jsonId == 4){
			//控制模式协议
			var devCoding = obj.devCoding;
			var ctrlModel = obj.ctrlModel;
			var tdModel = document.getElementById("d" + devCoding+"_a");
			if(tdModel == null){
				return;
			}
			if(ctrlModel == "LOCAL"){
				if(tdModel.innerHTML != "本地"){
					tdModel.innerHTML = "本地";
				}
			}else if(ctrlModel == "REMOTE"){
				if(tdModel.innerHTML != "远程"){
					tdModel.innerHTML = "远程";
				}
			}
		}else if(obj.jsonId == 5){
			//挡位协议
			var devCoding = obj.devCoding;
			var gear = obj.gear;
			if(gear == "ZIDONG"){
				//auto
				var btnAuto = $("#tbody_device").find(".d"+devCoding+"_0");
				var btnOn = $("#tbody_device").find(".d"+devCoding+"_3");
				var btnOff = $("#tbody_device").find(".d"+devCoding+"_4");
				btnAuto.addClass("active");
				btnOn.removeClass("active");
				btnOff.removeClass("active");
			}else if(gear == "KAI"){
				//on
				var btnAuto = $("#tbody_device").find(".d"+devCoding+"_0");
				var btnOn = $("#tbody_device").find(".d"+devCoding+"_3");
				var btnOff = $("#tbody_device").find(".d"+devCoding+"_4");
				btnAuto.removeClass("active");
				btnOn.addClass("active");
				btnOff.removeClass("active");
			}else if(gear == "GUAN"){
				//off
				var btnAuto = $("#tbody_device").find(".d"+devCoding+"_0");
				var btnOn = $("#tbody_device").find(".d"+devCoding+"_3");
				var btnOff = $("#tbody_device").find(".d"+devCoding+"_4");
				btnAuto.removeClass("active");
				btnOn.removeClass("active");
				btnOff.addClass("active");
			}
		}else if(obj.jsonId == 6){
			//液位计压力值
			var devCoding = obj.devCoding;
			var value = obj.currentValue;
			//var perValue = obj.percent;
			//alert(obj);
			//alert(devCoding + value);
			var trDevice = $("#tbody_climate").find(".d"+devCoding);
			trDevice[0].setAttribute("class", "d" + devCoding);
			var tdClimate = $("#tbody_climate").find(".d"+devCoding+"v");
			tdClimate[0].innerHTML = value;
		}else if(obj.jsonId == 7){
			//终端状态
			var state = obj.state;
			if(state == 0){
				$("#groupName").css("color", "red");
			}else{
				$("#groupName").css("color", "black");
			}
		}
	}
	
	function clearTime(){
		closeWebSocket();
	}
	window.onunload=clearTime;
	
	var serverIp = '<%=request.getAttribute("serverIp")%>';
	var websocket = null;
	if('WebSocket' in window){
//  		websocket = new WebSocket("ws://051801.cn/hamaSer/websocket");
		websocket = new WebSocket("ws://" + serverIp + "/hamaSer/websocket");
// 		websocket = new WebSocket("ws://123.206.104.15/hamaSer/websocket");
//   		websocket = new WebSocket("ws://192.168.1.111:8080/hamaSer/websocket");
	}else{
		alert("浏览器不支持websocket");
	}
	
	websocket.onerror = function(){
// 		alert("onerror");
	};
	
	var manage = '<%=managerName%>';
	var group = '<%=group.getName()%>';

	websocket.onopen = function() {
		var jsonUser = new Object();
		jsonUser.jsonId = 1;
		jsonUser.userName = manage;
		jsonUser.groupName = group
		send(JSON.stringify(jsonUser));
	};

	websocket.onmessage = function(event) {
		// 		alert("onmessage:" + event.data);
		//refreshState(event.data);
		refreshState2(event.data);
	};

	websocket.onclose = function() {
		// 		alert("onclose");
	};

	function closeWebSocket() {
		websocket.close();
	}

	function send(message) {
		websocket.send(message);
	}
</script>
</body>
</html>
