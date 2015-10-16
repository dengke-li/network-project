<!DOCTYPE html>
<html>

<body>
<?php
$comments='';
$con = mysql_connect("mysql-tp.svc.enst-bretagne.fr","dli","2347124");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("DLI", $con);

$result = mysql_query("SELECT * FROM projets2 where cours='basedonnee'");
echo "<table border='1'>
<tr>
<th>ressource</th>
<th>note</th>
<th>commentaire</th>

</tr>";

while($row = mysql_fetch_array($result))
  {
  
 echo "<tr>";
 echo "<td>" . "<a href=\"{$row['url']}\" title=\"This is the link\">    {$row['url']}   </a>" . "</td>";
 echo "<td>" . $row['note'] . "</td>";
 echo "<td>" . $row['commentaire'] . "</td>";
 echo "</tr>";
 $comments= $comments.$row['commentaire']."<br />";
  
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

<input   type=button   value=commentaire   onclick= 'test()'> 
<script   language=javascript>
  function   test(){
  document.write("<?php echo $comments; ?>"); 
  var k=0;
  return k;  
  }
  </script>
</body>
</html>
