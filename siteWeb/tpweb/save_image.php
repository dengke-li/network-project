<?
$conn=mysql_connect("localhost","dbuser","dbpwd");
mysql_db_select("test",$conn);
?>
<?php
$id=$_POST['id'];
$check_sql="select * from image where id = 'id'";
$check_re=mysql_query($check_sql);
$num=mysql_num_rows($check_re);
if($num!=0){
  echo "<table width='100%' align=center><tr><td align=center>";
 echo "该用户名已经存在，请选择另一个<br>";
    echo "<font color=red>上传失败！</font><br><a href='javascript:history.back(-1)'>返回</a>";
    echo "</td></tr></table>";
 exit();
}
