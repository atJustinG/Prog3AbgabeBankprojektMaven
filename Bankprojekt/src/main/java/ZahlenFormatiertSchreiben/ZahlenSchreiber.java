package ZahlenFormatiertSchreiben;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * simple Klasse die Zahlen in eine Text datei schreiben soll und diese verschieden formatieren
 */
public class ZahlenSchreiber {
    public static void main(String[] args) {
        double zahlKomma = 11.02365879;
        double zahlKomma2 = 5.4206;
        int zahl = 420;
        int zahl2 = 169;

        String dateiname = "zahrlenFormatiert.txt";

        PrintWriter pw = null;

        try{
            FileWriter fw = new FileWriter(dateiname);
            pw = new PrintWriter(fw);
            {
                pw.printf("Erste Zahl: %.6f, zweite Zahl: %.2f%%", zahlKomma, zahlKomma2);
                pw.printf("dritte Zahl: %d, Vierte Zahl: %d%n", zahl, zahl2);
                pw.printf("weitere Zahlen: %.1f, %04d%%", zahlKomma2 * 1000, zahl2);

                pw.printf("Erste Zahl (Breite 10, 2 Dezimalstellen): %10.2f%n", zahlKomma);
                pw.printf("Zweite Zahl (Breite 10, 6 Dezimalstellen): %10.6f%n", zahlKomma2);
                pw.printf("Dritte Zahl (Breite 5): %5d%n", zahl);
                pw.printf("Vierte Zahl (mit führenden Nullen): %04d%n", zahl2);
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if (pw != null) {
                pw.close();
            }
        }
    }
}
