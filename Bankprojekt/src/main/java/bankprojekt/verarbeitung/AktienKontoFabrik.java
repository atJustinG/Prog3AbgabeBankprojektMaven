package bankprojekt.verarbeitung;

/**
 * Aktienkonto Fabrik um Aktienkonten zu erstellen
 */
public class AktienKontoFabrik extends KontoFabrik{

    /**
     * Methode zum erstellen von Aktienkonto
     * @param inhaber Kunde dem das Konto gehoert
     * @param kontonummer kontonummer zum Konto
     * @return das erstell5te Aktienkonto
     */
    @Override
    public Konto kontoErstellen(Kunde inhaber, long kontonummer) {
        return new AktienKonto(inhaber, kontonummer);
    }
}
