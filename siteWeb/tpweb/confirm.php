<html>
<head>
<title>index</title>
</head>
<body>
<h2 align="center">user registration confirmation page.</h2>
	<form name="registration" method="post" action="output.php"
enctype="multipart/form-data">
	<table width="300" border="1" align="center" cellpadding="5"
cellspacing="0" bgcolor="#EEEEFF">
	
	
	<tr>
	<td width="47%" align="right"> <B> full name </B> </td>
	<td height="2">
<?php
$name=$_POST['name'];
	if(empty($name))
	{die("NO Name submitted");
	}
	elseif ((strlen($name)<4)||(strlen($name)>20))
	{
	die("Invalid name");
	}
	else
	{echo $name;
	}
?>
	</td>
	</tr>
	<tr>
	<td width="47%" align="right"> <B> user name </B> </td>
	<td height="2">
<?php
$username=$_POST['username'];
	if(empty($username))
	{die("NO userName submitted");
	}
	elseif ((strlen($username)<4)||(strlen($username)>20))
	{
	die("Invalid username");
	}
	else
	{echo $username;
	}
?>
	</td>
	</tr>

	<tr>
	<td width="47%" align="right"> <B> address </B> </td>
	<td height="2">
<?php
$address=$_POST['address'];
if(empty($address))
	{die("NO address submitted");
	}
	elseif ((strlen($address)<4)||(strlen($address)>50))
	{
	die("Invalid address");
	}
	else
	{echo $address;
	}
?>
	</td>
	</tr>
	<tr>
	<td width="47%" align="right"> <B> email </B> </td>
	<td height="2">
<?php
$email=$_POST['email'];

if(empty($email))
	{die("NO e-mail address submitted");
	}
	elseif ((strlen($email)<5)||(strlen($email)>20))
	{
	die("Invalid e-mail address,e-mail address too long or short.");
	}
	elseif(!ereg("@",$email)){
	die("Invalid e-mail address,no @ symbol found");
	}
	else
	{echo $email;
	}
?>
	</td>
	</tr>
	<tr>
	<td width="47%" align="right"> <B> password </B> </td>
	<td height="2">
<?php
$password=$_POST['password'];
$cpassword=$_POST['cpassword'];
if(empty($password)||empty($cpassword))
	{die("NO password submitted");
	}
	elseif ((strlen($password)<5)||(strlen($password)>15))
	{
	die("Invalid password longth");
	}
	elseif(!(strlen($password)==strlen($cpassword))){
	die("passwords do not match !");	
	}
	elseif(!($password==$cpassword)){
	die("passwords do not match !");	
	}
	else{
	for($i=0;$i<strlen($password);$i++)
	{echo "*";
	}
	}
?>

	<center> <input type="submit" name="Submit" value="confirm >>">
	</center>
	</form>
	</td>
	</tr>
	</table>
	</form>
<?php

print "Thanks for inscrit ! <br /><br />";
$name =$_POST['name'];  // filtre contre les SQL Injection

$address =$_POST['address']; 
$username =$_POST['username'];
$email = $_POST['email'];
$password = $_POST['password'];


$con=mysql_connect("mysql-tp.svc.enst-bretagne.fr","dli","2347124");
// Check connection
if (mysql_errno())
  {
  echo "Failed to connect to MySQL: " . mysql_connect_error();
  }
mysql_select_db("DLI", $con);

$sql="INSERT INTO customers(username,password,fullname,email)
VALUES
('$username','$password','$name','$email')";

if(!mysql_query($sql,$con))
  {
  die('Error: ' . mysql_error($con));
  }
mysql_close($con);

?>
	</body>
	</html>
