<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <link rel="stylesheet" href="stylenew.css" />
        <!--[if lt IE 9]>
        <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
        <title>TITRE</title>
    </head>
    <?php
$username =$_POST['username'];

$password = $_POST['password'];

echo $username;
$con=mysql_connect("mysql-tp.svc.enst-bretagne.fr","dli","2347124");

// Check connection
if (mysql_errno())
  {
  echo "Failed to connect to MySQL: " . mysql_connect_error();
  }
mysql_select_db("DLI", $con);

$result=mysql_query("SELECT * FROM customers where username='$username' AND password='$password'");

if(!mysql_num_rows($result))
  {
  die('login Error: ' . mysql_error($con));
  }
$q=$username;
echo "<td>" . "<a href=\"compte.php?username=$q\" title=\"This is the link\">    'votre commentaire'   </a>" . "</td>";

mysql_close($con);

?>
    <!--[if IE 6 ]><body class="ie6 old_ie"><![endif]-->
    <!--[if IE 7 ]><body class="ie7 old_ie"><![endif]-->
    <!--[if IE 8 ]><body class="ie8"><![endif]-->
    <!--[if IE 9 ]><body class="ie9"><![endif]-->
    <!--[if !IE]><!-->
	<body><!--<![endif]-->
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
	
          <input type="text" name="name" size="25" maxlength="25">  
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
echo "<td>" . "<a href=\"infotable.php?username=+$username+&password=+$password+&domaine=".$row['domaine']."\" >$domain
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

<script src="http://localhost:8081/api-js/easyrec.js" type="text/javascript"></script>
<script type="text/javascript">
   var apiKey = "83eee83d7ef6770c4ed9b1d18178f7dd";
   var tenantId = "abc";
</script> 


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
