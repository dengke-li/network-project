<?php
$p = $_POST["username1"];
echo $p;
$con = mysql_connect("mysql-tp.svc.enst-bretagne.fr","dli","1316113");
if ($con)
  {
  echo " you are my apple";
  }
mysql_select_db("DLI", $con);

$sql="INSERT INTO donne (id, url)
VALUES
('4','$p')";

if(!mysql_query($sql,$con))
   {die('Error: ' . mysql_error());
   }
echo " you are my apple";

mysql_close($con);
?>
