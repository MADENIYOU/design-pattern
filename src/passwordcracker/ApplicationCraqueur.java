package passwordcracker;

public class ApplicationCraqueur {
    public static void main(String[] args) {
        String typeAttaque = null;
        String typeCible = null;
        String nomUtilisateur = null;
        String urlCible = null;
        String cheminDictionnaire = "dictionnaire.txt"; // Chemin par défaut

        // Analyse des arguments
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--type":
                    if (i + 1 < args.length) typeAttaque = args[++i];
                    break;
                case "--target":
                    if (i + 1 < args.length) typeCible = args[++i];
                    break;
                case "--login":
                    if (i + 1 < args.length) nomUtilisateur = args[++i];
                    break;
                case "--url":
                    if (i + 1 < args.length) urlCible = args[++i];
                    break;
                case "--dict":
                    if (i + 1 < args.length) cheminDictionnaire = args[++i];
                    break;
            }
        }

        if (typeAttaque == null || typeCible == null || nomUtilisateur == null) {
            System.out.println("Utilisation : java -cp src passwordcracker.ApplicationCraqueur --type <bruteforce|dictionnaire> --target <local|en_ligne> --login <nom_utilisateur> [--url <url_cible>] [--dict <chemin_fichier>]");
            return;
        }

        FabriqueCraqueur fabrique = null;

        if ("bruteforce".equalsIgnoreCase(typeAttaque) && "local".equalsIgnoreCase(typeCible)) {
            fabrique = new FabriqueBruteForceLocale();
        } else if ("dictionnaire".equalsIgnoreCase(typeAttaque) && "en_ligne".equalsIgnoreCase(typeCible)) {
            if (urlCible == null) {
                System.out.println("Erreur : --url est requis pour les cibles en ligne.");
                return;
            }
            fabrique = new FabriqueDictionnaireEnLigne(urlCible, cheminDictionnaire);
        } else {
            System.out.println("Combinaison non supportée de type d'attaque et de type de cible.");
            return;
        }

        Cible cible = fabrique.creerCible();
        StrategieAttaque strategie = fabrique.creerStrategieAttaque();

        System.out.println("\nConfiguration de l'attaque :");
        System.out.println("  - Type d'attaque : " + typeAttaque);
        System.out.println("  - Type de cible   : " + typeCible);
        System.out.println("  - Nom utilisateur: " + nomUtilisateur);
        if (urlCible != null) {
            System.out.println("  - URL Cible       : " + urlCible);
        }
        if ("dictionnaire".equalsIgnoreCase(typeAttaque)) {
            System.out.println("  - Dictionnaire  : " + cheminDictionnaire);
        }

        strategie.craquer(cible, nomUtilisateur);
    }
}