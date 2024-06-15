package bankprojekt.verarbeitung;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class AktienKonto extends Konto{

    private Map<String, Integer> aktienDepot;
    private ScheduledExecutorService scheduler;
    private ExecutorService executor;

    public AktienKonto(Kunde inhaber, long kontoNummer){
        super(inhaber, kontoNummer);
        this.aktienDepot = new HashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.executor = Executors.newCachedThreadPool();
    }

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

    @Override
    public boolean abheben(double betrag) throws GesperrtException {
        if (betrag < 0 || this.isGesperrt() || getKontostand() < betrag) {
            return false;
        }
        setKontostand(getKontostand() - betrag);
        return true;
    }


}
