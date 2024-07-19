package bankprojekt.oberflaeche;

import bankprojekt.KontoController;
import bankprojekt.verarbeitung.Konto;
import javafx.beans.binding.Bindings;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;

/**
 * Eine Oberfläche für ein einzelnes Konto. Man kann einzahlen
 * und abheben und sperren und die Adresse des Kontoinhabers 
 * ändern
 * @author Doro
 *
 */
public class KontoOberflaeche extends BorderPane {
	private Text ueberschrift;
	private GridPane anzeige;
	private Text txtNummer;
	/**
	 * Anzeige der Kontonummer
	 */
	private Text nummer;
	private Text txtStand;
	/**
	 * Anzeige des Kontostandes
	 */
	private Text stand;
	private Text txtGesperrt;
	/**
	 * Anzeige und Änderung des Gesperrt-Zustandes
	 */
	private CheckBox gesperrt;
	private Text txtAdresse;
	/**
	 * Anzeige und Änderung der Adresse des Kontoinhabers
	 */
	private TextArea adresse;
	/**
	 * Anzeige von Meldungen über Kontoaktionen
	 */
	private Text meldung;
	private HBox aktionen;
	/**
	 * Auswahl des Betrags für eine Kontoaktion
	 */
	private TextField betrag;
	/**
	 * löst eine Einzahlung aus
	 */
	private Button einzahlen;
	/**
	 * löst eine Abhebung aus
	 */
	private Button abheben;

	/**
	 * erstellt die Oberfläche
	 */
	public KontoOberflaeche(Konto konto, KontoController kController)
	{
		ueberschrift = new Text("Ein Konto verändern");
		ueberschrift.setFont(new Font("Sans Serif", 25));
		BorderPane.setAlignment(ueberschrift, Pos.CENTER);
		this.setTop(ueberschrift);
		
		anzeige = new GridPane();
		anzeige.setPadding(new Insets(20));
		anzeige.setVgap(10);
		anzeige.setAlignment(Pos.CENTER);
		
		txtNummer = new Text("Kontonummer:");
		txtNummer.setFont(new Font("Sans Serif", 15));
		anzeige.add(txtNummer, 0, 0);
		nummer = new Text(Long.toString(konto.getKontonummer()));
		nummer.setFont(new Font("Sans Serif", 15));
		GridPane.setHalignment(nummer, HPos.RIGHT);
		anzeige.add(nummer, 1, 0);
		
		txtStand = new Text("Kontostand:");
		txtStand.setFont(new Font("Sans Serif", 15));
		anzeige.add(txtStand, 0, 1);
		stand = new Text();
		stand.setFont(new Font("Sans Serif", 15));
		stand.textProperty().bind(Bindings.format("%.2f", konto.getKontostandProperty()));
		stand.fillProperty().bind(Bindings.when(konto.getKontostandProperty().greaterThanOrEqualTo(0)).then(Color.GREEN).otherwise(Color.RED));
		GridPane.setHalignment(stand, HPos.RIGHT);
		anzeige.add(stand, 1, 1);
		
		txtGesperrt = new Text("Gesperrt: ");
		txtGesperrt.setFont(new Font("Sans Serif", 15));
		anzeige.add(txtGesperrt, 0, 2);
		gesperrt = new CheckBox();
		gesperrt.selectedProperty().bindBidirectional(konto.getGesperrtProperty());
		gesperrt.setOnAction(event -> gesperrtAendern(konto));
		GridPane.setHalignment(gesperrt, HPos.RIGHT);
		anzeige.add(gesperrt, 1, 2);
		
		txtAdresse = new Text("Adresse: ");
		txtAdresse.setFont(new Font("Sans Serif", 15));
		anzeige.add(txtAdresse, 0, 3);
		adresse = new TextArea();
		adresse.setPrefColumnCount(25);
		adresse.setPrefRowCount(2);
		adresse.textProperty().bindBidirectional(konto.getInhaber().adresseProperty());
		GridPane.setHalignment(adresse, HPos.RIGHT);
		anzeige.add(adresse, 1, 3);
		
		meldung = new Text("Willkommen lieber Benutzer");
		meldung.setFont(new Font("Sans Serif", 15));
		meldung.setFill(Color.RED);
		anzeige.add(meldung,  0, 4, 2, 1);
		
		this.setCenter(anzeige);
		
		aktionen = new HBox();
		aktionen.setSpacing(10);
		aktionen.setAlignment(Pos.CENTER);
		
		betrag = new TextField("100.00");
		aktionen.getChildren().add(betrag);
		einzahlen = new Button("Einzahlen");
		einzahlen.setOnAction(event -> einzahlen(kController, betrag.getText(), meldung));
		aktionen.getChildren().add(einzahlen);
		abheben = new Button("Abheben");
		abheben.setOnAction(event -> abheben(kController, betrag.getText(), meldung));
		aktionen.getChildren().add(abheben);
		
		this.setBottom(aktionen);
	}

	/**
	 * einzahlen Methode
	 * @param kController Controller
	 * @param betrag betrag der eingezahlt wird
	 * @param meldung Meldung bei Fehler
	 */
	private void einzahlen(KontoController kController, String betrag, Text meldung) {
		try{
			kController.einzahlenEvent(Double.parseDouble(betrag), meldung);
		}catch(NumberFormatException e){
			meldung.setText("falsche Eingabe!");
		}

	}

	/**
	 * abheben Methode gibt dem Controller alle Daten die er benötigt
	 * @param kController Controller
	 * @param betrag betrag zum abheben
	 * @param meldung Meldung bei Fehlverhalten
	 */
	private void abheben(KontoController kController, String betrag, Text meldung){
		try{
			kController.abhebenEvent(Double.parseDouble(betrag), meldung);
		}catch(NumberFormatException e){
			meldung.setText("falsche Eingabe!");
		}
	}

	/**
	 * sperrt das konto wenn die checkbox aktiviert wurde
	 * @param konto das Konto was gesperrt werden soll
	 */
	private void gesperrtAendern(Konto konto)
	{
		if(gesperrt.isSelected()){
			konto.sperren();
		}else{
			konto.entsperren();
		}
		if (konto.isGesperrt()) {
			meldung.setText("Konto ist gesperrt");
		} else {
			meldung.setText("Konto ist entsperrt");
		}
	}
}
