package bankprojekt;

import bankprojekt.oberflaeche.KontoOberflaeche;
import bankprojekt.verarbeitung.GesperrtException;
import bankprojekt.verarbeitung.Girokonto;
import bankprojekt.verarbeitung.Kunde;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class KontoController extends Application {
    Girokonto konto;
    private KontoOberflaeche kOberflaeche;

    @Override
    public void start(Stage stage) throws Exception {
        konto = new Girokonto(new Kunde(), 123456789, 500);
        kOberflaeche = new KontoOberflaeche(konto);

        Scene scene = new Scene(kOberflaeche, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Konto Verwaltung");
        stage.show();

        setEventHandlers();
    }

    private void einzahlenEvent(double betrag){
        konto.einzahlen(betrag);
    }

    private void abhebenEvent(double betrag){
        konto.abheben(betrag);
    }

    private void setEventHandlers(){
        kOberflaeche.getEinzahlenButton().setOnAction(event -> konto.einzahlen(Double.parseDouble(kOberflaeche.getBetragTextField().getText())));
        kOberflaeche.getAbhebenButton().setOnAction(event -> {
            try{
                konto.abheben(Double.parseDouble(kOberflaeche.getBetragTextField().getText()));
            }catch(GesperrtException e){
                e.printStackTrace();
            }
        });
        kOberflaeche.getGesperrtCheckBox().selectedProperty().addListener((obs, wasSelected, isNowSelected) -> konto.sperren());
        kOberflaeche.getAdresseTextArea().textProperty().addListener((obs, oldText, newText) -> konto.getInhaber().setAdresse(newText));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
