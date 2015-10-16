<?php
$comments='';
$con = mysql_connect("mysql-tp.svc.enst-bretagne.fr","dli","2347124");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("DLI", $con);

$result = mysql_query("SELECT * FROM projets2 order by note desc LIMIT 0,10");


echo "<table border='1'>
<tr>
<th>les premiers 10 populaires ressources </th>
<th>note </th>
<th>voir les commentaires </th>
</tr>";

while($row = mysql_fetch_array($result))
  {
echo "<tr>";
 echo "<td>" . "<a href=\"{$row['url']}\" title=\"This is the link\">    {$row['title']}   </a>" . "</td>";
 echo "<td>" . $row['note'] . "</td>";
$q=$row['urlid'];
 echo "<td>" . "<a href=\"allcommentaire.php?urlid=$q\" title=\"This is the link\">    'consult'   </a>" . "</td>";
 echo "</tr>";



 "<br />";
  
  
  echo "<br />";
  }
  echo "</table>";

mysql_close($con);
?>
