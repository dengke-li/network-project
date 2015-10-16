<!DOCTYPE html>
<script language="javascript">  
    
    

      
    function Set_Cookie( name, value, expires, path, domain, secure )   
    {  
    // 先取得目前時間  
    var today = new Date();  
    today.setTime( today.getTime() );  
      
     
    if ( expires )  
    {  
    expires = expires * 1000 * 60 * 60 * 24;  
    }  
    var expires_date = new Date( today.getTime() + (expires) );
      
    document.cookie = name + "=" +escape( value )+  
( ( expires ) ? ";expires=" + expires_date.toGMTString() : "" ) +   
( ( path ) ? ";path=" + path : "" ) +   
( ( domain ) ? ";domain=" + domain : "" ) +  
( ( secure ) ? ";secure" : "" );  
}  ;  
    }  
    // 如果已經存在cookie，則取得該值  
    function Get_Cookie( name ) {  
          
    var start = document.cookie.indexOf( name + "=" );  
    var len = start + name.length + 1;  
    if ( ( !start ) &&  
    ( name != document.cookie.substring( 0, name.length ) ) )  
    {  
    return null;  
    }  
    if ( start == -1 ) return null;  
    var end = document.cookie.indexOf( ";", len );  
    if ( end == -1 ) end = document.cookie.length;  
    return unescape( document.cookie.substring( len, end ) );  
    }  
    //設定Cookie  
window.onload = function(){
    //你把这段放在<body onload="xx">上也可以
    var date1 = new Date();
    if(Get_Cookie( 'date1' )==null){
    Set_Cookie("date1",date1);  
    }
    //alert("你进入页面的时间"+date);
}
window.onbeforeunload = function(){
    //关闭或刷新页面时调用
    var date2 = new Date();
    //alert("你离开页面的时间"+date);
    if(Get_Cookie( 'date2' )==null){
    Set_Cookie("date2",date2);  
    }
     
    } 

}  
     
    </script>  



<html>
    <head>
        <meta charset="utf-8" />
        <link rel="stylesheet" href="style.css" />
        <!--[if lt IE 9]>
        <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
        <title>TITRE</title>

    

<!--
<script type="text/javascript" >

window.onload = function(){
    //你把这段放在<body onload="xx">上也可以
    var date1 = new Date();
    var id =1;
    //alert("你进入页面的时间"+date);
document.location.href='http://web-tp.svc.enst-bretagne.fr/~dli/log.php?date1='+encodeURIComponent(date1)+'&id'=+id;
}
window.onbeforeunload = function(){
    //关闭或刷新页面时调用
    var date2 = new Date();
    //alert("你离开页面的时间"+date);
document.location.href='http://web-tp.svc.enst-bretagne.fr/~dli/log.php?date2='+encodeURIComponent(date2);
}
</script>-->
    </head>
    
    <!--[if IE 6 ]><body class="ie6 old_ie"><![endif]-->
    <!--[if IE 7 ]><body class="ie7 old_ie"><![endif]-->
    <!--[if IE 8 ]><body class="ie8"><![endif]-->
    <!--[if IE 9 ]><body class="ie9"><![endif]-->
    <!--[if !IE]><!--><body><!--<![endif]-->
        <div id="bloc_page">
            <header>
                <div id="titre_principal">
                    <img src="images/logo_tb.png" alt="TELECOM BRETAGNE" id="logo" />
                    <h1>TEXTE</h1>
                    <h2>Texte</h2>
                </div>
                
                <nav>
                    <ul>
                        <li><a href="#">TEXTE</a></li>
                        <li><a href="#">TEXTE</a></li>
                        <li><a href="#">TEXTE</a></li>
                        <li><a href="#">TEXTE</a></li>
                    </ul>
                </nav>
            </header>
            
            <div id="banniere_image">
                <div id="banniere_description">
                   TEXTE
                    <a href="#" class="bouton_rouge">Voir l'article <img src="images/flecheblanchedroite.png" alt="" /></a>
                </div>
            </div>
            
            <section>
                <aside>
                    <h1> MENU </h1>
                    <img src="images/bulle.png" alt="" id="fleche_bulle" />
                    <p id="Tb"><img src="images/tbmedaillon.jpg" alt="Photo de TB" /></p>
                    <p> <?php
$comments='';
$con = mysql_connect("mysql-tp.svc.enst-bretagne.fr","dli","2347124");
//echo "<Script language="JavaScript"> alert('用户名不能为空！')</Script>";

if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("DLI", $con);

$result = mysql_query("SELECT DISTINCT domaine FROM projets2");


echo "<table border='1'>
<tr>
<th>categorie de domaines</th>


</tr>";


while($row = mysql_fetch_array($result))
  {
$domain=$row['domaine'];
echo  $row['cours'];
 echo "<tr>";
echo "<td>" . "<a href=\"infotable.php?domaine=".$row['domaine']."\" >$domain
   </a>" . "</td>";
 //echo "<td>" . "<a href=\"{http://web-tp.svc.enst-bretagne.fr/~dli/tableau0.php?cours='+encodeURIComponent($row['cours'])}\" title=\"This is the link\">    {$row['cours']}   </a>" . "</td>";
 

 echo "</tr>";
 "<br />";
  
  /*echo   "<script   language=javascript>
  function   test(){
  alert('commentaire');    
  }
  </script> ";*/
  /*echo '<input type="button" value="Back" onClick="history.go(-1);return true;">'
  echo "<script language='javascript'><button type="button" onclick="alert('Welcome!')">点击这里</button>;</script>";
  echo "<script<input type="button" onclick="foo('<?php echo $row['commentaire'];?>')"> /script>";*/
  echo "<br />";
  }
  echo "</table>";

mysql_close($con);
?>



</p>
                    
                </aside>
                <article>
                    <h1><img src="images/ico_epingle.png" alt="Catégorie voyage" class="ico_categorie" />TEXTE</h1>
                    <p> PARAGRAPHE 1 </p>
                    <p> PARAGRAPHE 2</p>
                    <p> PARAGRAPHE 3</p>
                </article>
                
            </section>
            
            <footer>
                <div id="tweet">
                    <h1>Texte</h1>
                    <p>Texte </p>
                    <p>Texte</p>
                </div>
                <div id="mes_photos">
                    <h1>Texte</h1>
                    
                </div>
                <div id="mes_amis">
                    <h1>Texte</h1>
                    <ul>
                        <li><a href="#">Texte</a></li>
                        <li><a href="#">Texte</a></li>
                        <li><a href="#">Texte</a></li>
                        <li><a href="#">Texte</a></li>
                    </ul>
                    <ul>
                        <li><a href="#">Texte</a></li>
                        <li><a href="#">Texte</a></li>
                        <li><a href="#">Texte</a></li>
                        <li><a href="#">Texte</a></li>
                    </ul>
                </div>
            </footer>
        </div>
    </body>
</html>
