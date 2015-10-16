<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Untitled Document</title>
<script type="text/javascript">
function getCookie(name){
if(document.cookie.length > 0){
start = document.cookie.indexOf(name + "=");
if( start != -1){
start = start +name.length + 1;
end = document.cookie.indexOf(";",start);
if( end == -1){
end = document.cookie.length;
}
return decodeURI(document.cookie.substring(start,end));
}
}
return "";
}
function setCookie(name, value, expires, path, domain, secure){
var curcookie = name + "=" +encodeURI(value)
+ ((expires) ? ";expires=" + expires.toGMTString() : "")
+ ((path) ? ";path=" + path : "")
+ ((domain) ? ";domain=" + domain : "")
+ ((secure) ? ";secure" : "");
document.cookie = curcookie;
}
function start() {
var text = "";
var visitorName = getCookie("VisitorName");
var lasttime = getCookie("time");
var isnMonth = new Array("1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月");
var isnDay = new Array("星期日","星期一","星期二","星期三","星期四","星期五","星期六");
var today = new Date();
var year = today.getYear();
var day = today.getDate();
var cookietime = year + "年" + isnMonth[today.getMonth()] + day +"日" + isnDay[today.getDay()];
today.setTime(today.getTime() + 365*24*60*60*1000);
setCookie("time",cookietime,today);
if(visitorName =="") {
visitorName = prompt("Input your name:");
setCookie("VisitorName",visitorName,today);
document.write("Hello,"+ visitorName + "首次光臨本站，請多提寶貴意見");
}
else {
text += "Hello,"+ visitorName + "您是第 " + count() + "次訪問本站,<br>上次訪問時間為:" + lasttime;
document.write(text);
}
}
function count() {
var now = new Date();
now.setTime(now.getTime() + 365*24*60*60*1000);
var visits = getCookie("counter");
if( !visits ){
visits = 1;
}else {
visits = parseInt(visits) + 1;
}
setCookie("counter",visits,now);
return visits;
} 

</script>
</head>
<body 


onload="start()">
</body>
</html> 
