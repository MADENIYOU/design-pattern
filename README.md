# Password Cracker Factory

Ce projet est une application en ligne de commande permettant de réaliser des attaques de cassage de mot de passe. Il a été conçu en mettant l'accent sur une architecture logicielle modulaire et extensible, en s'appuyant sur des patrons de conception fondamentaux.

L'application permet de lancer deux types d'attaques :
1.  **Attaque par Force Brute :** Teste systématiquement toutes les combinaisons de caractères possibles. Cette implémentation est hautement optimisée grâce au multithreading pour utiliser tous les cœurs du processeur.
2.  **Attaque par Dictionnaire :** Teste une liste de mots de passe potentiels à partir d'un fichier.

Ces attaques peuvent être dirigées contre deux types de cibles :
1.  **Cible Locale :** Une simulation d'authentification locale ultra-rapide, entièrement réalisée en mémoire.
2.  **Cible en Ligne :** Une véritable cible qui effectue des requêtes HTTP POST vers un formulaire de connexion web.

## Architecture et Patrons de Conception

L'architecture du projet a été conçue pour offrir une flexibilité maximale en découplant totalement la création des stratégies d'attaque de la création des cibles. Pour ce faire, elle s'appuie sur **deux hiérarchies de Fabriques Abstraites (Abstract Factory)**.

### 1. Hiérarchie des Fabriques de Stratégies
*   **Fabrique Abstraite :** `FabriqueStrategieAttaque` (Interface)
*   **Fabriques Concrètes :**
    *   `FabriqueBruteForce` : Crée un objet `AttaqueBruteForce`.
    *   `FabriqueDictionnaire` : Crée un objet `AttaqueDictionnaire`.

### 2. Hiérarchie des Fabriques de Cibles
*   **Fabrique Abstraite :** `FabriqueCible` (Interface)
*   **Fabriques Concrètes :**
    *   `FabriqueCibleLocale` : Crée un objet `CibleLocale`.
    *   `FabriqueCibleEnLigne` : Crée un objet `CibleEnLigne`.

### Rôle des Patrons de Conception

*   **Abstract Factory :** Ce patron est utilisé deux fois. Il permet à l'application principale de décider au moment de l'exécution quelle fabrique de stratégie et quelle fabrique de cible instancier en fonction des arguments de l'utilisateur. Le code client est ainsi complètement indépendant des classes concrètes des stratégies et des cibles.

*   **Strategy :** L'interface `StrategieAttaque` et ses implémentations (`AttaqueBruteForce`, `AttaqueDictionnaire`) permettent de changer l'algorithme d'attaque à la volée.

*   **Bridge :** Ce patron relie les stratégies d'attaque aux cibles. Une stratégie contient une référence à une `Cible` et peut opérer sur n'importe quelle implémentation de cette interface, ce qui permet de combiner n'importe quelle attaque avec n'importe quelle cible.

## Description Détaillée des Fichiers

*   `ApplicationCraqueur.java`: **Point d'entrée.** Analyse les arguments, choisit les fabriques appropriées (une pour la stratégie, une pour la cible), leur demande de créer les objets, puis lance l'attaque.

*   `FabriqueStrategieAttaque.java` & `FabriqueCible.java`: **Interfaces des Fabriques Abstraites.**

*   `FabriqueBruteForce.java`, `FabriqueDictionnaire.java`, `FabriqueCibleLocale.java`, `FabriqueCibleEnLigne.java`: **Fabriques Concrètes** qui encapsulent la logique de création de chaque composant.

*   `StrategieAttaque.java` & `Cible.java`: **Interfaces** pour les stratégies et les cibles.

*   `AttaqueBruteForce.java`, `AttaqueDictionnaire.java`, `CibleLocale.java`, `CibleEnLigne.java`: **Implémentations concrètes** des stratégies et des cibles.

*   `dictionnaire.txt`: Fichier de mots de passe pour l'attaque par dictionnaire.

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
```bash
javac -d src -cp src src/passwordcracker/*.java
```

#### 2. Exécuter les 4 variantes

*   **1. Attaque par Force Brute sur Cible Locale**
    *(Cherche un mot de passe de 7 caractères pour l'utilisateur 'admin')*
    ```bash
    java -cp src passwordcracker.ApplicationCraqueur --type bruteforce --target local --login admin --length 7
    ```

*   **2. Attaque par Force Brute sur Cible en Ligne**
    ```bash
    java -cp src passwordcracker.ApplicationCraqueur --type bruteforce --target en_ligne --login admin --url "http://localhost/ProjetPC/login.php" --length 4
    ```

*   **3. Attaque par Dictionnaire sur Cible Locale**
    ```bash
    java -cp src passwordcracker.ApplicationCraqueur --type dictionnaire --target local --login admin
    ```

*   **4. Attaque par Dictionnaire sur Cible en Ligne**
    ```bash
    java -cp src passwordcracker.ApplicationCraqueur --type dictionnaire --target en_ligne --login admin --url "http://localhost/ProjetPC/login.php"
    ```

## Pistes d'amélioration

Le projet actuel est une base solide, mais plusieurs améliorations pourraient être envisagées pour le rendre encore plus puissant et réaliste.

*   **Attaques Hybrides :** Créer une nouvelle stratégie `AttaqueHybride` qui utilise d'abord un dictionnaire, puis enchaîne avec une attaque par force brute sur les mots de passe du dictionnaire en y ajoutant des chiffres ou des symboles (ex: `password` -> `password123`, `password!`).

*   **Gestion Avancée du Multithreading :** Permettre à l'utilisateur de spécifier le nombre de threads à utiliser via un argument (`--threads 8`). Pour l'attaque par dictionnaire, on pourrait également la paralléliser en divisant le fichier dictionnaire en plusieurs segments, chaque thread testant une partie de la liste.

*   **Support de Cibles Supplémentaires :** Ajouter de nouvelles implémentations de `Cible` pour d'autres protocoles, comme `CibleFTP`, `CibleSSH` ou `CibleBaseDeDonnees`. Grâce à l'architecture actuelle, cela ne nécessiterait aucune modification des stratégies d'attaque existantes.

*   **Rapports et Sauvegarde de Session :** Ajouter une option pour sauvegarder la progression d'une attaque par force brute dans un fichier, afin de pouvoir la reprendre plus tard. On pourrait également générer un rapport à la fin de l'attaque (temps écoulé, mot de passe trouvé, vitesse moyenne).

*   **Interface Graphique (GUI) :** Envelopper la logique du projet dans une interface graphique simple (avec Swing ou JavaFX) pour une utilisation plus conviviale.

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