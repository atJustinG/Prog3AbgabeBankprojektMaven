package bankprojekt.verarbeitung;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Klasse Aktienkonto die Aktiendepots sowie deren Kontostand managen soll
 */
public class AktienKonto extends Konto{

    private Map<String, Integer> aktienDepot;
    private ScheduledExecutorService scheduler;
    private ExecutorService executor;

    /**
     * konsturktor der das Konto mit einem Inhaber und einer Kontonummer initialisiert
     * @param inhaber Kunde dem das Konto gehört
     * @param kontoNummer kontonummer zum Konto
     */
    public AktienKonto(Kunde inhaber, long kontoNummer){
        super(inhaber, kontoNummer);
        this.aktienDepot = new HashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.executor = Executors.newCachedThreadPool();
    }

    /**
     * Methode die eine bestimmte anzahl an Aktien kauft sobald der Kurs unter einem bestimmten Preis fällt
     * @param wkn Wertpapierkennnummer der Aktie
     * @param anzahl Anzahl der zu kaufenden Aktien
     * @param hoechstpreis der maximale Preis zu dem die Aktie gekauft werden soll
     * @return den Gesamtpreis der gekauften Aktien oder 0.0 wenn eine Aktie nicht existiert, Das Konto nicht gedeckt oder gesperrt ist
     * @throws GesperrtException bei gesperrtem Konto
     */
    public Future<Double> kaufauftrag(String wkn, int anzahl, double hoechstpreis) throws GesperrtException{
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
            synchronized (this) {
                try {
                    if (abheben(gesamtkaufpreis)) {
                        aktienDepot.put(wkn,aktienDepot.getOrDefault(wkn, 0) + anzahl);
                        return gesamtkaufpreis;
                    }
                } catch (GesperrtException e) {
                    return 0.0;
                }
            }
            return 0.0;
        }, executor);
    }

    /**
     * Methode die alle Aktien einer bestimmten Wertpapiernummer verkauft sobald der Kurs unter einem bestimmten Preis fällt
     * @param wkn Wertpapierrnummer der Aktie
     * @param minimalpreis minimalpreis der Aktie
     * @return den Gesamterloes der verkauften Aktien oder 0.0 wenn eine Aktie nicht existiert, Das Konto nicht gedeckt oder gesperrt ist
     */
    public Future<Double> verkaufauftrag(String wkn, double minimalpreis) {
        return CompletableFuture.supplyAsync(() -> {
            Integer anzahl;
            synchronized (this) {
                if (!aktienDepot.containsKey(wkn)) {
                    return 0.0;
                }
                anzahl = aktienDepot.get(wkn);
            }
            Aktie aktie = Aktie.getAktie(wkn);
            if (aktie == null) {
                return 0.0;
            }
            try{
                aktie.warteBisKursUeber(minimalpreis);
            }catch(InterruptedException e){
                return 0.0;
            }
            double gesamterloes = 0.0;
            synchronized(this) {
                gesamterloes = aktie.getKurs() * anzahl;
                aktienDepot.remove(wkn);
                einzahlen(gesamterloes);
            }
            return gesamterloes;
        }, executor);
    }

    /**
     * abheben Methode den Kontostand anpasst beim kaufen einer Aktie solange das Konto gedeckt ist, nicht Gesperrt, und der Betrag nicht unter 0.0 ist
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
