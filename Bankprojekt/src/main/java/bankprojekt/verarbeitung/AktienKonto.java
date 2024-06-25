package bankprojekt.verarbeitung;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Klasse Aktienkonto die Aktiendepots sowie deren Kontostand managen soll
 */
public class AktienKonto extends Konto {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> aktienDepot;
    private ExecutorService executor;
    private final Lock lock = new ReentrantLock();

    /**
     * konsturktor der das Konto mit einem Inhaber und einer Kontonummer initialisiert
     *
     * @param inhaber     Kunde dem das Konto gehört
     * @param kontoNummer kontonummer zum Konto
     */
    public AktienKonto(Kunde inhaber, long kontoNummer) {
        super(inhaber, kontoNummer);
        this.aktienDepot = new HashMap<>();
        this.executor = Executors.newCachedThreadPool(Thread.ofPlatform().daemon().factory());
    }

    /**
     * Methode die eine bestimmte anzahl an Aktien kauft sobald der Kurs unter einem bestimmten Preis fällt
     *
     * @param wkn          Wertpapierkennnummer der Aktie
     * @param anzahl       Anzahl der zu kaufenden Aktien
     * @param hoechstpreis der maximale Preis zu dem die Aktie gekauft werden soll
     * @return den Gesamtpreis der gekauften Aktien oder 0.0 wenn eine Aktie nicht existiert, Das Konto nicht gedeckt oder gesperrt ist
     */
    public Future<Double> kaufauftrag(String wkn, int anzahl, double hoechstpreis){
        return CompletableFuture.supplyAsync(() -> {
            Aktie aktie = Aktie.getAktie(wkn);
            if (aktie == null) {
                return 0.0;
            }
            try {
                aktie.warteBisKursUnter(hoechstpreis);
            } catch (InterruptedException e) {
                return 0.0;
            }
            double gesamtkaufpreis = aktie.getKurs() * anzahl;
            try {
                lock.lock();
                if (abheben(gesamtkaufpreis)) {
                    aktienDepot.compute(wkn, (wknIgnored, bestand) -> {
                        if (bestand != null) {
                            return anzahl + bestand;
                        } else
                            return anzahl;
                    });
                    return gesamtkaufpreis;
                } else {
                    return 0.0;
                }
            } catch (GesperrtException e) {
                return 0.0;
            } finally {
                lock.unlock();
            }
        }, executor);
    }

    /**
     * Methode die alle Aktien einer bestimmten Wertpapiernummer verkauft sobald der Kurs unter einem bestimmten Preis fällt
     *
     * @param wkn          Wertpapierrnummer der Aktie
     * @param minimalpreis minimalpreis der Aktie
     * @return den Gesamterloes der verkauften Aktien oder 0.0 wenn eine Aktie nicht existiert, Das Konto nicht gedeckt oder gesperrt ist
     */
    public Future<Double> verkaufauftrag(String wkn, double minimalpreis) {
        return CompletableFuture.supplyAsync(() -> {

            if (!aktienDepot.containsKey(wkn)) {
                return 0.0;
            }

            Aktie aktie = Aktie.getAktie(wkn);
            if (aktie == null) {
                return 0.0;
            }
            try {
                aktie.warteBisKursUeber(minimalpreis);
            } catch (InterruptedException e) {
                return 0.0;
            }
            try {
                lock.lock();
                double gesamterloes = aktie.getKurs() * aktienDepot.remove(wkn);
                einzahlen(gesamterloes);
                return gesamterloes;
            } finally {
                lock.unlock();
            }
        }, executor);
    }

    /**
     * abheben Methode den Kontostand anpasst beim kaufen einer Aktie solange das Konto gedeckt ist, nicht Gesperrt, und der Betrag nicht unter 0.0 ist
     *
     * @param betrag Betrag der abgehoben werden soll
     * @return true wenn die Abhebung erfolgreich war bei einem gedeckten Konto false wenn sie nicht erfolgreich war
     * @throws GesperrtException
     */
    @Override
    public boolean abheben(double betrag) throws GesperrtException {
        if (betrag < 0 || this.isGesperrt() || getKontostand() < betrag) {
            return false;
        }
        setKontostand(getKontostand() - betrag);
        return true;
    }


}
