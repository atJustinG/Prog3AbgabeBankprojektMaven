package bankprojekt.verarbeitung;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * testet die Methoden waehrungswechsel und abheben für Girokonto
 */
class GiroKontoTest {
    Konto konto1;
    Konto konto2;
    KontoObserver observer;

    /**
     * Konto Objekt mit einem Kontostand von 100 Euro als basis für alle weiteren tests
     */
    @BeforeEach
    void setUp() {
        konto1 = new Girokonto();
        konto1.setKontostand(100);
        konto2 = new Sparbuch();
        konto2.setKontostand(300);
        observer = Mockito.mock(KontoObserver.class);
        konto1.registerObserver(observer);
        konto2.registerObserver(observer);
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
     * testet den wechsel von Euro auf Escudo
     */
    @Test
    void waehrungswechsel_vonEuroAufAndereWaehrung_dannAndereWaehrung(){
        konto1.waehrungswechsel(Waehrung.ESCUDO);
        assertEquals(Waehrung.ESCUDO, konto1.getAktuelleWaehrung());
        assertEquals(10982.69, konto1.getKontostand());
    }

    /**
     * testet den Waehrungswechsel von Escudo auf Euro
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
     * Test der testet ob die richtige Exception geworfen
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
     * testet das abheben wenn der Betrag NaN ist
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
     * testet das abheben bei einem Betrag von Double.POSITIVE_INFINITY
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
     * testet abheben wenn das Konto gesperrt ist
     */
    @Test
    void abheben_kontoIstGesperrt(){
        konto1.sperren();
        assertThrowsExactly(GesperrtException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                konto1.abheben(250, Waehrung.EUR);
            }
        });
    }

    /**
     * testet abheben wenn der Dispo nicht überzogen wird
     * @throws GesperrtException
     */
    @Test
    void abheben_dispoNichtUeberzogen() throws GesperrtException {
        assertTrue(konto1.abheben(500.0, Waehrung.EUR));
        assertEquals(-400.0,konto1.getKontostand());
    }

    /**
     * testet das abheben mit einem ueberzogenen Dispo
     * @throws GesperrtException
     */
    @Test
    void abheben_dispoUeberzogen()throws GesperrtException{
        assertFalse(konto1.abheben(700.0,Waehrung.EUR));
        assertEquals(100, konto1.getKontostand());
    }

    /**
     * testet abheben mit einer anderen Waehrung als Euro
     * @throws GesperrtException
     */
    @Test
    void abheben_andereWaehrungAlsEuro() throws GesperrtException{
        konto1.abheben(10982.69, Waehrung.ESCUDO);
          assertEquals(0, konto1.getKontostand());
    }

    /**
     * testet ob der Observer 1 mal aufgerufen wird
     */
    @Test
    void testObserverWirdBenachrichtigtBeimEinzahlen() {
        konto1.einzahlen(100);
        verify(observer, times(1)).update(eq(konto1), eq(100.0), eq(200.0));
    }

    /**
     * testet ob der observer update 1 mal aufgerufen wurde
     * @throws GesperrtException bei gesperrtem Konto
     */
    @Test
    void testObserverWirdBenachrichtigtBeimAbheben() throws GesperrtException{
        konto1.abheben(100);
        verify(observer, times(1)).update(eq(konto1), eq(100.0), eq(0.0));
    }

}