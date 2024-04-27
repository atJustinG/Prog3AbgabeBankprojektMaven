package bankprojekt.verarbeitung;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class GiroKontoTest {
    Konto konto1;
    @BeforeEach
    void setUp() {
        konto1 = new Girokonto();
        konto1.setKontostand(100);
    }

    @Test
    void kontoWaehrungistEuro(){
        assertEquals(Waehrung.EUR, konto1.getAktuelleWaehrung());
        assertEquals(100, konto1.getKontostand());
    }

    @Test
    void waehrungswechsel_vonEuroAufEuro_dannEuro() {
        Waehrung w = Waehrung.EUR;
        konto1.waehrungswechsel(w);
        assertEquals(w, konto1.getAktuelleWaehrung());
        assertEquals(100, konto1.getKontostand());
    }

    @Test
    void waehrungswechsel_vonEuroAufAndereWaehrung_dannAndereWaehrung(){
        konto1.waehrungswechsel(Waehrung.Escudo);
        assertEquals(Waehrung.Escudo, konto1.getAktuelleWaehrung());
        assertEquals(10982.69, konto1.getKontostand());
    }

    @Test
    void waehrungswechsel_vonEscudoAufEuro_dannEuro(){
        konto1.setKontostand(0);
        konto1.waehrungswechsel(Waehrung.Escudo);
        konto1.setKontostand(10982.69);
        konto1.waehrungswechsel(Waehrung.EUR);
        assertEquals(Waehrung.EUR, konto1.getAktuelleWaehrung());
        assertEquals(100, konto1.getKontostand());
    }
    @Test
    void waehrungswechsel_vonEscudoaufEscudo(){
        konto1.setKontostand(0);
        konto1.waehrungswechsel(Waehrung.Escudo);
        konto1.setKontostand(10982.69);
        konto1.waehrungswechsel(Waehrung.Escudo);
        assertEquals(Waehrung.Escudo, konto1.getAktuelleWaehrung());
        assertEquals(10982.69, konto1.getKontostand());
    }

    @Test
    void abheben_betragKleinerNull(){
        assertThrowsExactly(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                konto1.abheben(-25.0, Waehrung.EUR);
            }
        });
    }

    @Test
    void abheben_betragIstNaN(){
        assertThrowsExactly(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                konto1.abheben(Double.NaN, Waehrung.EUR);
            }
        });
    }

    @Test
    void abheben_betragIstInfinite(){
        assertThrowsExactly(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                konto1.abheben(Double.POSITIVE_INFINITY, Waehrung.EUR);
            }
        });
    }

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

    @Test
    void abheben_dispoNichtUeberzogen() throws GesperrtException {
        assertTrue(konto1.abheben(500.0, Waehrung.EUR));
        assertEquals(-400.0,konto1.getKontostand());
    }

    @Test
    void abheben_dispoUeberzogen()throws GesperrtException{
        assertFalse(konto1.abheben(700.0,Waehrung.EUR));
        assertEquals(100, konto1.getKontostand());
    }

    @Test
    void abheben_andereWaehrungAlsEuro() throws GesperrtException{
        konto1.abheben(10982.69, Waehrung.Escudo);
          assertEquals(0, konto1.getKontostand());
    }



}