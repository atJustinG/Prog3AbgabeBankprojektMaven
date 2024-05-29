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

    private long kontonummer = 0;

    private HashMap<Long, Konto> konten = new HashMap<Long,Konto>();
   // private HashMap<Long, Konto> konten = new HashMap<Long, Konto>();

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
            kontonummer++;
            konten.put(kontonummer, new Girokonto(inhaber, kontonummer, 500));
            return kontonummer;
    }

    /**
     * genauso wie giroKonto erstellt diese methode ein Sparbuch
     * und vergibt ihm eine beliebige einzigartige Kontonummer.
     * @param inhaber
     * @return
     */
    public long sparbuchErstellen(Kunde inhaber){
            kontonummer++;
            konten.put(kontonummer, new Sparbuch(inhaber, kontonummer));
            return kontonummer;
    }

    /**
     * simple Methode um mock Konten in die Bank einzufuegen
     * @param k das mock Konto
     * @return die Kontonummer des mock Kontos
     */
    public long mockEinfuegen(Konto k){
        long tempKontonummer = k.getKontonummer();
        konten.put(tempKontonummer, k);
        return tempKontonummer;
    }

    /**
     * gibt alle Konten der Bank in einer Bank als Text zurück mit Konto und Kontostand
     * @return zusammengebauten String aller Konten
     */
    public String getAlleKonten(){
        String kontonummern = "";
        long tempKontonummer;
        double tempKontostand;
        kontonummern = "Konten:" + System.lineSeparator();
        for(Map.Entry<Long, Konto> entry: konten.entrySet()){
            tempKontonummer = entry.getKey();
            tempKontostand =  entry.getValue().getKontostand();
            kontonummern += "Konto: " + tempKontonummer + System.lineSeparator();
            kontonummern += "Kontostand: " + tempKontostand + System.lineSeparator();
        }
        return kontonummern;
    }

    /**
     * gibt alle Kontonummern zurück als eine Liste
     * @return Listenobjekt aller Kontonummern
     */
    public List<Long> getAlleKontonummern(){
        ArrayList<Long> tempKontoNummern = new ArrayList<Long>(konten.size());
        for (Map.Entry<Long, Konto> entry: konten.entrySet()){
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
        if(konten.get(von) != null){
            return konten.get(von).abheben(betrag);
        }else return false;
    }

    /**
     * zahlt Geld auf ein Konto ein
     * @param auf das Konto auf dem das Geld eingezahlt werden soll
     * @param betrag der Betrag der eingezahlt werden soll
     * @throws IllegalArgumentException wird bei einem falschen Betrag geworfen oder bei einer unbekannten Kontonummer
     */
    public void geldEinzahlen(long auf, double betrag) throws IllegalArgumentException{
        if (konten.get(auf) != null) {
            konten.get(auf).einzahlen(betrag);
        }else throw new IllegalArgumentException();
    }

    /**
     * loescht das mit der Nummer angegebene Konto die Kontonummer des Kontos wird nicht wieder freigegeben
     * gibt sicher das alle Konten eine eigene erstellte Kontonummer haben.
     * @param nummer Kontonummer des Kontos
     * @return gibt true bei erfolgreichem loeschen zurueck und false wenn das Konto nicht existiert
     */
    public boolean kontoLoeschen(long nummer){
        if(konten.get(nummer) != null){
            konten.remove(nummer);
            return true;
        }else return false;
    }

    /**
     * gibt den aktuellen Kontostand des spezifizierten Kontos zurück
     * @param nummer Kontonummer des Kontos
     * @return den aktuellen Kontostand oder NaN bei einer falschen oder unbekannten kontonummer
     */
    public double getKontostand(long nummer){
        if(konten.get(nummer) != null){
            return konten.get(nummer).getKontostand();
        }
        return Double.NaN;
    }

    public Konto getKonto(long kontonummer){
        if(konten.get(kontonummer) != null){
            return konten.get(kontonummer);
        }else return null;
    }
    /**
     * ueberweist einen Betrag von einem Konto auf ein anderes innerhalb der Bank
     * @param vonKontonr Kontonummer des sendeden Kontos
     * @param nachKontonr Kontonummer des empfangenden Kontos
     * @param betrag Betrag der ueberwiesen werden soll
     * @param verwendungszweck Verwendungszweck der Ueberweisung
     * @return true bei durchgefuehrter Ueberweisung false wenn die Konten nicht existieren oder das zweite Konto ein Sparbuch ist
     * @throws GesperrtException wird geworfen wenn ein Konto gesperrt ist
     */
    public boolean geldUeberweisen(long vonKontonr, long nachKontonr, double betrag, String verwendungszweck) throws GesperrtException {
        if(konten.get(vonKontonr) != null && konten.get(nachKontonr) != null){
            if(konten.get(vonKontonr) instanceof Girokonto && konten.get(nachKontonr) instanceof Girokonto){
                UeberweisungsfaehigesKonto giroKontoVon = (UeberweisungsfaehigesKonto) konten.get(vonKontonr);
                UeberweisungsfaehigesKonto giroKontoZu = (UeberweisungsfaehigesKonto) konten.get(nachKontonr);
                if(!giroKontoVon.ueberweisungAbsenden(betrag, giroKontoZu.getInhaber().getNachname(),
                        giroKontoZu.getKontonummer(), bankleitzahl, verwendungszweck)) {
                    return false;
                }else{
                    giroKontoZu.ueberweisungEmpfangen(betrag, giroKontoVon.getInhaber().getNachname(), giroKontoVon.getKontonummer(), bankleitzahl, verwendungszweck);
                    return true;
                }
            }
        }
        return false;
    }
}
