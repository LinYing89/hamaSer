/**
 * 报警信息网页脚本
 */
window.onload=writeInfo;
function writeInfo(){
	var str='<%=request.getAttribute("alarms") %>';
	var dataObj = eval("(" + str + ")");       //转换为json对象 
	alart(dataObj);
	var $tab1 = $("#mytab");
    
}