package bankprojekt;

import bankprojekt.verarbeitung.Aktie;
import bankprojekt.verarbeitung.AktienKonto;
import bankprojekt.verarbeitung.GesperrtException;
import bankprojekt.verarbeitung.Kunde;

import java.util.concurrent.*;

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
            System.out.println("Aktueller Kurs von 01: " + aktie1.getKurs());
            System.out.println("Aktueller Kurs von 02: " + aktie2.getKurs());
            System.out.println("Aktueller Kurs von 03: " + aktie3.getKurs());
            System.out.println("---------------------------");

        }, 0, 3, TimeUnit.SECONDS);

        Future<Double> kaufpreis1 = aktienKonto.kaufauftrag("WKN001", 10, 49.0);
        Future<Double> kaufpreis2 = aktienKonto.kaufauftrag("WKN002", 5, 74.0);
        Future<Double> kaufpreis3 = aktienKonto.kaufauftrag("WKN003", 2, 99.0);
        kaufpreis(aktie1.getWkn(), kaufpreis1);
        kaufpreis(aktie2.getWkn(), kaufpreis2);
        kaufpreis(aktie3.getWkn(), kaufpreis3);
        Future<Double> verkaufserloes1 = aktienKonto.verkaufauftrag("WKN001", 51.0);
        Future<Double> verkaufserloes2 = aktienKonto.verkaufauftrag("WKN002", 76.0);
        Future<Double> verkaufserloes3 = aktienKonto.verkaufauftrag("WKN003", 101.0);
        verkaufpreis(aktie1.getWkn(), verkaufserloes1);
        verkaufpreis(aktie2.getWkn(), verkaufserloes2);
        verkaufpreis(aktie3.getWkn(), verkaufserloes3);
        scheduler.shutdown();
    }

    /**
     * gibt den Kaufpreis aus für die übergebene Aktie
     * @param wkn Wertpapiernummer
     * @param kaufpreis Kaufpreis
     */
    public static void kaufpreis(String wkn, Future<Double> kaufpreis) {
        try {
            System.out.println("Kaufpreis für " + wkn + ": " + kaufpreis.get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * gibt den gesamterloes für die verkauften aktien wieder
     * @param wkn Wertpapiernummer
     * @param verkaufErloes gesamter verkaufserloes
     */
    public static void verkaufpreis(String wkn, Future<Double> verkaufErloes) {
        try {
            System.out.println("Verkaufserlös für" + wkn + " 01: " + verkaufErloes.get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
