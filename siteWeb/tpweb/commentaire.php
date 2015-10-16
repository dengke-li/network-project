<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr" lang="fr">
    <head>
        <title>Commentaireda</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
    </head>
    <body>
               
        <?php
		/* Création table comment
		Create table comment (
		id INT Auto_Increment Primary,
		pseudo TEXT ,
		commentaire Text,
		note INT);*/
		
		function EnregistreCommentaire($pseudo,$commentaire,$note)
		{
			try
			{
			$bdd = new PDO('mysql:host=mysql-tp.svc.enst-bretagne.fr;dbname=DLI', 'root', '');
			}
			catch(Exception $e)
			{
				die('Erreur : '.$e->getMessage());
			}
 
			$req = $bdd->exec('INSERT INTO comment (pseudo,commentaire,note) VALUES(\''.$pseudo.'\', \''.$commentaire.'\', \''.$note.'\')');
			 
		}
		function CalculMoyenne()
		{
			try
			{
			$bdd = new PDO('mysql:host=localhost;dbname=test', 'root', '');
			}
			catch(Exception $e)
			{
				die('Erreur : '.$e->getMessage());
			}
			$reponse = $bdd->query('SELECT count(id) FROM comment');
			$nbPersonne = $reponse->fetch();
			$reponse = $bdd->query('SELECT sum(note) FROM comment');
			$sommeNote=$reponse->fetch();
			$reponse->closeCursor();
			return $sommeNote['sum(note)']/$nbPersonne['count(id)'];
		}
		EnregistreCommentaire('mok','mok',7);
		//$moy=CalculMoyenne();
		echo 'Note moyenne '.$moy.' .' ;
		?>
    </body>
</html>
