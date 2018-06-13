
window.onload=prepareLinks;
function prepareLinks(){
//	var links = document.getElementsByTagName("a");
	var links = document.getElementsByClassName("a_debug");
	for(var i=0; i<links.length;i++){
		links[i].onclick=function(){
			var appName = this.innerHTML;
			//var appPath = this.href;
			popUp(appName);
			return false;
		}
	}
	
	var aDebug = document.getElementById("a_release");
	aDebug.onclick=function(){
		window.open("ShowAppList?debug=false", "popup");
		return false;
	}
}

function popUp(appName){
	window.open("Download?appName="+appName + "&debug=true", "popup");
}