<!DOCTYPE html>
<html>
<body>
<?php
if (authenticated_user())
{
$authorized = true;
}
if ($authorized) {
include 'connexion.php'; }
?>
</body>
</html>
