package bankprojekt.verarbeitung;

import org.decimal4j.util.DoubleRounder;

/**
 * Enum um Euro spezifische Waehrungen darstelen und umrechnen zu koennen
 */
public enum Waehrung {

    EUR(1), Escudo(109.8269), DOBRA(24304.7429), FRANC(490.92);

    /**
     * einfacher Konstruktor
     * @param zahl
     */
    private Waehrung(double zahl){
        this.umrechnungsKurs = zahl;
    }

    /**
     *
     */
    private double umrechnungsKurs;

    /**
     * rechnet Euro in die angegebene Waehrung um
     * @param betrag der umzurechnende Betrag
     * @return den neuen Betrag
     */
    public double euroInWaehrungUmrechnen(double betrag){
        return DoubleRounder.round(betrag * umrechnungsKurs, 2);
    }

    /**
     * rechnet Waehrung in Euro um
     * @param betrag Betrag der zu Euro umgewandelt wird
     * @return der Betrag in Euro
     */
    public double waehrungInEuroUmrechnen(double betrag){
        return DoubleRounder.round(betrag/umrechnungsKurs, 2);
    }
}
