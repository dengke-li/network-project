<?php
$q=$_GET["q"];

$con = mysql_connect("mysql-tp.svc.enst-bretagne.fr","dli","2347124");
if (!$con)
 {
 die('Could not connect: ' . mysql_error());
 }

mysql_select_db("DLI", $con);

$result = mysql_query("SELECT * FROM list_ressource");

echo "<table border='1'>
<tr>
<th>id</th>
<th>ressource</th>
<th>note</th>
<th>commentaire</th>

</tr>";

while($row = mysql_fetch_array($result))
 {
 echo "<tr>";
 echo "<td>" .  $row['id'] . "</td>";
 echo "<td>" . "<a href=\"{$row['ressource']}\" title=\"This is the link\">    {$row['ressource']}   </a>" . "</td>";
 echo "<td>" . $row['note'] . "</td>";
 echo "<td>" . $row['commentaire'] . "</td>";
 echo "</tr>";
 }
echo "</table>";

mysql_close($con);
?>
