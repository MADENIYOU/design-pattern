# Password Cracker Factory

Ce projet est une application en ligne de commande permettant de réaliser des attaques de cassage de mot de passe. Il a été conçu en mettant l'accent sur une architecture logicielle modulaire et extensible, en s'appuyant sur des patrons de conception fondamentaux.

L'application permet de lancer deux types d'attaques :
1.  **Attaque par Force Brute :** Teste systématiquement toutes les combinaisons de caractères possibles. Cette implémentation est hautement optimisée grâce au multithreading pour utiliser tous les cœurs du processeur.
2.  **Attaque par Dictionnaire :** Teste une liste de mots de passe potentiels à partir d'un fichier.

Ces attaques peuvent être dirigées contre deux types de cibles :
1.  **Cible Locale :** Une simulation d'authentification locale ultra-rapide, entièrement réalisée en mémoire.
2.  **Cible en Ligne :** Une véritable cible qui effectue des requêtes HTTP POST vers un formulaire de connexion web.

## Architecture et Patrons de Conception

L'architecture du projet repose sur l'interaction de trois patrons de conception majeurs qui assurent le découplage, la flexibilité et l'extensibilité du code.

### 1. Patron de Conception Fabrique Abstraite (Abstract Factory)

C'est le pilier de l'architecture. Il fournit une interface pour créer des **familles d'objets liés** sans avoir à spécifier leurs classes concrètes.

*   **Rôle dans le projet :**
    *   L'interface `FabriqueCraqueur` définit le contrat pour toutes les fabriques. Chaque fabrique doit savoir comment créer une `StrategieAttaque` et une `Cible`.
    *   Les classes `FabriqueBruteForceLocale` et `FabriqueDictionnaireEnLigne` sont les implémentations concrètes. Leur responsabilité est de créer une paire d'objets cohérents et conçus pour fonctionner ensemble (par exemple, une attaque par dictionnaire avec une cible en ligne).
*   **Avantages :**
    *   **Cohérence :** Garantit que les objets créés sont compatibles entre eux.
    *   **Extensibilité :** Pour ajouter une nouvelle combinaison (ex: force brute sur une cible en ligne), il suffit de créer une nouvelle classe `FabriqueBruteForceEnLigne` sans toucher au code client.

### 2. Patron de Conception Stratégie (Strategy)

Ce patron permet de définir une famille d'algorithmes, de les encapsuler et de les rendre interchangeables.

*   **Rôle dans le projet :**
    *   L'interface `StrategieAttaque` définit la méthode commune `craquer()`.
    *   Les classes `AttaqueBruteForce` et `AttaqueDictionnaire` sont des stratégies concrètes. Elles contiennent les algorithmes spécifiques pour chaque type d'attaque.
*   **Avantages :**
    *   **Flexibilité :** Le client (la classe `ApplicationCraqueur`) peut utiliser n'importe quelle stratégie d'attaque de manière transparente.
    *   **Lisibilité :** La logique complexe de chaque attaque est isolée dans sa propre classe, ce qui rend le code plus propre.

### 3. Patron de Conception Pont (Bridge)

Ce patron, plus subtil, découple une abstraction de son implémentation pour qu'elles puissent évoluer indépendamment.

*   **Rôle dans le projet :**
    Il connecte l'abstraction `StrategieAttaque` avec les implémentations de `Cible`. La stratégie d'attaque détient une référence vers un objet `Cible` et interagit avec lui via son interface.
*   **Avantages :**
    *   **Indépendance :** On peut ajouter de nouvelles cibles (`CibleFTP`, `CibleSSH`...) sans jamais modifier les stratégies d'attaque. Inversement, on peut créer de nouvelles stratégies qui fonctionneront avec toutes les cibles existantes.
    *   **Modularité Maximale :** C'est ce patron qui permet le plus haut niveau de découplage du projet.

## Description Détaillée des Fichiers

Le code source est entièrement contenu dans le package `passwordcracker`.

*   `ApplicationCraqueur.java`: **Point d'entrée.** Cette classe analyse les arguments de la ligne de commande (`--type`, `--target`, etc.), sélectionne la `FabriqueCraqueur` appropriée, et lui demande de créer la stratégie et la cible. Elle orchestre ensuite le lancement de l'attaque.

*   `FabriqueCraqueur.java`: **Interface de la Fabrique Abstraite.** Définit les méthodes que toutes les fabriques concrètes doivent implémenter : `creerStrategieAttaque()` et `creerCible()`.

*   `FabriqueBruteForceLocale.java`: **Fabrique Concrète.** Implémente `FabriqueCraqueur`. Elle est spécialisée dans la création d'une `AttaqueBruteForce` et d'une `CibleLocale`. C'est ici que l'on configure les paramètres de l'attaque par force brute (longueur des mots de passe, alphabet à utiliser).

*   `FabriqueDictionnaireEnLigne.java`: **Fabrique Concrète.** Implémente `FabriqueCraqueur`. Elle crée une `AttaqueDictionnaire` et une `CibleEnLigne`, en leur passant les dépendances nécessaires comme l'URL de la cible et le chemin du fichier dictionnaire.

*   `StrategieAttaque.java`: **Interface de la Stratégie.** Définit la méthode `craquer(Cible cible, String nomUtilisateur)` que toutes les stratégies d'attaque doivent fournir.

*   `AttaqueBruteForce.java`: **Stratégie Concrète.** Contient la logique de l'attaque par force brute. Elle est optimisée pour la vitesse en utilisant un `ExecutorService` pour répartir le travail sur plusieurs threads. Elle affiche également une barre de progression en temps réel.

*   `AttaqueDictionnaire.java`: **Stratégie Concrète.** Implémente l'attaque par dictionnaire. Elle lit un fichier texte (`dictionnaire.txt` par défaut) et teste chaque mot de passe contre la cible.

*   `Cible.java`: **Interface du Pont (côté implémentation).** Définit la méthode `authentifier(String nomUtilisateur, String motDePasse)` que toutes les cibles doivent implémenter.

*   `CibleLocale.java`: **Implémentation Concrète de Cible.** Simule une authentification locale. La vérification du mot de passe (`pass123` pour l'utilisateur `admin`) est faite directement en mémoire pour une performance maximale, ce qui est idéal pour tester la vitesse de l'algorithme de force brute.

*   `CibleEnLigne.java`: **Implémentation Concrète de Cible.** Simule une authentification sur un service web. Elle envoie les identifiants via une requête HTTP POST à l'URL fournie et analyse la réponse pour déterminer si l'authentification a réussi.

*   `dictionnaire.txt`: Un simple fichier texte contenant des mots de passe courants, utilisé par `AttaqueDictionnaire`.

## Explication des Imports Java

Voici le détail des packages importés dans les classes clés du projet.

#### Dans `AttaqueBruteForce.java`
*   `java.util.concurrent.*`: Ce groupe d'imports est crucial pour l'optimisation multithread.
    *   `ExecutorService`: Pour gérer un pool de threads et y soumettre des tâches.
    *   `Executors`: Une classe utilitaire pour créer facilement des `ExecutorService`.
    *   `TimeUnit`: Pour spécifier les unités de temps (utilisé lors de l'attente de la fin des threads).
    *   `AtomicBoolean`, `AtomicLong`: Des classes atomiques qui garantissent que les opérations (comme vérifier si le mot de passe est trouvé ou incrémenter un compteur) sont sûres dans un environnement multithread, évitant les erreurs de concurrence.

#### Dans `AttaqueDictionnaire.java`
*   `java.io.BufferedReader`, `java.io.FileReader`, `java.io.IOException`: Utilisés pour lire efficacement le fichier `dictionnaire.txt` ligne par ligne.
*   `java.util.ArrayList`, `java.util.List`: Pour stocker en mémoire la liste des mots de passe lus depuis le dictionnaire.

#### Dans `CibleEnLigne.java`
*   `java.io.*`: `BufferedReader`, `InputStreamReader`, `OutputStream` sont utilisés pour lire la réponse du serveur et envoyer les données du formulaire.
*   `java.net.*`: C'est le cœur de la fonctionnalité de cette classe.
    *   `HttpURLConnection`: Pour établir et gérer la connexion HTTP.
    *   `URL`: Pour représenter l'URL de la cible.
    *   `URLEncoder`: Essentiel pour formater correctement les données (`login` et `password`) afin qu'elles puissent être envoyées dans le corps d'une requête POST.
*   `java.nio.charset.StandardCharsets`: Pour garantir que l'encodage des caractères (UTF-8) est cohérent lors de l'envoi et de la réception des données HTTP.

## Compilation et Exécution

Assurez-vous d'être à la racine du projet.

#### 1. Compiler le projet
La commande suivante compile tous les fichiers `.java` et place les fichiers `.class` dans le répertoire `src`.
```bash
javac -d src -cp src src/passwordcracker/*.java
```

#### 2. Exécuter une attaque

*   **Attaque par Force Brute sur une Cible Locale**
    *(Cherche un mot de passe de 7 caractères pour l'utilisateur 'admin')*
    ```bash
    java -cp src passwordcracker.ApplicationCraqueur --type bruteforce --target local --login admin
    ```

*   **Attaque par Dictionnaire sur une Cible en Ligne**
    *(Nécessite un serveur web local avec le script `login.php` du projet)*
    ```bash
    java -cp src passwordcracker.ApplicationCraqueur --type dictionnaire --target en_ligne --login admin --url "http://localhost/ProjetPC/login.php"
    ```

*   **Utiliser un autre dictionnaire**
    ```bash
    java -cp src passwordcracker.ApplicationCraqueur --type dictionnaire --target en_ligne --login admin --url "http://localhost/ProjetPC/login.php" --dict "chemin/vers/mon_dico.txt"
    ```

## Diagramme d'Architecture

L'architecture suit le patron de **Fabrique Abstraite**. Le diagramme ci-dessous illustre les relations entre les composants clés.

```
+---------------------------------------------------------+           +--------------------------------------------+
|           <<interface>>                                 |           |           <<interface>>                    |
|         StrategieAttaque                                |<----------|             Cible                          |
|---------------------------------------------------------|           |--------------------------------------------|
| + craquerMotDePasse(nomUtilisateur, motDePasseACraquer) |           | + authentifier(nomUtilisateur, motDePasse) |
+---------------------------------------------------------+           +--------------------------------------------+
                 ^                                                                  ^
                 |                                                                  |
                 | (implémente)                                                     | (implémente)
                 |                                                                  |
+---------------------------------+                                     +---------------------------------+
|         AttaqueBruteForce       |                                     |           CibleLocale           |
|---------------------------------|                                     |---------------------------------|
| + craquerMotDePasse()           |                                     | + authentifier()                |
+---------------------------------+                                     +---------------------------------+
                 ^                                                                  ^
                 |                                                                  |
                 | (implémente)                                                     | (implémente)
                 |                                                                  |
+---------------------------------+                                     +---------------------------------+
|         AttaqueDictionnaire     |                                     |           CibleEnLigne          |
|---------------------------------|                                     |---------------------------------|
| + craquerMotDePasse()           |                                     | + authentifier()                |
+---------------------------------+                                     +---------------------------------+


+---------------------------------------------------------------------------------+
|                           <<interface>>                                         |
|                             FabriqueCraqueur                                    |
|---------------------------------------------------------------------------------|
| + creerStrategieAttaque(): StrategieAttaque                                     |
| + creerCible(): Cible                                                           |
+---------------------------------------------------------------------------------+
                 ^
                 |
                 | (implémente)
                 |
+---------------------------------------------------------------------------------+
|                       FabriqueBruteForceLocale                                  |
|---------------------------------------------------------------------------------|
| + creerStrategieAttaque(): AttaqueBruteForce                                    |
| + creerCible(): CibleLocale                                                     |
+---------------------------------------------------------------------------------+
                 ^
                 |
                 | (implémente)
                 |
+---------------------------------------------------------------------------------+
|                       FabriqueDictionnaireEnLigne                               |
|---------------------------------------------------------------------------------|
| + creerStrategieAttaque(): AttaqueDictionnaire                                  |
| + creerCible(): CibleEnLigne                                                    |
+---------------------------------------------------------------------------------+


+---------------------------------------+
|         ApplicationCraqueur           |
|---------------------------------------|
| - main(args: String[])                |
|---------------------------------------|
| + utilise(fabrique: FabriqueCraqueur) |
+---------------------------------------+
                 |
                 | (utilise/dépend de)
                 | 
                 +---------------------------------+
                 |         FabriqueCraqueur        | (via l'interface)
                 +---------------------------------+
```
