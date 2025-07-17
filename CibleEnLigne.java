package passwordcrackerfactory;

public class CibleEnLigne implements Cible {
    private String url;

    public CibleEnLigne(String url) {
        this.url = url;
    }

    @Override
    public boolean authentifier(String nomUtilisateur, String motDePasse) {
        System.out.println("Tentative d'authentification en ligne à " + url + " pour l'utilisateur : " + nomUtilisateur);
        // Simule l'authentification en ligne. Dans un scénario réel, cela impliquerait des requêtes HTTP.
        // Pour la démonstration, supposons une authentification réussie pour des identifiants spécifiques.
        if (nomUtilisateur.equals("admin") && motDePasse.equals("onlinepass")) {
            return true;
        }
        return false;
    }
}
