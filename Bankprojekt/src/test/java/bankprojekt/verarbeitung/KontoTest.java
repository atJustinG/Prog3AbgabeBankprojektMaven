package bankprojekt.verarbeitung;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class KontoTest {
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



}