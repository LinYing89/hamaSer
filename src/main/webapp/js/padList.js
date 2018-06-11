
window.onload=prepareLinks;
function prepareLinks(){
	var links = document.getElementsByTagName("a");
	for(var i=0; i<links.length;i++){
		links[i].onclick=function(){
			var userInfo = this.innerHTML;
			alert(userInfo);
			popUp(whichPad.href);
			return false;
		}
	}
}

function toPadMessage(whichPad){
	var userInfo = whichPad.innerHTML;
	alert(userInfo);
	//popUp(whichPad.href);
	return false;
}

function popUp(winUrl){
	window.open(winUrl, "popup");
}