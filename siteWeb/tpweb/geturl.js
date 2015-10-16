function checkNote(note)
{
	note = parseInt(note);
	if (isNaN(note))
	   {
	   alert("Veuillez rentrer un nombre");
	   return 0;
	   }
	else
	   {
	   if (note < 0)
	      {
	      alert("La note doit etre positive");
	      }
	   else if (note > 10)
	      {
	      alert("La note doit etre inferieure a 10");
	      }
	    else
	    {
	    	return 1;
	    }
	   }
}


function checkIfOk(note,keywords)
{
	var test = checkNote(note);
	if (test)
	{
		return 1;
	}
	else
	{
		return 0;
	}
}

function userInp()
{
	var username=prompt("username", "");
	var note = prompt("Note du site", "0-10");
	var domaine = prompt("Domaine", "");
	var cours=prompt("cours","");
	var keywords = prompt("Mots-clefs", "separes par des virgules");
	var commentaire=prompt("commentaire","");
	if (checkIfOk(note,keywords))
		{
			alert('merci !');
			document.location='http://web-tp.svc.enst-bretagne.fr/~dli/geturl.php?url='+encodeURIComponent(location.href)+'&title='+encodeURIComponent(document.title)+'&note='+encodeURIComponent(note)+'&keywords='+encodeURIComponent(keywords)+'&domaine='+encodeURIComponent(domaine)+'&cours='+encodeURIComponent(cours)+'&commentaire='+encodeURIComponent(commentaire)+'&username='+encodeURIComponent(username);
		}
	else
		{
			eval(userInp());
		}
}
function getCookie(c_name)
{
if (document.cookie.length>0)
  {
  c_start=document.cookie.indexOf(c_name + "=")
  if (c_start!=-1)
    { 
    c_start=c_start + c_name.length+1 
    c_end=document.cookie.indexOf(";",c_start)
    if (c_end==-1) c_end=document.cookie.length
    return unescape(document.cookie.substring(c_start,c_end))
    } 
  }
return ""
}
function allCookie(){
prompt('Please enter your name:',document.cookie.indexOf(c_name + "="))

}

function setCookie(c_name,value,expiredays)
{
var exdate=new Date()
exdate.setDate(exdate.getDate()+expiredays)
document.cookie=c_name+ "=" +escape(value)+
((expiredays==null) ? "" : ";expires="+exdate.toGMTString())
}


function checkCookie()
{
username=getCookie('username')
if (username!=null && username!="")
  {alert('Welcome again '+username+'!')}
else 
  {
  username=prompt('Please enter your name:',"")
  if (username!=null && username!="")
    {
    setCookie('username',username,365)
    }
  }
}







eval(userInp());

// var toto = userInp();
// alert(location.href)
// alert(document.title);
