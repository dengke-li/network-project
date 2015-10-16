<?php

print "Thanks for sharing ! <br /><br />";
$url =$_GET['url'];  // filtre contre les SQL Injection
print "url : $url";
$username = $_GET['username'];
$note = $_GET['note'];
$commentaire = $_GET['commentaire'];
$con=mysql_connect("mysql-tp.svc.enst-bretagne.fr","dli","2347124");
// Check connection
if (mysql_errno())
  {
  echo "Failed to connect to MySQL: " . mysql_connect_error();
  }
mysql_select_db("DLI", $con);


$sql="INSERT INTO commentaire(url,note,commentaire,username)
VALUES
('$url','$note','$commentaire','$username')";

if(!mysql_query($sql,$con))
  {
  die('Error: ' . mysql_error($con));
  }

echo "1 record added - Thanks for sharing !<br />";
echo "url : $url";

echo "<br /> note : $note";
echo "<br /> commentaire : $commentaire";

mysql_close($con);

?>
