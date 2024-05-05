package bankprojekt.verarbeitung;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {

    private long bankleitzahl;

    long Kontonummer = 0;

    HashMap<Long, Konto> giroKonten = new HashMap<Long,Konto>();
    HashMap<Long, Konto> sparbuchKonten = new HashMap<Long, Konto>();

    public Bank(long bankleitzahl){
        this.bankleitzahl = bankleitzahl;
    }

    public long getBankleitzahl(){
        return bankleitzahl;
    }

    public long girokontoErstellen(Kunde inhaber){
            giroKonten.put(Kontonummer, new Girokonto(inhaber, Kontonummer, 500));
            Kontonummer++;
            return Kontonummer;
    }

    public long sparbucherstellen(Kunde inhaber){
        sparbuchKonten.put(Kontonummer, new Sparbuch(inhaber, Kontonummer));
        Kontonummer++;
        return Kontonummer;
    }

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

    public boolean geldAbheben(long von, double betrag) throws GesperrtException {
        if(giroKonten.get(von) != null){
            return giroKonten.get(von).abheben(betrag);
        }else if(sparbuchKonten.get(von) != null){
            return sparbuchKonten.get(von).abheben(betrag);
        }else return false;
    }

    public void geldEinzahlen(long auf, double betrag) throws IllegalArgumentException{
        if (giroKonten.get(auf) != null) {
            giroKonten.get(auf).einzahlen(betrag);
        } else if(sparbuchKonten.get(auf) != null){
            sparbuchKonten.get(auf).einzahlen(betrag);
        }else throw new IllegalArgumentException();
    }

    public double getKontostand(long nummer){
        if(giroKonten.get(nummer) != null){
            return giroKonten.get(nummer).getKontostand();
        }else if(sparbuchKonten.get(nummer) != null){
            return sparbuchKonten.get(nummer).getKontostand();
        }
        return Double.NaN;
    }

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
