<?php

print "Thanks for sharing ! <br /><br />";
$url =$_GET['url'];  // filtre contre les SQL Injection
print "url : $url";
$title =$_GET['title']; 
$note = $_GET['note'];
$keywords = $_GET['keywords'];
$cours = $_GET['cours'];
$domaine = $_GET['domaine'];
$commentaire = $_GET['commentaire'];
$username=$_GET['username'];


$con=mysql_connect("mysql-tp.svc.enst-bretagne.fr","dli","2347124");
// Check connection
if (mysql_errno())
  {
  echo "Failed to connect to MySQL: " . mysql_connect_error();
  }
mysql_select_db("DLI", $con);

$sql="INSERT INTO projets2(url,title, note, keywords,cours, domaine,commentaire,username)
VALUES
('$url','$title','$note','$keywords','$cours','$domaine','$commentaire','$username')";

if(!mysql_query($sql,$con))
  {
  die('Error: ' . mysql_error($con));
  }

echo "1 record added - Thanks for sharing !<br />";
echo "url : $url";
echo "<br /> title : $title";
echo "<br /> note : $note";
echo "<br /> keywords : $keywords";
echo "<br /> cours : $cours";
echo "<br /> domaine : $domaine";
echo "<br /> commentaire : $commentaire";
echo "<br /> username : $username";

mysql_close($con);

?>
