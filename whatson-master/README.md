WhatsOn
=================
=================

What is WhatsOn?
---------------------

WhatsOn is an API which aims at finding content information from different types such as videos, images, musics or texts according to your current feelings, determined thanks to some choices among different submitted pictures.

We also develop an android application and a web service accessible from a browser in order to use this API and turn it into an amazing application.

Developpment environnement
--------------------

It is developped in Java, using REST calls and servlets to access algoritms and machine learning processes. Data is stored in a MySQL database. The Android SDK is used to develop the android application, and JSP is used for the web service.


Installation
--------------------

Installation avec l'IDE Eclipse:
- Télécharger un serveur Apache Tomcat (http://tomcat.apache.org/download-70.cgi) le décompresser et retenir où il se trouve
- Dans Eclipse créer un nouveau serveur au runtime( Window > Preferences > Servers > Runtime Environment > Add > Apache > Apache Tomcat v7
- Cloner le projet (git clone https://github.com/alicecaron/whatson)
- Importer ce projet dans Eclipse
- Créer la base de données en local (il faut au préalable avoir une base de données MySQL), pour cela :
	- Lancer le programme Java : com.first.whatson.database avec INIT en paramêtre pour remplir la base avec les données de base (les images présentes dans le dossier images, les émotions possibles, les tags des images par émotion...)
	- Il est possible de supprimer cette base en lançant le même programme avec le paramètre DROP
	- Il est également possible de synchroniser la base avec le dossier images si celui ci est modifié par la suite (paramètre SYNCHRONIZE)
- lancer l'application sur le serveur Tomcat: run as > run on server, ajouter le module wathson sur le serveur tomcat
- Rendez vous à l'URL http://127.0.0.1:8080/whatson/connexion
- Connectez-vous avec un profil présent dans WebContent/scripts/users.txt qui a été utilisé pour initialiser la base de donnée de l'application.
(Par la suite, il sera possible de créer des utilisateurs directement sur la plateforme).



Student organization
--------------------

WhatsOn has been developped in connection with the First program created by Telecom fundation, in collaboration with french engineering schools (Telecom ParisTech, Telecom Bretagne, Telecom SudParis, Telecom ecole de Managment, ESAD Reims).




Manuel et vue d'ensemble (VF)
--------------------

Le projet est composé de deux parties bien distincte:
- whatsonServer: la partie serveur qui tourne sur un serveur Tomcat7.
- com.whatson.prototype: la partie cliente sous forme d'application android (sdk min:8)


Le serveur
---

Les packages:

- com.first.whatson.algorithms
	- EuclideanDistance	: Calcul la distance euclidienne entre deux images
	- ImagesSelection	: Selection des prochaines images à afficher en fonction de celles qui ont déjà été choisies au cours du processus de suggestion actuel
	- Matchinfo	: Permet d'obtenir la liste des contenus numériques à proposer à l'utilisateur à l'issu d'un processus de suggestions. La list des résultats est classée par ordre décroissant de correspondance entre les profiles émotionnels des sources de contenus numériques, l'historique des profils émotionnels de l'utilisateur et le profil émotionnel actuel de l'utilisateur

- com.first.whatson.connection
	- ConnexionForm	: permet d'établir la connection (nouvelle session) de l'utilisateur quand il se connecte à l'application), il y a vérification de l'identité de l'utilisateur (email/mot de passe). Si l'utillisateur est connu, il y a instanciation d'un objet de la classe User afin de détenir les informations de celui-ci au cours de la session active (les photos sur lesquelles il clique, son email pour les prochaines requêtes à l'API depuis le client par exemple...)
	- User	: objet java qui possède les informations relatives à la session active d'un utilisateur.

- com.first.whatson.database
	- Database	: s'occupe de la connection à la base de données SQL (adresse, mot de passe, connection...)

- com.first.whatson.database.objects
	- Emotion	: objet java modèle d'une émotion en base (id, emotion)
	- EmotionSpider	: Abstract class Emotional profile of something (taggued image, user profile, external content) Main attribute: emotionSpider is a map that binds each emotion with an intensity for the thing
	- FeltEmotion	: objet java modèle d'une émotion ressentie en base (id, idemotion, idfeltemotion)
	- Photo	: objet java modèle d'une image en base (id,source)
	- PhotoTag	: objet java modèle d'un tag de photo pour une émotion en base (idphoto,idemotion, intensité)
	- PhotoTags	: objet java qui possède le profil émotionel d'une image (toutes les émotions et leur intensité)
	- TextExternalContent	: objet java modèle d'un contenu textuel en base (id, url,source,auteur,titre)
	- User	: objet java modèle d'un utilisateur en base

- com.first.whatson.database.tables
	- TableEmotions	: Requêtes SQL pour interagir avec la table des émotions
	- TableExterneTags	: Requêtes SQL pour interagir avec la table des contenus externes qui sont taggués
	- TablePhotos	: Requêtes SQL pour interagir avec la table des photos
	- TablePhotosTags	: Requêtes SQL pour interagir avec la table des photos et leur tags
	- TableTextsource	: Requêtes SQL pour interagir avec la table des sources externes textuelles
	- TableUsers	: Requêtes SQL pour interagir avec la table des utilisateurs


- com.first.whatson.rest
	- RestServices	: Liste des services REST auxquels ont peut accéder grâce à un client dédié pour interagir avec ce serveur.
	(liste des requêtes possibles: obtenir les prochaines images,obtenir la playlist spotify qui match avec le profile émotionnel de l'utilisateur, obtenir la liste triée par ordre décroissant des contenus externes qui correspondent le plus au profil utilisateur actuel)

- com.first.whatson.servlet
	- LoginServlet	: Servlet permettant la connexion d'un utilisateur afin de démarrer une nouvell session
	- LogoutServlet	: Servlet permettant le déconnexio d'un utiisateur


L'application Android (client)
-----

- com.whatson.prototype
	- ActivityModel	: modèle des activités de l'application, permet de gérer de manière plus factorisée les menus dans l'application en gérant les autorisations selon l'authentification des utilisateurs
	- LoadImages	: Activité qui permet d'afficher les images au cours du processus de sélection d'images
	- MainActivity	: Activité principale qui affiche le fromulaire de connexion de l'application
- com.whatson.prototype.models
	- Image	: objet modèle d'une image en base
- com.whatson.prototype.resultDisplayers
	- CustomList	: Customisation de la liste qui affiche la liste des résultats d'une sélection d'images
	- ResultListDisplayer	: Activité qui affiche la liste décroissante des résultats qui correspondent au profil émotionnel de l'utilisateur
	- TextContentDisplayer	: Activité qui affiche un résultat de type texte
- com.whatson.prototype.resultTypes
	- Content	: classe abstraite pour les différents type de forme de contenus numériques pour les résultats
	- TextContent	: modèle pour un contenu texte pour un résultat d'une sélection d'image
- com.whatson.prototype.serverCalls
	- RemoteAuthentication	: Lancement d'un thread permettant d'authentifier un utilisateur en ouvrant une session sur le serveur distant
	- SimpleHttpClient	: Communication avec le serveur, permettant de poster et de récupérer des informations avec le serveur (envoie d'identifiants pour la connexion, envoie des appels rest vers l'API disponible sur le serveur, ce qui renvoie des JSON appropriés pour réponse et interprétation par le client)





