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


function checkIfOk(note)
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

function Inp()
{
	var username=prompt("saisir votre username","");
	var note = prompt("Note du site", "0-10");	
	var commentaire=prompt("commentaire","");
	if (checkIfOk(note))
		{
			alert('merci !');
			document.location='http://web-tp.svc.enst-bretagne.fr/~dli/commenter.php?url='+encodeURIComponent(location.href)+'&title='+encodeURIComponent(document.title)+'&note='+encodeURIComponent(note)+'&commentaire='+encodeURIComponent(commentaire)+'&username='+encodeURIComponent(username);
		}
	else
		{
			eval(Inp());
		}
}




eval(Inp());
