package bankprojekt;

import bankprojekt.verarbeitung.Aktie;
import bankprojekt.verarbeitung.AktienKonto;
import bankprojekt.verarbeitung.GesperrtException;
import bankprojekt.verarbeitung.Kunde;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AktienSpielereien {
    public static void main(String[] args) {
        Aktie aktie1 = new Aktie("WKN001", 50.0);
        Aktie aktie2 = new Aktie("WKN002", 75.0);
        Aktie aktie3 = new Aktie("WKN003", 100.0);

        // Erstellen Sie ein AktienKonto
        AktienKonto aktienKonto = new AktienKonto(new Kunde(), 123456);

        // Einzahlen auf das Konto
        aktienKonto.einzahlen(10000.0);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            synchronized (System.out) {
                System.out.println("Aktueller Kurs von WKN001: " + aktie1.getKurs());
                System.out.println("Aktueller Kurs von WKN002: " + aktie2.getKurs());
                System.out.println("Aktueller Kurs von WKN003: " + aktie3.getKurs());
                System.out.println("---------------------------");
            }
        }, 0, 5, TimeUnit.SECONDS);

        // Kaufaufträge erteilen
        try {
            double kaufpreis1 = aktienKonto.kaufauftrag("WKN001", 10, 49.0).get();
            System.out.println("Kaufpreis für WKN001: " + kaufpreis1);
            System.out.println("Kontostand: " + aktienKonto.getKontostand());

            double kaufpreis2 = aktienKonto.kaufauftrag("WKN002", 5, 74.0).get();
            System.out.println("Kaufpreis für WKN002: " + kaufpreis2);
            System.out.println("Kontostand: " + aktienKonto.getKontostand());

            double kaufpreis3 = aktienKonto.kaufauftrag("WKN003", 2, 99.0).get();
            System.out.println("Kaufpreis für WKN003: " + kaufpreis3);
            System.out.println("Kontostand: " + aktienKonto.getKontostand());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (GesperrtException e) {
            throw new RuntimeException(e);
        }

        // Verkaufsaufträge erteilen
        try {
            double verkaufserloes1 = aktienKonto.verkaufauftrag("WKN001", 51.0).get();
            System.out.println("Verkaufserlös für WKN001: " + verkaufserloes1);
            System.out.println("Kontostand: " + aktienKonto.getKontostand());

            double verkaufserloes2 = aktienKonto.verkaufauftrag("WKN002", 76.0).get();
            System.out.println("Verkaufserlös für WKN002: " + verkaufserloes2);
            System.out.println("Kontostand: " + aktienKonto.getKontostand());
            double verkaufserloes3 = aktienKonto.verkaufauftrag("WKN003", 101.0).get();
            System.out.println("Verkaufserlös für WKN003: " + verkaufserloes3);
            System.out.println("Kontostand: " + aktienKonto.getKontostand());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Thread zum Anzeigen der aktuellen Aktienkurse

    }
}
