# Usine de Cassage de Mots de Passe

Ce projet implémente des techniques de cassage de mots de passe en utilisant le patron de conception **Fabrique**, tel que spécifié dans le document "Mini-projet-Password-Cracking.pdf". Il démontre des attaques par force brute et par dictionnaire contre des cibles locales et en ligne.

## Structure du Projet

Le projet est organisé autour du patron de conception **Méthode de Fabrique** (ou Fabrique Abstraite), permettant une création flexible des stratégies d'attaque et des cibles.

*   `StrategieAttaque.java`: Interface définissant le contrat pour les stratégies de cassage de mots de passe.
*   `AttaqueBruteForce.java`: Implémentation concrète de `StrategieAttaque` qui simule une attaque par force brute.
*   `AttaqueDictionnaire.java`: Implémentation concrète de `StrategieAttaque` qui simule une attaque par dictionnaire.
*   `Cible.java`: Interface définissant le contrat pour les cibles d'authentification.
*   `CibleLocale.java`: Implémentation concrète de `Cible` pour la simulation d'authentification locale.
*   `CibleEnLigne.java`: Implémentation concrète de `Cible` pour la simulation d'authentification en ligne (les requêtes HTTP sont simulées).
*   `FabriqueCraqueur.java`: Interface définissant la fabrique abstraite pour créer des objets `StrategieAttaque` et `Cible`.
*   `FabriqueBruteForceLocale.java`: Fabrique concrète qui produit une `AttaqueBruteForce` et une `CibleLocale`.
*   `FabriqueDictionnaireEnLigne.java`: Fabrique concrète qui produit une `AttaqueDictionnaire` et une `CibleEnLigne`.
*   `ApplicationCraqueur.java`: La classe d'application principale qui analyse les arguments de la ligne de commande, utilise la fabrique appropriée pour créer des objets d'attaque et de cible, et exécute le processus de cassage.

## Comment Compiler et Exécuter

1.  **Naviguer vers le répertoire du projet :**
    ```bash
    cd C:\Users\menis\OneDrive\Documents\MES_FICHIERS_DIC\MES FICHIERS DIC1\SEMESTRE 2\PATRON DE CONCEPTION\TP\password-cracker-factory
    ```

2.  **Compiler les fichiers Java :**
    ```bash
    javac *.java
    ```

3.  **Exécuter l'application :**

    *   **Exemple 1 : Attaque par Force Brute sur une Cible Locale**
        (En supposant que le mot de passe de la cible locale est "password123")
        ```bash
        java ApplicationCraqueur --type bruteforce --target local --login testuser --password password123
        ```

    *   **Example 2 : Attaque par Dictionnaire sur une Cible en Ligne**
        (En supposant que le mot de passe de la cible en ligne est "onlinepass" pour l'utilisateur "admin")
        ```bash
        java ApplicationCraqueur --type dictionnaire --target en_ligne --login admin --password onlinepass --url http://example.com/auth
        ```

    *   **Aide/Utilisation :**
        ```bash
        java ApplicationCraqueur
        ```
        (Ceci affichera le message d'utilisation si les arguments requis sont manquants.)

## Patrons de Conception Utilisés

*   **Patron de Méthode de Fabrique (ou Fabrique Abstraite) :** Utilisé pour créer des familles d'objets liés (`StrategieAttaque` et `Cible`) sans spécifier leurs classes concrètes. Cela permet à l'application d'être facilement étendue avec de nouveaux types d'attaques ou de cibles sans modifier la logique principale.

## Variantes Implémentées

*   **Types d'Attaque :**
    *   **Force Brute :** Tente de casser un mot de passe en essayant systématiquement toutes les combinaisons possibles de caractères. (Simplifié pour la démonstration).
    *   **Dictionnaire :** Tente de casser un mot de passe en essayant une liste de mots ou de phrases courants à partir d'un dictionnaire. (Simplifié avec un petit dictionnaire codé en dur).
*   **Types de Cible :**
    *   **Cible Locale :** Simule l'authentification contre un système local (par exemple, un nom d'utilisateur/mot de passe codé en dur).
    *   **Cible en Ligne :** Simule l'authentification contre un service en ligne (par exemple, un formulaire web).

## Architecture (Description du Diagramme de Classes UML)

L'architecture suit le patron de **Fabrique Abstraite** (ou Méthode de Fabrique, selon l'interprétation, car `FabriqueCraqueur` crée une famille d'objets liés).

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

Cette structure permet d'ajouter facilement de nouveaux types d'attaques (par exemple, Attaque par Table Arc-en-ciel) ou de nouveaux types de cibles (par exemple, Cible de Base de Données) en créant simplement de nouvelles implémentations concrètes de `StrategieAttaque` et `Cible`, puis une nouvelle `FabriqueCraqueur` concrète pour les combiner, sans modifier le code existant.