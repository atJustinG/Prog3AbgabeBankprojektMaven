package bankprojekt.verarbeitung;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Klasse Bank beschreibt das verhalten einer Bank die Konten verwaltet
 * @author Justin Glowa
 */
public class Bank {

    private long bankleitzahl;

    private long Kontonummer = 0;

    private HashMap<Long, Konto> giroKonten = new HashMap<Long,Konto>();
    private HashMap<Long, Konto> sparbuchKonten = new HashMap<Long, Konto>();

    /**
     * Konstruktor zur erstellung eines Bankobjekts
     * @param bankleitzahl Bankleitzahl der Bank
     */
    public Bank(long bankleitzahl){
        this.bankleitzahl = bankleitzahl;
    }

    /**
     * getter für Bankleitzahl
     * @return die aktuelle Bankleitzahl des Objekts
     */
    public long getBankleitzahl(){
        return bankleitzahl;
    }

    /**
     * erstellen und hinzufügen eines Girokontos
     * generiert eine neue Kontonummer bei jedem neu erstelltem Konto und speichert es in einer HashMap
     * @param inhaber Kunde dem das Konto gegeben ist
     * @return die Kontonummer des erstellten Kontos
     */
    public long girokontoErstellen(Kunde inhaber){
            giroKonten.put(Kontonummer, new Girokonto(inhaber, Kontonummer, 500));
            Kontonummer++;
            return Kontonummer;
    }

    /**
     * genauso wie giroKonto erstellt diese methode ein Sparbuch
     * und vergibt ihm eine beliebige einzigartige Kontonummer.
     * @param inhaber
     * @return
     */
    public long sparbuchErstellen(Kunde inhaber){
        sparbuchKonten.put(Kontonummer, new Sparbuch(inhaber, Kontonummer));
        Kontonummer++;
        return Kontonummer;
    }

    /**
     * gibt alle Konten der Bank in einer Bank als Text zurück mit Konto und Kontostand
     * @return zusammengebauten String aller Konten
     */
    public String getAlleKonten(){
        String kontonummern = "";
        long tempKontonummer;
        double tempKontostand;
        kontonummern = "Girokontos:\n";
        for(Map.Entry<Long, Konto> entry: giroKonten.entrySet()){
            tempKontonummer = entry.getKey();
            tempKontostand =  entry.getValue().getKontostand();
            kontonummern += "Konto: " + tempKontonummer + "\n";
            kontonummern += "Kontostand: " + tempKontostand + "\n";
        }
        kontonummern = "Sparebuecher:\n";
        for(Map.Entry<Long, Konto> entry: sparbuchKonten.entrySet()){
            tempKontonummer = entry.getKey();
            tempKontostand = entry.getValue().getKontostand();
            kontonummern += "Konto: " + tempKontonummer + "\n";
            kontonummern += "Kontostand: " + tempKontostand + "\n";

        }
        return kontonummern;
    }

    /**
     * gibt alle Kontonummern zurück als eine Liste
     * @return Listenobjekt aller Kontonummern
     */
    public List<Long> getAlleKontonummern(){
        ArrayList<Long> tempKontoNummern = new ArrayList<Long>(giroKonten.size());
        for (Map.Entry<Long, Konto> entry: giroKonten.entrySet()){
           tempKontoNummern.add(entry.getKey());
        }
        for (Map.Entry<Long, Konto> entry: sparbuchKonten.entrySet()){
            tempKontoNummern.add(entry.getKey());
        }
        return tempKontoNummern;
    }

    /**
     * hebt Geld vom angegeben Konto ab unterscheidet anhand der Kontonummer die Kontoart
     * @param von Kontonummer vom Konto von dem abgehoben werden soll
     * @param betrag der Betrag der abgeheben werden soll
     * @return boolean true wenn abheben erfolgreich ist false wenn das Konto nicht existiert
     * @throws GesperrtException wird geworfen wenn das Konto gesperrt ist
     */
    public boolean geldAbheben(long von, double betrag) throws GesperrtException {
        if(giroKonten.get(von) != null){
            return giroKonten.get(von).abheben(betrag);
        }else if(sparbuchKonten.get(von) != null){
            return sparbuchKonten.get(von).abheben(betrag);
        }else return false;
    }

    /**
     * zahlt Geld auf ein Konto ein
     * @param auf das Konto auf dem das Geld eingezahlt werden soll
     * @param betrag der Betrag der eingezahlt werden soll
     * @throws IllegalArgumentException wird bei einem falschen Betrag geworfen oder bei einer unbekannten Kontonummer
     */
    public void geldEinzahlen(long auf, double betrag) throws IllegalArgumentException{
        if (giroKonten.get(auf) != null) {
            giroKonten.get(auf).einzahlen(betrag);
        } else if(sparbuchKonten.get(auf) != null){
            sparbuchKonten.get(auf).einzahlen(betrag);
        }else throw new IllegalArgumentException();
    }

    /**
     * gibt den aktuellen Kontostand des spezifizierten Kontos zurück
     * @param nummer Kontonummer des Kontos
     * @return den aktuellen Kontostand oder NaN bei einer falschen oder unbekannten kontonummer
     */
    public double getKontostand(long nummer){
        if(giroKonten.get(nummer) != null){
            return giroKonten.get(nummer).getKontostand();
        }else if(sparbuchKonten.get(nummer) != null){
            return sparbuchKonten.get(nummer).getKontostand();
        }
        return Double.NaN;
    }

    /**
     * ueberweist einen Betrag von einem Konto auf ein anderes innerhalb der Bank
     * @param vonKontonr Kontonummer des sendeden Kontos
     * @param nachKontonr Kontonummer des empfangenden Kontos
     * @param betrag Betrag der ueberwiesen werden soll
     * @param verwendungszweck Verwendungszweck der Ueberweisung
     * @return true bei durchgefuehrter Ueberweisung false wenn die Konten nicht existieren
     * @throws GesperrtException wird geworfen wenn ein Konto gesperrt ist
     */
    public boolean geldUeberweisen(long vonKontonr, long nachKontonr, double betrag, String verwendungszweck) throws GesperrtException {
        if(giroKonten.get(vonKontonr) != null && giroKonten.get(nachKontonr) != null){
            Girokonto giroKontoVon = (Girokonto) giroKonten.get(vonKontonr);
            Girokonto giroKontoZu = (Girokonto) giroKonten.get(nachKontonr);
            giroKontoVon.ueberweisungAbsenden(betrag, giroKontoZu.getInhaber().toString(), giroKontoZu.getKontonummer(), bankleitzahl, verwendungszweck);
            giroKontoZu.ueberweisungEmpfangen(betrag, giroKontoVon.getInhaber().getNachname(), giroKontoVon.getKontonummer(), bankleitzahl, verwendungszweck);
            return true;
        }
            return false;
    }
}
