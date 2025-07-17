package passwordcrackerfactory;

public class FabriqueBruteForceLocale implements FabriqueCraqueur {
    private String nomUtilisateur;
    private String motDePasse;

    public FabriqueBruteForceLocale(String nomUtilisateur, String motDePasse) {
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
    }

    @Override
    public StrategieAttaque creerStrategieAttaque() {
        return new AttaqueBruteForce();
    }

    @Override
    public Cible creerCible() {
        return new CibleLocale(nomUtilisateur, motDePasse);
    }
}
