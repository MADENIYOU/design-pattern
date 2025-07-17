package passwordcrackerfactory;

public class AttaqueBruteForce implements StrategieAttaque {
    @Override
    public boolean craquerMotDePasse(String nomUtilisateur, String motDePasseACraquer) {
        System.out.println("Exécution de l'attaque par force brute pour l'utilisateur : " + nomUtilisateur);
        // Ceci est un exemple simplifié. Une vraie attaque par force brute itérerait sur les combinaisons.
        // Pour la démonstration, nous allons simplement "trouver" le mot de passe s'il est "password123"
        if (motDePasseACraquer.equals("password123")) {
            System.out.println("Mot de passe trouvé par force brute : password123");
            return true;
        }
        System.out.println("L'attaque par force brute n'a pas réussi à trouver le mot de passe.");
        return false;
    }
}
