<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
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
	
<title>远程设备信息</title>

<!-- Bootstrap core CSS -->
<link href="<%=path%>/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<script src="<%=path%>/jquery/jquery-2.1.4.min.js"></script>
<script src="<%=path%>/bootstrap/js/bootstrap.min.js"></script>

</head>

<body>
<span>过滤条件:</span>
	<input id="input_filter" type="text">
	<button id="btn_set_filter">设置过滤器</button>
	<button id="btn_clear">清空</button>
	<br/>
<div id="container">
</div>

<script type="text/javascript">
	
$(document).ready(function() {
	
	$("#btn_set_filter").click(function() {
		var filter = $("#input_filter").val();
		send(filter);
	});
	
	$("#btn_clear").click(function() {
		$("#container").empty();
	});
	
});
	
	function clearTime(){
		closeWebSocket();
	}
	window.onunload=clearTime;
	
	var url = "ws://" + window.location.host + "/hamaSer/remoteLog";
	var websocket = null;
	if('WebSocket' in window){
		websocket = new WebSocket(url);
	}else{
		alert("浏览器不支持websocket");
	}
	
	websocket.onerror = function(){
	};

	websocket.onopen = function() {
		send("");
	};

	websocket.onmessage = function(event) {
		var msg = event.data;
		if(msg.charAt(0) == "1"){
			$("#container").append("<span>" + event.data + "</span><br/>");
		}else{
			$("#container").append("<span style='color:red'>" + event.data + "</span><br/>");
		}
	};

	websocket.onclose = function() {
	};

	function closeWebSocket() {
		websocket.close();
	}

	function send(message) {
		websocket.send(message);
	}
</script>
</body>