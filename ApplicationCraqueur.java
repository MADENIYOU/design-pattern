package passwordcrackerfactory;

public class ApplicationCraqueur {
    public static void main(String[] args) {
        String typeAttaque = null;
        String typeCible = null;
        String nomUtilisateur = null;
        String motDePasseACraquer = null; // C'est le mot de passe que nous essayons de craquer
        String urlCible = null; // Pour les cibles en ligne

        // Analyser les arguments de la ligne de commande
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--type":
                    if (i + 1 < args.length) {
                        typeAttaque = args[++i];
                    }
                    break;
                case "--target":
                    if (i + 1 < args.length) {
                        typeCible = args[++i];
                    }
                    break;
                case "--login":
                    if (i + 1 < args.length) {
                        nomUtilisateur = args[++i];
                    }
                    break;
                case "--password": // Supposons qu'un mot de passe à craquer est fourni pour les tests
                    if (i + 1 < args.length) {
                        motDePasseACraquer = args[++i];
                    }
                    break;
                case "--url": // Pour les cibles en ligne
                    if (i + 1 < args.length) {
                        urlCible = args[++i];
                    }
                    break;
            }
        }

        if (typeAttaque == null || typeCible == null || nomUtilisateur == null || motDePasseACraquer == null) {
            System.out.println("Utilisation : java ApplicationCraqueur --type <bruteforce|dictionnaire> --target <local|en_ligne> --login <nom_utilisateur> --password <mot_de_passe_a_craquer> [--url <url_cible>]");
            return;
        }

        FabriqueCraqueur fabrique = null;
        Cible cible = null;

        // Déterminer quelle fabrique utiliser en fonction des arguments
        if ("bruteforce".equalsIgnoreCase(typeAttaque) && "local".equalsIgnoreCase(typeCible)) {
            // Pour une cible locale, nous devons connaître le mot de passe réel pour simuler l'authentification
            // Dans un scénario réel, ce serait le mot de passe stocké dans le système local
            // Pour cet exemple, nous supposerons que la CibleLocale est initialisée avec le mot de passe que nous voulons craquer
            fabrique = new FabriqueBruteForceLocale(nomUtilisateur, motDePasseACraquer);
        } else if ("dictionnaire".equalsIgnoreCase(typeAttaque) && "en_ligne".equalsIgnoreCase(typeCible)) {
            if (urlCible == null) {
                System.out.println("Erreur : --url est requis pour les cibles en ligne.");
                return;
            }
            fabrique = new FabriqueDictionnaireEnLigne(urlCible);
        } else {
            System.out.println("Combinaison non supportée de type d'attaque et de type de cible.");
            return;
        }

        // Créer la stratégie d'attaque et la cible en utilisant la fabrique
        StrategieAttaque strategieAttaque = fabrique.creerStrategieAttaque();
        cible = fabrique.creerCible();

        System.out.println("\nDébut du processus de cassage de mot de passe...");
        boolean craque = strategieAttaque.craquerMotDePasse(nomUtilisateur, motDePasseACraquer);

        if (craque) {
            System.out.println("Cassage de mot de passe réussi !");
        } else {
            System.out.println("Cassage de mot de passe échoué.");
        }
    }
}