package passwordcrackerfactory;

public class FabriqueDictionnaireEnLigne implements FabriqueCraqueur {
    private String url;

    public FabriqueDictionnaireEnLigne(String url) {
        this.url = url;
    }

    @Override
    public StrategieAttaque creerStrategieAttaque() {
        return new AttaqueDictionnaire();
    }

    @Override
    public Cible creerCible() {
        return new CibleEnLigne(url);
    }
}
