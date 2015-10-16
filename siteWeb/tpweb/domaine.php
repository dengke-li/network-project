<!DOCTYPE html>
<script language="javascript">  
    
        //获取接受返回信息层  
        var msg = document.getElementById("msg");  
      
        //获取表单对象和用户信息值  
        //var f = document.sent;  
        //var pg = f.pg.value;  
        //var userAge   = f.user_age.value;  
        //var userSex   = f.user_sex.value;  
        var pg=new Date();
        //接收表单的URL地址  
        var url = "log.php";  
      
        //需要POST的值，把每个变量都通过&来联接  
        var postStr   = "pg="+ pg;  
        //var postStr   = "user_name="+ userName +"&user_age="+ userAge +"&user_sex="+ userSex;  
      
        //实例化Ajax  
        //var ajax = InitAjax();  
      
        var ajax = false;  
        //开始初始化XMLHttpRequest对象  
        if(window.XMLHttpRequest)   
        {   //Mozilla 浏览器  
            ajax = new XMLHttpRequest();  
            if (ajax.overrideMimeType)   
            {   //设置MiME类别  
                ajax.overrideMimeType("text/xml");  
            }  
        }  
        else if (window.ActiveXObject)   
        {   // IE浏览器  
            try   
            {  
                ajax = new ActiveXObject("Msxml2.XMLHTTP");  
            }   
            catch (e)   
            {  
                try   
                {  
                    ajax = new ActiveXObject("Microsoft.XMLHTTP");  
                }   
                catch (e) {}  
            }  
        }  
        if (!ajax)   
        {   // 异常，创建对象实例失败  
            window.alert("不能创建XMLHttpRequest对象实例.");  
            return false;  
        }  
                      
        //通过Post方式打开连接  
        ajax.open("POST","log.php?pg="+pg, true);  
      
        //定义传输的文件HTTP头信息  
        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded");  
      
        //发送POST数据  
        ajax.send();  
      
        //获取执行状态  
        ajax.onreadystatechange = function()   
        {   
            //如果执行状态成功，那么就把返回信息写到指定的层里  
            if (ajax.readyState == 4 && ajax.status == 200)   
            {   
                msg.innerHTML = ajax.responseText;   
            }   
        }   
    
    </script>    
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

$result = mysql_query("SELECT DISTINCT domaine FROM projets2");


echo "<table border='1'>
<tr>
<th>categorie de domaines</th>


</tr>";


while($row = mysql_fetch_array($result))
  {
$domain=$row['domaine'];
echo  $row['cours'];
 echo "<tr>";
echo "<td>" . "<a href=\"infotable.php?domaine=".$row['domaine']."\" >$domain
   </a>" . "</td>";
 //echo "<td>" . "<a href=\"{http://web-tp.svc.enst-bretagne.fr/~dli/tableau0.php?cours='+encodeURIComponent($row['cours'])}\" title=\"This is the link\">    {$row['cours']}   </a>" . "</td>";
 

 echo "</tr>";
 "<br />";
  
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


</body>
</html>
