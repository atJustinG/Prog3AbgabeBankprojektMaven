package bankprojekt.verarbeitung;

/**
 * Klasse um Girokontos zu erstellen
 */
public class GirokontoFabrik extends KontoFabrik{

    private double dispo;

    /**
     * Konstruktor um dispo festlegen zu können
     * @param dispo der Dispo des Kontos
     */
    public GirokontoFabrik(double dispo){
        this.dispo = dispo;
    }

    /**
     * erstellt ein Girokonto mit dem ausgewählten Dispo
     * @param inhaber Kunde dem das Konto gehoert
     * @param kontonummer kontonummer zum Konto
     * @return das erstellte Girokonto
     */
    @Override
    public Konto kontoErstellen(Kunde inhaber, long kontonummer) {
        return new Girokonto(inhaber, kontonummer, dispo);
    }
}
