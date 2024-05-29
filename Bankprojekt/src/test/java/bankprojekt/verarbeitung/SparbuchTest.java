package bankprojekt.verarbeitung;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;
/**
 * testet die Methoden waehrungswechsel und abheben für Sparbuch
 * LocalDate wird nicth getestet da es von außen schwer manipulierbar ist
 */
class SparbuchTest {
    Konto konto1;

    /**
     * Konto Objekt mit einem Kontostand von 100 Euro als basis für alle weiteren tests
     */
    @BeforeEach
    void setUp(){
        konto1 = new Sparbuch();
        konto1.setKontostand(100);
    }

    /**
     * testet dass Waehrung und kontostand jeweils Euro und Waehrung
     */
    @Test
    void kontoWaehrungistEuro(){
        assertEquals(Waehrung.EUR, konto1.getAktuelleWaehrung());
        assertEquals(100, konto1.getKontostand());
    }

    /**
     * Testet die Waehrung von Euro auf Euro zu wechseln
     */
    @Test
    void waehrungswechsel_vonEuroAufEuro_dannEuro() {
        Waehrung w = Waehrung.EUR;
        konto1.waehrungswechsel(w);
        assertEquals(w, konto1.getAktuelleWaehrung());
        assertEquals(100, konto1.getKontostand());
    }

    /**
     * testet den waehrungswechsel von Euro auf eine andere Waehrung in diesem Fall Escudo
     */
    @Test
    void waehrungswechsel_vonEuroAufAndereWaehrung_dannAndereWaehrung(){
        konto1.waehrungswechsel(Waehrung.ESCUDO);
        assertEquals(Waehrung.ESCUDO, konto1.getAktuelleWaehrung());
        assertEquals(10982.69, konto1.getKontostand());
    }

    /**
     *testet Waehrungswechsel von Escudo auf Euro
     */
    @Test
    void waehrungswechsel_vonEscudoAufEuro_dannEuro(){
        konto1.setKontostand(0);
        konto1.waehrungswechsel(Waehrung.ESCUDO);
        konto1.setKontostand(10982.69);
        konto1.waehrungswechsel(Waehrung.EUR);
        assertEquals(Waehrung.EUR, konto1.getAktuelleWaehrung());
        assertEquals(100, konto1.getKontostand());
    }

    /**
     * testet Waehrungswechsel von Escudo auf Escudo
     */
    @Test
    void waehrungswechsel_vonEscudoaufEscudo(){
        konto1.setKontostand(0);
        konto1.waehrungswechsel(Waehrung.ESCUDO);
        konto1.setKontostand(10982.69);
        konto1.waehrungswechsel(Waehrung.ESCUDO);
        assertEquals(Waehrung.ESCUDO, konto1.getAktuelleWaehrung());
        assertEquals(10982.69, konto1.getKontostand());
    }

    /**
     * testet abheben Methode wenn Betrag kleiner 0 soll die korrekte IllegalArtgument Exception geworfen werden
     */
    @Test
    void abheben_betragKleinerNull(){
        assertThrowsExactly(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                konto1.abheben(-25.0, Waehrung.EUR);
            }
        });
    }

    /**
     * testet abheben Methode wenn betrag NaN ist das die korrekte Exception geworfen wird
     */
    @Test
    void abheben_betragIstNaN(){
        assertThrowsExactly(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                konto1.abheben(Double.NaN, Waehrung.EUR);
            }
        });
    }

    /**
     * testget abehen wenn Betrag Infinite ist dass die richtige die korrekte Exception geworfen wird
     */
    @Test
    void abheben_betragIstInfinite(){
        assertThrowsExactly(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                konto1.abheben(Double.POSITIVE_INFINITY, Waehrung.EUR);
            }
        });
    }

    /**
     * testet abheben das die korrekte Gesperrt Exception geworfen wird bei einem gesperrtem Konto
     */
    @Test
    void abheben_kontoIstGesperrt(){
        konto1.sperren();
        assertThrowsExactly(GesperrtException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                konto1.abheben(90, Waehrung.EUR);
            }
        });
    }

    /**
     * testet abheben bei dem der finale Betrag die 0,50 Euro Grenze nicht unterschreitet
     * @throws GesperrtException wenn Konto gesperrt ist
     */
    @Test
    void abheben_KontostandIstUeber50Cent() throws GesperrtException{
        assertTrue(konto1.abheben(99.5, Waehrung.EUR));
        assertEquals(0.5, konto1.getKontostand());
    }

    /**
     * testet abheben bei dem der finale Kontostand unter der 0,5 Euro grenze fällt
     * @throws GesperrtException geworfen wenn Konto gesperrt ist
     */
    @Test
    void abheben_KontostandIstUnter50Cent()throws GesperrtException{
        assertFalse(konto1.abheben(100, Waehrung.EUR));
        assertEquals(100, konto1.getKontostand());
    }

    /**
     * testet abheben wenn der Betrag kleiner gleich der Abhebesumme ist
     * @throws GesperrtException geworfen bei gesperrtem Konto
     */
    @Test
    void abheben_betragKleinerGleichAbhebesumme() throws GesperrtException{
        konto1.setKontostand(2000.50);
        assertTrue(konto1.abheben(2000, Waehrung.EUR));
        assertEquals(0.5, konto1.getKontostand());
    }

    /**
     * testet abheben wenn der Betrag groesser als die erlaubte Abhebesumme ist
     * @throws GesperrtException geworfen bei gesperrtem Konto
     */
    @Test
    void abheben_betragGroesserAbhebesumme() throws GesperrtException{
        konto1.setKontostand(2100);
        assertFalse(konto1.abheben(2050, Waehrung.EUR));
        assertEquals(2100, konto1.getKontostand());
    }

    /**
     * testet abheben wenn der abzuhebene Betrag in einer anderen Waehrung angegeben wird
     * @throws GesperrtException geworfen bei gesperrtem Konto
     */
    @Test
    void abheben_andereWaehrungAlsEuro() throws GesperrtException{
        konto1.setKontostand(100.5);
        konto1.abheben(10982.69, Waehrung.ESCUDO);
        assertEquals(0.5, konto1.getKontostand());
    }
}