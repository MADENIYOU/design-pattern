package passwordcrackerfactory;

public interface FabriqueCraqueur {
    StrategieAttaque creerStrategieAttaque();
    Cible creerCible();
}
