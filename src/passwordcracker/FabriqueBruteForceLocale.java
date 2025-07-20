package passwordcracker;

public class FabriqueBruteForceLocale implements FabriqueCraqueur {

    @Override
    public StrategieAttaque creerStrategieAttaque() {
        // Alphabet et longueur pour la force brute
        char[] alphabet = genererAlphabet(true, true, true, true);
        return new AttaqueBruteForce(alphabet, 7, 7);
    }

    @Override
    public Cible creerCible() {
        return new CibleLocale();
    }

    private static char[] genererAlphabet(boolean minuscules, boolean majuscules, boolean chiffres, boolean symboles) {
        StringBuilder sb = new StringBuilder();
        if (minuscules) for (char c = 'a'; c <= 'z'; c++) sb.append(c);
        if (majuscules) for (char c = 'A'; c <= 'Z'; c++) sb.append(c);
        if (chiffres) for (char c = '0'; c <= '9'; c++) sb.append(c);
        if (symboles) sb.append("!@#$%^&*()-_=+[]{}|;:,.<>?/");
        return sb.toString().toCharArray();
    }
}
