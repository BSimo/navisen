# Navisen Project

Navisen est une Application Web d'aide à la navigation dans les locaux de l'ISEN Brest.
Développé par Simonneaux Benoît & Trotin Eddy, c'est aussi notre projet de fin d'année de CIR2 en cette même école.

## Prérequis du système

Cette application est developpé en PHP ainsi qu'en Java.
Voici les recommendations du système afin de pouvoir lancer l'application.

- Apache2 & PHP 5.5
- MySQL (ainsi qu'une base de données dédié)
- Java JRE 1.7 (ou le JDK pour compiler)

## Configuration

### Configuration du Serveur JAVA

L'executable JAR du serveur est disponible dans _server/_.
Les informations de la base de données ainsi que le port du serveur sont à renseigner dans le fichier _server/config.properties_.

#### Compilation

```
cd server/
find -name *.java > FilesList.txt
mkdir out
javac @FilesList.txt -cp ".:lib/json-20160212.jar:lib/commons-codec-1.10.jar:lib/commons-validator-1.5.0.jar:lib/mysql-connector-java-5.1.39-bin.jar" -d out
jar cmvf META-INF/MANIFEST.MF NavisenServer.jar -C out .
```

#### Execution

```
java -jar NavisenServer.jar
```

### Configuration d'Apache2

NavisenProject necessite un VirtualHost dédié dont le DocumentRoot pointe vers web/public.
Voici un exemple de configuration :

```
<VirtualHost *:80>
	ServerName navisen.dev
	DocumentRoot /srv/navisen/web/public
	<Directory  "/srv/navisen/web/public/">
		Options Indexes FollowSymLinks MultiViews
		AllowOverride All
	</Directory>
</VirtualHost>
```

> N'oubliez pas de modifier votre /etc/hosts si vous utilisez un sous domaine personnalisé !

## Execution de l'application

Il faut d'abord exécuter le serveur Java avant de se rendre sur l'appli web. Sans quoi, la recherche de chemin sera impossible !
