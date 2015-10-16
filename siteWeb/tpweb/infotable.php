<!DOCTYPE html>
<html>

<body>
<?php
$username=$_GET['username'];
$password=$_GET['password'];
$domaine =$_GET['domaine']; 
$comments='';
$con = mysql_connect("mysql-tp.svc.enst-bretagne.fr","dli","2347124");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("DLI", $con);

$result = mysql_query("SELECT DISTINCT cours FROM projets2 where domaine='$domaine'");


echo "<table border='1'>
<tr>
<th>cours</th>


</tr>";


while($row = mysql_fetch_array($result))
  {
$cour=$row['cours'];
echo  $row['cours'];
 echo "<tr>";
echo "<td>" . "<a href=\"tablenew.php?cours=".$row['cours']."\" >$cour
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


</body>
</html>
