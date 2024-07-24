package bankprojekt;

import bankprojekt.verarbeitung.GesperrtException;
import bankprojekt.verarbeitung.Girokonto;
import bankprojekt.verarbeitung.Konto;
import bankprojekt.verarbeitung.Kunde;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Kontroller Klasse um mit der Konto Oberflaeche zu kommunizieren und zu steuern
 */
public class KontoController extends Application {
    Konto konto;
    //private KontoOberflaeche kOberflaeche;

    @FXML
    private Text nummer;
    @FXML
    private Text stand;
    @FXML
    private CheckBox gesperrt;
    @FXML
    private TextArea adresse;
    @FXML
    private Text meldung;
    @FXML
    private TextField betrag;

    /**
     * initialize Methote um den KontoController für die FXML zu initialisieren
     */
    @FXML public void initialize(){
        konto = new Girokonto(new Kunde(), 123456789, 500);


        nummer.setText(Long.toString(konto.getKontonummer()));
        stand.textProperty().bind(Bindings.format("%.2f", konto.kontostandProperty()));
        stand.fillProperty().bind(Bindings.when(konto.kontostandProperty().greaterThanOrEqualTo(0)).then(Color.BLACK).otherwise(Color.RED));
        gesperrt.selectedProperty().bindBidirectional(konto.gesperrtProperty());
        adresse.textProperty().bindBidirectional(konto.getInhaber().adresseProperty());
    }


    /**
     * start Methode startet die Anwendung
     * @param stage visualisiert das Fenster
     * @throws Exception bei Fehler wirft eine Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("KontoOberflaeche.fxml"));
        //loader.setController(this);
        BorderPane root = loader.load();

        //kOberflaeche = new KontoOberflaeche(konto, this);


        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Konto Verwaltung");
        stage.show();
    }

    /**
     * einzahlen Methode eventhandler per Knopfdruck der Oberflaeche ins Konto einzuzahlen
     */
    @FXML
    private void einzahlen() {
        try {
            double betragValue = Double.parseDouble(betrag.getText());
            konto.einzahlen(betragValue);
            meldung.setText("Einzahlung erfolgreich!");
        } catch (IllegalArgumentException e) {
            meldung.setText("Falsche Eingabe!");
        }
    }

    /**
     * FXML Methode zum abheben
     */
    @FXML
    private void abheben() {
        try {
            double betragValue = Double.parseDouble(betrag.getText());
            try {
                konto.abheben(betragValue);
                meldung.setText("Abhebung erfolgreich!");
            } catch (GesperrtException e) {
                meldung.setText("Konto gesperrt!");
            }
        } catch (NumberFormatException e) {
            meldung.setText("falsche Eingabe!");
        }
    }

    /**
     * FXML Methode zum Konto sperren
     */
    @FXML
    private void gesperrtAendern() {
        if (gesperrt.isSelected()) {
            konto.sperren();
        } else {
            konto.entsperren();
        }
        if (konto.isGesperrt()) {
            meldung.setText("Konto ist gesperrt");
        } else {
            meldung.setText("Konto ist entsperrt");
        }
    }

// alte Klassen für die alte Oberfläche

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
