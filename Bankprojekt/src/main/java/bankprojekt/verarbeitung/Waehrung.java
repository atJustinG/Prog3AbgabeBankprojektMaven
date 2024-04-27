package bankprojekt.verarbeitung;

import org.decimal4j.util.DoubleRounder;

public enum Waehrung {
    EUR(1), Escudo(109.8269), DOBRA(24304.7429), FRANC(490.92);

    private Waehrung(double zahl){
        this.umrechnungsKurs = zahl;
    }

    private double umrechnungsKurs;

    public double euroInWaehrungUmrechnen(double betrag){
        return DoubleRounder.round(betrag * umrechnungsKurs, 2);
    }

    public double waehrungInEuroUmrechnen(double betrag){
        return DoubleRounder.round(betrag/umrechnungsKurs, 2);
    }
}
