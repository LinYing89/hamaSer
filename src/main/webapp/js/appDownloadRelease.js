
window.onload=prepareLinks;
function prepareLinks(){
//	var links = document.getElementsByTagName("a");
	var links = document.getElementsByClassName("a_release");
	for(var i=0; i<links.length;i++){
		links[i].onclick=function(){
			var appName = this.innerHTML;
			//var appPath = this.href;
			popUp(appName);
			return false;
		}
	}
	
	var aDebug = document.getElementById("a_debug");
	aDebug.onclick=function(){
		window.open("ShowAppList?debug=true", "popup");
		return false;
	}
}

function popUp(appName){
	window.open("Download?appName="+appName + "&debug=false", "popup");
}