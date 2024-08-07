package bakprojekt.verwaltung;

import bankprojekt.verarbeitung.*;
import bankprojekt.verwaltung.Bank;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

/**
 * Klasse zum testen
 */
class BankTest {

    Bank bank;
    @Mock
    Girokonto mockKonto1;
    @Mock
    Girokonto mockKonto2;
    @Mock
    UeberweisungsfaehigesKonto mockKontoNichtInListe;

    long kontoNummer1;
    long kontoNummer2;
    long kontonummerSparbuch;
    private AutoCloseable autoCloseable;

    /**
     * setUp Methode um alle Tests durchzuführen
     * erstellt mock und testObjekte
     */
    @BeforeEach
    public void setUp(){
        autoCloseable = MockitoAnnotations.openMocks(this);
        bank = new Bank(10080000);
        Mockito.when(mockKonto1.getKontonummer()).thenReturn(1L);
        Mockito.when(mockKonto1.getInhaber()).thenReturn(new Kunde());
        Mockito.when(mockKonto1.getDispo()).thenReturn(500.0);
        Mockito.when(mockKonto2.getKontonummer()).thenReturn(2L);
        Mockito.when(mockKonto2.getInhaber()).thenReturn(new Kunde());
        Mockito.when(mockKonto2.getDispo()).thenReturn(300.0);
        kontoNummer1 = bank.kontoErstellen(new GirokontoFabrik(mockKonto1.getDispo()), mockKonto1.getInhaber(), mockKonto1.getKontonummer());
        kontoNummer2 = bank.kontoErstellen(new GirokontoFabrik(mockKonto2.getDispo()), mockKonto2.getInhaber(), mockKonto2.getKontonummer());
        Mockito.when(mockKonto1.getInhaber()).thenReturn(new Kunde());
        Mockito.when(mockKonto2.getInhaber()).thenReturn(new Kunde());
        Mockito.when(mockKontoNichtInListe.getInhaber()).thenReturn(new Kunde());
        Mockito.when(mockKonto1.getKontostand()).thenReturn(500.0);
        Mockito.when(mockKonto2.getKontostand()).thenReturn(0.0);
        Mockito.when(mockKonto1.getKontonummer()).thenReturn(kontoNummer1);
        Mockito.when(mockKonto2.getKontonummer()).thenReturn(kontoNummer2);

    }


    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    /**
     * testet konto Loeschen bei einem Konto das nicht in der Liste ist
     */
    @Test
    void kontoLoeschenWennNummerNichtInListeDannFalse() {
        assertFalse(bank.kontoLoeschen(1521258551));
    }

    /**
     * testet KontoLoeschen Methode wenn das Konto existiert
     */
    @Test
    void kontoLoeschenWennNummerNichtNullDannTrue() {
        assertTrue(bank.kontoLoeschen(kontoNummer1));
    }

    /**
     * testet kontoLoeschen wenn versucht wird das gleiche Konto zweimal zu loeschen
     */
    @Test
    void kontoLoeschenWennKontoBereitsGeloeschtDannFalse(){
        assertTrue(bank.kontoLoeschen(kontoNummer1));
        assertFalse(bank.kontoLoeschen(kontoNummer1));
    }

    /**
     * testet geldUeberweisen wenn das Konto nicht gedeckt ist
     * @throws GesperrtException wird bei einem gesperrtem Konto geworfen
     */
    @Test
    void geldUeberweisenWennGirokontoNichtGedecktDannFalse() throws GesperrtException {
        Mockito.when(mockKonto1.ueberweisungAbsenden(anyDouble(), anyString(), eq(kontoNummer2),
                anyLong(), anyString())).thenReturn(false);
        assertFalse(bank.geldUeberweisen(kontoNummer1, kontoNummer2, 501, "sollte nicht gehen!"));
    }

    /**
     * testet geldUeberweisen wenn KontoVon nicht existiert
     * @throws GesperrtException wirft gesperrt bei einem gesperrten Konto
     */
    @Test
    void geldUeberweisenWennGirokontoVonExistiertNichtInListeDannFalse() throws GesperrtException {
        Mockito.when(mockKontoNichtInListe.ueberweisungAbsenden(anyDouble(), anyString(),
                eq(kontoNummer2), anyLong(), anyString())).thenReturn(false);
        assertFalse(bank.geldUeberweisen(mockKontoNichtInListe.getKontonummer(), kontoNummer2, 1500, "sollte nicht gehen!"));
    }

    /**
     * testet geldUeberweisen bei einem gedecktem Konto zu einem existierenden Girokonto
     * @throws GesperrtException wird bei einem gesperrten Konto geworfen
     */
    @Test
    void geldUeberweisenWennKontoVonGedecktDannTrue() throws GesperrtException{
        Mockito.when(mockKonto1.ueberweisungAbsenden(anyDouble(), anyString(),
                eq(kontoNummer2), anyLong(), anyString())).thenReturn(true);
        assertTrue(bank.geldUeberweisen(kontoNummer1, kontoNummer2, 300,"sollte funktionieren!"));
    }


    /**
     * testet Die Klon Methode der Bank
     */
    /*@Test
    public void testBankClone() {
        Bank originalBank = new Bank(123456);
        Kunde kunde = new Kunde();
        long kontoNummer = originalBank.kontoErstellen(new GirokontoFabrik(500), new Kunde(), 123568971);
        Bank clonedBank = originalBank.clone();
        //ueberprüft dass die Objekte ordnungsgemäß geklont wurden
        assertEquals(originalBank.getAlleKonten(), clonedBank.getAlleKonten());
        Girokonto originalKonto = (Girokonto) originalBank.getKonto(kontoNummer);
        Girokonto clonedKonto = (Girokonto) clonedBank.getKonto(kontoNummer);
        //ueberprueft dass das Konto und das geklonte Konto nicht das selbe Objekt sind
        assertNotSame(originalKonto, clonedKonto);
        double geldWert = 100.0;
        originalKonto.einzahlen(geldWert);
        //ueberprueft dass wenn man das original Konto verändert nicht das geklonte Konto mitverändert wird
        assertEquals(geldWert, originalKonto.getKontostand());
        assertEquals(0.0, clonedKonto.getKontostand());
        clonedKonto.einzahlen(geldWert*2);
        assertEquals((geldWert*2), clonedKonto.getKontostand());
        assertEquals(geldWert, originalKonto.getKontostand());
    }*/

}