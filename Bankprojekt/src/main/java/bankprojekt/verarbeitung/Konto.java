package bankprojekt.verarbeitung;

import com.google.common.primitives.Doubles;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.Serializable;
import java.util.HashSet;
//Abkürzung des Klassennamens ist jetzt erlaubt

/**
 * stellt ein allgemeines Bank-Konto dar
 */
public abstract class Konto implements Serializable, Comparable<Konto>
{
	private static final long serialVersionUID = 1L;
	private final HashSet<KontoObserver> observers = new HashSet<>();
	//Methode ist IGITTIGITT!
	//Trenne Verarbeitung von Ein- und Ausgabe!
	public void aufDerKonsoleAusgeben()
	{
		System.out.println(this.toString());
	}
	/** 
	 * der Kontoinhaber
	 */
	private Kunde inhaber;
	/**
	 * Währung des Kontos
	 */
	private Waehrung waehrung;

	/**
	 * die Kontonummer
	 */
	private final long nummer;

	/**
	 * der aktuelle Kontostand
	 */
	private final ReadOnlyDoubleWrapper kontostand = new ReadOnlyDoubleWrapper(0);
	/**
	 * Wenn das Konto gesperrt ist (gesperrt = true), können keine Aktionen daran mehr vorgenommen werden,
	 * die zum Schaden des Kontoinhabers wären (abheben, Inhaberwechsel)
	 */
	private final BooleanProperty gesperrt = new SimpleBooleanProperty(false);
	/**
	 * Property um zu erkennen wenn das Konto im plus ist
	 */
	private final BooleanProperty kontostandImPlus = new SimpleBooleanProperty(true);

	/**
	 * Property um den Kontostand weitergeben zu können
	 * @return den aktuellen Kontostand
	 */
	public ReadOnlyDoubleProperty getKontostandProperty(){
		return kontostand.getReadOnlyProperty();
	}

	/**
	 * gibt das gesperrt Property weiter
	 * @return gersperrt
	 */
	public BooleanProperty getGesperrtProperty(){
		return gesperrt;
	}

	/**
	 * gibt den KontoStandImplus wieder
	 * @return kontostandimPluis
	 */
	public BooleanProperty getKontostandImPlus(){
		return kontostandImPlus;
	}


	/**
	 * setzt den aktuellen Kontostand
	 * @param kontostand neuer Kontostand
	 */
	protected void setKontostand(double kontostand) {
		this.kontostand.set(kontostand);
	}

	/**
	 * Setzt die beiden Eigenschaften kontoinhaber und kontonummer auf die angegebenen Werte,
	 * der anfängliche Kontostand wird auf 0 gesetzt.
	 *
	 * @param inhaber der Inhaber
	 * @param kontonummer die gewünschte Kontonummer
	 * @throws IllegalArgumentException wenn der inhaber null ist
	 */
	public Konto(Kunde inhaber, long kontonummer) {
		if(inhaber == null)
			throw new IllegalArgumentException("Inhaber darf nicht null sein!");
		this.inhaber = inhaber;
		this.nummer = kontonummer;
		waehrung = Waehrung.EUR;
		this.kontostand.set(0);
		this.gesperrt.set(false);
	}
	
	/**
	 * setzt alle Eigenschaften des Kontos auf Standardwerte
	 */
	public Konto() {
		this(Kunde.MUSTERMANN, 1234567);
	}

	/**
	 * liefert den Kontoinhaber zurück
	 * @return   der Inhaber
	 */
	public final Kunde getInhaber() {
		return this.inhaber;
	}
	
	/**
	 * setzt den Kontoinhaber
	 * @param kinh   neuer Kontoinhaber
	 * @throws GesperrtException wenn das Konto gesperrt ist
	 * @throws IllegalArgumentException wenn kinh null ist
	 */
	public final void setInhaber(Kunde kinh) throws GesperrtException{
		if (kinh == null)
			throw new IllegalArgumentException("Der Inhaber darf nicht null sein!");
		if(this.gesperrt.get())
			throw new GesperrtException(this.nummer);        
		this.inhaber = kinh;
	}
	
	/**
	 * liefert den aktuellen Kontostand
	 * @return   Kontostand
	 */
	public double getKontostand() {
		return kontostand.get();
	}

	/**
	 * liefert die Kontonummer zurück
	 * @return   Kontonummer
	 */
	public final long getKontonummer() {
		return nummer;
	}

	/**
	 * liefert zurück, ob das Konto gesperrt ist oder nicht
	 * @return true, wenn das Konto gesperrt ist
	 */
	public boolean isGesperrt() { //Achtung: nicht get... bei booleschen Werten
		return gesperrt.get();
	}
	
	/**
	 * Erhöht den Kontostand um den eingezahlten Betrag.
	 *
	 * @param betrag double
	 * @throws IllegalArgumentException wenn der betrag negativ ist 
	 */
	public void einzahlen(double betrag) {
		if (betrag < 0 ||!Doubles.isFinite(betrag)) {
			throw new IllegalArgumentException("Falscher Betrag");
		}
		double letzterKontostand = getKontostand();
		setKontostand(getKontostand() + betrag);
		if(getKontostand() >= 0 && !kontostandImPlus.getValue()){
			kontostandImPlus.set(true);
		}
		notifyObserver(letzterKontostand, getKontostand());
	}
	
	@Override
	public String toString() {
		String ausgabe;
		ausgabe = "Kontonummer: " + this.getKontonummerFormatiert()
				+ System.getProperty("line.separator");
		ausgabe += "Inhaber: " + this.inhaber;
		ausgabe += "Aktueller Kontostand: " + getKontostandFormatiert() + " ";
		ausgabe += this.getGesperrtText() + System.getProperty("line.separator");
		return ausgabe;
	}

	/**
	 * Mit dieser Methode wird der geforderte Betrag vom Konto abgehoben, wenn es nicht gesperrt ist
	 * und die speziellen Abheberegeln des jeweiligen Kontotyps die Abhebung erlauben
	 *
	 * @param betrag double
	 * @throws GesperrtException wenn das Konto gesperrt ist
	 * @throws IllegalArgumentException wenn der betrag negativ oder unendlich oder NaN ist 
	 * @return true, wenn die Abhebung geklappt hat, 
	 * 		   false, wenn sie abgelehnt wurde
	 */
	public boolean abheben(double betrag) throws GesperrtException{
		if (betrag < 0 || Double.isNaN(betrag) || Double.isInfinite(betrag)) {
			throw new IllegalArgumentException("Betrag ungültig");
		}
		if (this.isGesperrt()) {
			throw new GesperrtException(this.getKontonummer());
		}
		if (pruefeBedingungenFuerAbhebung(betrag)) {
			double letzterKontostand = getKontostand();
			setKontostand(getKontostand() - betrag);
			if(kontostandImPlus.get() && getKontostand() < 0){
				kontostandImPlus.set(false);
			}
			notifyObserver(letzterKontostand, getKontostand());
			return true;
		}
		return false;
	}

	/**
	 * Hook Methode für abheben
	 * @param betrag der abzuhebende Betrag
	 * @return true bei erfolgreicher Abhebung false bei nicht erfolgreicher Abhebung
	 */
	protected abstract boolean pruefeBedingungenFuerAbhebung(double betrag);

	/**
	 * sperrt das Konto, Aktionen zum Schaden des Benutzers sind nicht mehr möglich.
	 */
	public void sperren() {
		this.gesperrt.set(true);
	}

	/**
	 * entsperrt das Konto, alle Kontoaktionen sind wieder möglich.
	 */
	public void entsperren() {
		this.gesperrt.set(false);
	}
	
	
	/**
	 * liefert eine String-Ausgabe, wenn das Konto gesperrt ist
	 * @return "GESPERRT", wenn das Konto gesperrt ist, ansonsten ""
	 */
	public String getGesperrtText()
	{
		if (this.gesperrt.get())
		{
			return "GESPERRT";
		}
		else
		{
			return "";
		}
	}

	
	/**
	 * liefert die ordentlich formatierte Kontonummer
	 * @return auf 10 Stellen formatierte Kontonummer
	 */
	public String getKontonummerFormatiert()
	{
		return String.format("%10d", this.nummer);
	}
	
	/**
	 * liefert den ordentlich formatierten Kontostand
	 * @return formatierter Kontostand mit 2 Nachkommastellen und Währungssymbol
	 */
	public String getKontostandFormatiert()
	{
		return String.format("%10.2f €" , this.getKontostand(), waehrung.name());
	}

	/**
	 * gibt die aktuelle im Konto geführte Waehrung wieder
	 * @return waerung aktuelle Waehrung
	 */
	public Waehrung getAktuelleWaehrung() {
		return waehrung;
	}

	/**
	 * Vergleich von this mit other; Zwei Konten gelten als gleich,
	 * wen sie die gleiche Kontonummer haben
	 * @param other das Vergleichskonto
	 * @return true, wenn beide Konten die gleiche Nummer haben
	 */
	@Override
	public boolean equals(Object other)
	{
		if(this == other)
			return true;
		if(other == null)
			return false;
		if(this.getClass() != other.getClass())
			return false;
		if(this.nummer == ((Konto)other).nummer)
			return true;
		else
			return false;
	}
	
	@Override
	public int hashCode()
	{
		return 31 + (int) (this.nummer ^ (this.nummer >>> 32));
	}

	@Override
	public int compareTo(Konto other)
	{
		if(other.getKontonummer() > this.getKontonummer())
			return -1;
		if(other.getKontonummer() < this.getKontonummer())
			return 1;
		return 0;
	}

	/**
	 * hebt den gewuenschten betrag in der angegebenen Wahrung ab
	 * @param betrag zu bahebender Betrag
	 * @param w angegebene Waehrung
	 * @return true beim erfolgreichen abheben false wenn das Konto gesperrt ist
	 * @throws GesperrtException wirft die gesperrte exception
	 */
	public boolean abheben(double betrag, Waehrung w) throws GesperrtException{
			return abheben(umrechnen(betrag, w));
	}

	/**
	 * zahlt den gewuenschten Betrag in der angegeben Waehrung ein
	 * @param betrag der einzuzahlende Betrag
	 * @param w die angegebene Waehrung die eingezahlt werden soll
	 */
	public void einzahlen(double betrag, Waehrung w) {
		einzahlen(umrechnen(betrag, w));
	}

	/**
	 * wechselt die aktuelle Wahrung zu der angegeben gewuenschten neuen Waehrung um und rechnet den aktuellen
	 * Kontostand in der neuen Waehrung aus
	 * @param neu die gewuenschte neue Waehrung
	 */
	public void waehrungswechsel(Waehrung neu){
		double tempKontostand = getKontostand();
		if(waehrung == Waehrung.EUR){
			tempKontostand = neu.euroInWaehrungUmrechnen(tempKontostand);
		}else{
			tempKontostand = waehrung.waehrungInEuroUmrechnen(tempKontostand);
			tempKontostand = neu.euroInWaehrungUmrechnen(tempKontostand);
		}
		waehrung = neu;
		setKontostand(tempKontostand);
	}

	/**
	 * rechnet den angegebenen betrag zu einem Betrag der angegebenen Waehrung um
	 * @param betrag umrechnender Betrag
	 * @param w Waehrung die den umrechnungskurs angibtg
	 * @return den neu gerechneten Betrag
	 */
	public double umrechnen(double betrag, Waehrung w) {
			betrag = w.waehrungInEuroUmrechnen(betrag);
			betrag = waehrung.euroInWaehrungUmrechnen(betrag);
			return betrag;
	}

	/**
	 * fügt einen Observer zum Objekt hinzu
	 * @param observer observer der hinzugefügt werden soll
	 */
	public void registerObserver(KontoObserver observer) {
		observers.add(observer);
	}

	/**
	 * entfernt den observer vom Objekt
	 * @param observer observer der entfernt werden soll
	 */
	public void removeObserver(KontoObserver observer) {
		observers.remove(observer);
	}

	/**
	 * signalisiert allen listener bei Äanderungen
	 * @param letzterKontostand der letzte Kontostand
	 * @param neuerKontostand der aktuelle Kontostand
	 */
	protected void notifyObserver(double letzterKontostand, double neuerKontostand) {
		observers.forEach(observer ->
				observer.update(this, letzterKontostand ,neuerKontostand));
	}
}
