package passwordcracker;

public class FabriqueDictionnaireEnLigne implements FabriqueCraqueur {
    private final String urlCible;
    private final String cheminDictionnaire;

    public FabriqueDictionnaireEnLigne(String urlCible, String cheminDictionnaire) {
        this.urlCible = urlCible;
        this.cheminDictionnaire = cheminDictionnaire;
    }

    @Override
    public StrategieAttaque creerStrategieAttaque() {
        return new AttaqueDictionnaire(cheminDictionnaire);
    }

    @Override
    public Cible creerCible() {
        return new CibleEnLigne(urlCible);
    }
}
