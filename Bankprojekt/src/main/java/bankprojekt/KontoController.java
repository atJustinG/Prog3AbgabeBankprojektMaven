package bankprojekt;

import bankprojekt.oberflaeche.KontoOberflaeche;
import bankprojekt.verarbeitung.GesperrtException;
import bankprojekt.verarbeitung.Girokonto;
import bankprojekt.verarbeitung.Kunde;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Kontroller Klasse um mit der Konto Oberflaeche zu kommunizieren und zu steuern
 */
public class KontoController extends Application {
    Girokonto konto;
    private KontoOberflaeche kOberflaeche;

    /**
     * start Methode startet die Anwendung
     * @param stage visualisiert das Fenster
     * @throws Exception bei Fehler wirft eine Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        konto = new Girokonto(new Kunde(), 123456789, 500);
        kOberflaeche = new KontoOberflaeche(konto, this);

        Scene scene = new Scene(kOberflaeche, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Konto Verwaltung");
        stage.show();
    }

    /**
     * einzahlen Methode eventhandler per Knopfdruck der Oberflaeche ins Konto einzuzahlen
     * @param betrag einzuzahlender Betrag
     * @param meldung Meldung bei Fehlverhalten
     */
    public void einzahlenEvent(double betrag, Text meldung){
        konto.einzahlen(betrag);
    }

    /**
     * abheben Methode eventhandler per Knopfdruck der Oberflaeche vom Konto abzuheben
     * @param betrag abzuhebender Betrag
     * @param meldung Meldung bei Fehlverhalten
     * @throws NumberFormatException wirft bei falscher eingabe eine NumberformatException
     */
    public void abhebenEvent(double betrag, Text meldung) throws NumberFormatException {
        try{
            konto.abheben(betrag);
        }catch(GesperrtException e){
            meldung.setText("Konto gesperrt!");
        }
    }
}
