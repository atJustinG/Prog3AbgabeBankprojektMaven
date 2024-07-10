package bankprojekt.verarbeitung;

/**
 * Kontoobserver der beijeder Aenderung des Kontostands sich meldet
 */
public class KontoObserverImpl implements KontoObserver{
    @Override
    public void update(Konto konto, double letzterKontostand, double neuerKontostand) {
        System.out.println("Kontostand von Konto: " + konto.getKontonummer() + "wurde aktualisiert " + System.lineSeparator() +
                  "alet Kontostasnd: " + letzterKontostand + System.lineSeparator() + "neuer Kontostand: " + konto.getKontostand() );
    }
}
