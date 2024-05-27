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
            kontonummer++;
            giroKonten.put(kontonummer, new Girokonto(inhaber, kontonummer, 500));
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
            sparbuchKonten.put(kontonummer, new Sparbuch(inhaber, kontonummer));
            return kontonummer;
    }

    /**
     * gibt alle Konten der Bank in einer Bank als Text zurück mit Konto und Kontostand
     * @return zusammengebauten String aller Konten
     */
    public String getAlleKonten(){
        String kontonummern = "";
        long tempKontonummer;
        double tempKontostand;
        kontonummern = "Girokontos:" + System.lineSeparator();
        for(Map.Entry<Long, Konto> entry: giroKonten.entrySet()){
            tempKontonummer = entry.getKey();
            tempKontostand =  entry.getValue().getKontostand();
            kontonummern += "Konto: " + tempKontonummer + System.lineSeparator();
            kontonummern += "Kontostand: " + tempKontostand + System.lineSeparator();
        }
        kontonummern = "Sparebuecher:" + System.lineSeparator();
        for(Map.Entry<Long, Konto> entry: sparbuchKonten.entrySet()){
            tempKontonummer = entry.getKey();
            tempKontostand = entry.getValue().getKontostand();
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
     * loescht das mit der Nummer angegebene Konto die Kontonummer des Kontos wird nicht wieder freigegeben
     * gibt sicher das alle Konten eine eigene erstellte Kontonummer haben.
     * @param nummer Kontonummer des Kontos
     * @return gibt true bei erfolgreichem loeschen zurueck und false wenn das Konto nicht existiert
     */
    public boolean kontoLoeschen(long nummer){
        if(giroKonten.get(nummer) != null){
            giroKonten.remove(nummer);
            return true;
        }else if(sparbuchKonten.get(nummer) != null){
            sparbuchKonten.remove(nummer);
            return true;
        }else return false;
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
            UeberweisungsfaehigesKonto giroKontoVon = (UeberweisungsfaehigesKonto) giroKonten.get(vonKontonr);
            UeberweisungsfaehigesKonto giroKontoZu = (UeberweisungsfaehigesKonto) giroKonten.get(nachKontonr);
            if(!giroKontoVon.ueberweisungAbsenden(betrag, giroKontoZu.getInhaber().getNachname(),
                    giroKontoZu.getKontonummer(), bankleitzahl, verwendungszweck)) {
                    return false;
            }else{
                giroKontoZu.ueberweisungEmpfangen(betrag, giroKontoVon.getInhaber().getNachname(), giroKontoVon.getKontonummer(), bankleitzahl, verwendungszweck);
                return true;
            }
        }
        return false;
    }
}
