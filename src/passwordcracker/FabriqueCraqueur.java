package passwordcracker;

public interface FabriqueCraqueur {
    StrategieAttaque creerStrategieAttaque();
    Cible creerCible();
}
