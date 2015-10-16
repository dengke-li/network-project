<?php
/*if(isset($_COOKIE["date1"])&&isset($_COOKIE["date2"]))  
{  
    $date1 = $_COOKIE["date1"];  
    $date2 = $_COOKIE["date2"];  
    echo $date1;
    echo $date2;
    

}  */
$p=$_GET['pg'];
echo $p;
echo 1;
$time_one = date("Y-m-d H:i:s");
$stamp_one=strtotime($time_one)-strtotime($time_one); 

echo $stamp_one;
/*
$date1 = $_GET['date1'];
$id=$_GET['id'];
$url =$_GET['date1'];  // filtre contre les SQL Injection
print "url : $url";
$title =$_GET['date2']; 
$note = $_GET['id'];;


echo $date1;
$date2 = $_GET['date2'];
//echo $date2-$date1;
$con=mysql_connect("mysql-tp.svc.enst-bretagne.fr","dli","2347124");
// Check connection
if (mysql_errno())
  {
  echo "Failed to connect to MySQL: " . mysql_connect_error();
  }
mysql_select_db("DLI", $con);

$sql="INSERT INTO projets2(url,title, note)
VALUES
('$url','$title','$note')";

if(!mysql_query($sql,$con))
  {
  die('Error: ' . mysql_error($con));
  }

echo "1 record added - Thanks for sharing !<br />";
echo "url : $url";
echo "<br /> title : $title";
echo "<br /> note : $note";


mysql_close($con);*/
 
?>
