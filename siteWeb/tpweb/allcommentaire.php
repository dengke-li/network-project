<!DOCTYPE html>
<html>

<body>
<?php

$urlid =$_GET['urlid'];
echo $url;
$notes=0;
$numbre=0;
$con = mysql_connect("mysql-tp.svc.enst-bretagne.fr","dli","2347124");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("DLI", $con);


$result1 = mysql_query("SELECT * FROM projets2 where urlid='$urlid'");

echo "<table border='1'>
<tr>
<th>note</th>
<th>commentaire</th>
<th>customer</th>

</tr>";
$row = mysql_fetch_array($result1);
echo "<tr>";
 //echo "<td>" . "<a href=\"{$row['url']}\" title=\"This is the link\">    {$row['url']}   </a>" . "</td>";
 echo "<td>" . $row['note'] . "</td>";
 echo "<td>" . $row['commentaire'] . "</td>";
 echo "<td>" . $row['username'] . "</td>";
 echo "</tr>";
 $notes= $notes+$row['note']."<br />";
 $numbre=$numbre+1;
 echo "<br />";
$result = mysql_query("SELECT * FROM commentaire where url='$row[url]'");
while($row = mysql_fetch_array($result))
  {
  
 echo "<tr>";
 //echo "<td>" . "<a href=\"{$row['url']}\" title=\"This is the link\">    {$row['url']}   </a>" . "</td>";
 echo "<td>" . $row['note'] . "</td>";
 echo "<td>" . $row['commentaire'] . "</td>";
 echo "<td>" . $row['username'] . "</td>";
 echo "</tr>";
 $notes= $notes+$row['note']."<br />";
 $numbre=$numbre+1;
  
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
if($numbre!=0){
  echo $notes/$numbre;}
mysql_close($con);
?>


</body>
</html>
