
window.onload=prepareLinks;
function prepareLinks(){
	var links = document.getElementsByTagName("a");
	for(var i=0; i<links.length;i++){
		links[i].onclick=function(){
			var appName = this.innerHTML;
			//var appPath = this.href;
			popUp(appName);
			return false;
		}
	}
}

function popUp(appName){
	window.open("Download?appName="+appName, "popup");
}