package bankprojekt;

import bankprojekt.verarbeitung.Aktie;
import bankprojekt.verarbeitung.AktienKonto;
import bankprojekt.verarbeitung.GesperrtException;
import bankprojekt.verarbeitung.Kunde;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Main Klasse um Aktienkonto und Aktien interaktionen zu testen
 */
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
                System.out.println("Aktueller Kurs von 01: " + aktie1.getKurs());
                System.out.println("Aktueller Kurs von 02: " + aktie2.getKurs());
                System.out.println("Aktueller Kurs von 03: " + aktie3.getKurs());
                System.out.println("---------------------------");
            }
        }, 0, 5, TimeUnit.SECONDS);

        // Kaufaufträge erteilen
        try {
            double kaufpreis1 = aktienKonto.kaufauftrag("WKN001", 10, 49.0).get();
            System.out.println("Kaufpreis für 01: " + kaufpreis1);
            System.out.println("Kontostand: " + aktienKonto.getKontostand());

            double kaufpreis2 = aktienKonto.kaufauftrag("WKN002", 5, 74.0).get();
            System.out.println("Kaufpreis für 02: " + kaufpreis2);
            System.out.println("Kontostand: " + aktienKonto.getKontostand());

            double kaufpreis3 = aktienKonto.kaufauftrag("WKN003", 2, 99.0).get();
            System.out.println("Kaufpreis für 03: " + kaufpreis3);
            System.out.println("Kontostand: " + aktienKonto.getKontostand());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (GesperrtException e) {
            System.out.println("Konto ist gesperrt");
        }

        // Verkaufsaufträge erteilen
        try {
            double verkaufserloes1 = aktienKonto.verkaufauftrag("01", 51.0).get();
            System.out.println("Verkaufserlös für 01: " + verkaufserloes1);
            System.out.println("Kontostand: " + aktienKonto.getKontostand());

            double verkaufserloes2 = aktienKonto.verkaufauftrag("02", 76.0).get();
            System.out.println("Verkaufserlös für 02: " + verkaufserloes2);
            System.out.println("Kontostand: " + aktienKonto.getKontostand());
            double verkaufserloes3 = aktienKonto.verkaufauftrag("03", 101.0).get();
            System.out.println("Verkaufserlös für 03: " + verkaufserloes3);
            System.out.println("Kontostand: " + aktienKonto.getKontostand());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Thread zum Anzeigen der aktuellen Aktienkurse

    }
}
