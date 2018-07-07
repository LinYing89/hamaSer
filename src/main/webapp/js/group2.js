$(document).ready(function() {
		
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
			var text = "";
			if(state == 0){
				text = "终端:离线";
			}else{
				text = "终端:在线";
			}
			var btnPadState = $(".navbar").find("#a_pad_state");
			btnPadState[0].innerHTML = text;
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
	var group = '<%=user.getName()%>';

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