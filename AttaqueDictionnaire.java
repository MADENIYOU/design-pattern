package passwordcrackerfactory;

import java.util.Arrays;
import java.util.List;

public class AttaqueDictionnaire implements StrategieAttaque {
    private List<String> dictionnaire = Arrays.asList("password", "123456", "qwerty", "admin", "password123");

    @Override
    public boolean craquerMotDePasse(String nomUtilisateur, String motDePasseACraquer) {
        System.out.println("Exécution de l'attaque par dictionnaire pour l'utilisateur : " + nomUtilisateur);
        for (String motDePasseDict : dictionnaire) {
            if (motDePasseDict.equals(motDePasseACraquer)) {
                System.out.println("Mot de passe trouvé par attaque par dictionnaire : " + motDePasseDict);
                return true;
            }
        }
        System.out.println("L'attaque par dictionnaire n'a pas réussi à trouver le mot de passe.");
        return false;
    }
}
