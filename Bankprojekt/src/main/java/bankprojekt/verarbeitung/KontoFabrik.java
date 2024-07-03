package bankprojekt.verarbeitung;

/**
 * Fabrik Klasse für Kontoarten
 */
public abstract class KontoFabrik {
    /**
     * Methode zum erstellen von Konten
     * @param inhaber Kunde dem das Konto gehoert
     * @param kontonummer kontonummer zum Konto
     * @return das erstellte Konto
     */
    public abstract Konto kontoErstellen(Kunde inhaber, long kontonummer);
}
