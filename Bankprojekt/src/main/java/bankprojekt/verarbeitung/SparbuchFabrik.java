package bankprojekt.verarbeitung;

/**
 * Klasse um Sparbuecher zu erstellen
 */
public class SparbuchFabrik extends KontoFabrik{

    /**
     * Methode um ein Sparbuch zu erstellen
     * @param inhaber Kunde dem das Konto gehoert
     * @param kontonummer kontonummer zum Konto
     * @return das erstellte Sparbuch
     */
    @Override
    public Konto kontoErstellen(Kunde inhaber, long kontonummer) {
        return new Sparbuch(inhaber, kontonummer);
    }
}
