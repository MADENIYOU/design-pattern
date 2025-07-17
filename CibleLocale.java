package passwordcrackerfactory;

public class CibleLocale implements Cible {
    private String nomUtilisateur;
    private String motDePasse;

    public CibleLocale(String nomUtilisateur, String motDePasse) {
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
    }

    @Override
    public boolean authentifier(String nomUtilisateur, String motDePasse) {
        System.out.println("Tentative d'authentification locale pour l'utilisateur : " + nomUtilisateur);
        return this.nomUtilisateur.equals(nomUtilisateur) && this.motDePasse.equals(motDePasse);
    }
}
