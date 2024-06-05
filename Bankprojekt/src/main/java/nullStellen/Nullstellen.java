package nullStellen;

import java.util.Iterator;

public class Nullstellen {

    /**
     * Methode um Nullstellen mit dem Intervallhalbierungsverfahren zu ermitteln
     * @param f die uebergebene Funktion f(x)
     * @param min die untere Intervallgrenze
     * @param max die obere Intervallgrenze
     * @return die errechnete 0 Stelle mit einer Genauigkeit von 0,01
     */
    public static double nullStelleBerechnen(Ifunktion f, double min, double max){
        double mittelwert = 0;
        if((f.funktion(min) > 0 && f.funktion(max) > 0) ^ (f.funktion(min) < 0 && f.funktion(max) < 0)) {
            System.out.println("min: " + f.funktion(min) + "max: " + f.funktion(max));
            return Double.NaN;
        }
        while ((max - min) / 2 > 0.01) {
            mittelwert = (min + max) / 2;
            if (f.funktion(mittelwert) == 0) {
                return mittelwert;
            } else if (f.funktion(min) * f.funktion(mittelwert) < 0) {
                max = mittelwert;
            } else {
                min = mittelwert;
            }
        }
        return mittelwert;
    }

    /**
     * Methode um Rekursiv die Nullstellen zu ermitteln
     * leider Fehlerhaft
     * @param f die uebergebene Funktion f(x)
     * @param min die untere Intervallgrenze
     * @param max die obere Intervallgrenze
     * @return die errechnete Nullstelle mit einer Genauigkeit von 0,01
     */
    public static double nullStellenRechnerRekursiv(Ifunktion f, double min, double max){
        double mittelwert = Double.NaN;

        if((f.funktion(min) > 0 && f.funktion(max) > 0) ^ (f.funktion(min) < 0 && f.funktion(max) < 0)) {
            System.out.println("min: " + f.funktion(min) + "max: " + f.funktion(max));
            return Double.NaN;
        }
            mittelwert = (min + max) / 2;
            if(Math.abs(f.funktion(mittelwert)) <= 0.01){
                return mittelwert;
            }else
            if(f.funktion(min) == 0){
                return min;
            }else
                if(f.funktion(max) == 0){
                return max;
            }else
                if(f.funktion(mittelwert) == 0){
                return mittelwert;
            }else
                if(f.funktion(min) < 0 && f.funktion(mittelwert) > 0){
                nullStellenRechnerRekursiv(f, min, mittelwert);
            }else{
                nullStellenRechnerRekursiv(f, mittelwert, max);
            }
        return mittelwert;
    }

}