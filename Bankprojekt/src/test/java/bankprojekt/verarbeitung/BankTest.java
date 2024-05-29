package bankprojekt.verarbeitung;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {

    Bank mockBank;
    Bank bank;
    Konto mockKonto1;
    Konto mockKonto2;
   // Kunde mockKunde;
    Konto mockKontoNichtInListe;
    long kontoNummer1;
    long kontoNummer2;
    long kontonummerSparbuch;

    /**
     * setUp Methode um alle Tests durchzuführen
     * erstellt mock und testObjekte
     */
    @BeforeEach
    public void setUp(){
        mockBank = Mockito.mock(Bank.class);
        mockKonto1 = Mockito.mock(Konto.class);
        mockKonto2 = Mockito.mock(Konto.class);
        mockKontoNichtInListe = Mockito.mock(Konto.class);
       // mockBank.mockEinfuegen(mockKonto1);
       // mockBank.mockEinfuegen(mockKonto2);



        bank = new Bank(10080000);
        bank.girokontoErstellen(new Kunde());
        bank.girokontoErstellen(new Kunde());
        bank.sparbuchErstellen(new Kunde());
        kontoNummer1 = 1;
        kontoNummer2 = 2;
        kontonummerSparbuch = 3;
    }


    /**
     * testet konto Loeschen bei einem Konto das nicht in der Liste ist
     */
    @Test
    void kontoLoeschenWennNummerNichtInListeDannFalse() {
        Mockito.when(mockBank.kontoLoeschen(1521258551)).thenReturn(false);
        assertFalse(bank.kontoLoeschen(1521258551));
    }

    /**
     * testet KontoLoeschen Methode wenn das Konto existiert
     */
    @Test
    void kontoLoeschenWennNummerNichtNullDannTrue() {
        Mockito.when(mockBank.kontoLoeschen(mockKonto1.getKontonummer())).thenReturn(true);
        assertTrue(bank.kontoLoeschen(1));
    }

    /**
     * testet kontoLoeschen wenn versucht wird das gleiche Konto zweimal zu loeschen
     */
    @Test
    void kontoLoeschenWennKontoBereitsGeloeschtDannFalse(){
        Mockito.when(mockBank.kontoLoeschen(1)).thenReturn(true);
        Mockito.when(mockBank.kontoLoeschen(1)).thenReturn(false);
        bank.kontoLoeschen(1);
        assertFalse(bank.kontoLoeschen(1));
    }

    /**
     * testet geldUeberweisen wenn das Konto nicht gedeckt ist
     * @throws GesperrtException wird bei einem gesperrtem Konto geworfen
     */
    @Test
    void geldUeberweisenWennGirokontoNichtGedecktDannFalse() throws GesperrtException {
        Konto mockKontoDispo = Mockito.mock(Konto.class);
        mockBank.mockEinfuegen(mockKontoDispo);
        mockKontoDispo.setKontostand(-500);
        Mockito.when(mockBank.geldUeberweisen(mockKontoDispo.getKontonummer(), mockKonto2.getKontonummer(), 500, "sollte nicht gehen!")).thenReturn(false);
        assertFalse(bank.geldUeberweisen(1, 2, 501, "sollte nicht gehen!"));
    }

    /**
     * testet geldUeberweisen wenn KontoVon nicht existiert
     * @throws GesperrtException wirft gesperrt bei einem gesperrten Konto
     */
    @Test
    void geldUeberweisenWennGirokontoVonExistiertNichtInListeDannFalse() throws GesperrtException {
        Mockito.when(mockBank.geldUeberweisen(mockKontoNichtInListe.getKontonummer(), mockKonto2.getKontonummer(), 1500, "sollte nicht gehen!")).thenReturn(false);
        assertFalse(bank.geldUeberweisen(15, 2, 1500, "sollte nicht gehen!"));
    }

    /**
     * testet geldUeberweisen bei einem gedecktem Konto zu einem existierenden Girokonto
     * @throws GesperrtException wird bei einem gesperrten Konto geworfen
     */
    @Test
    void geldUeberweisenWennKontoVonGedecktDannTrue() throws GesperrtException{
        mockKonto1.setKontostand(1500);
        Mockito.when(mockBank.geldUeberweisen(mockKonto1.getKontonummer(), mockKonto2.getKontonummer(), 1000,"sollte funktionieren!")).thenReturn(true);
        assertTrue(bank.geldUeberweisen(1, 2, 300,"sollte funktionieren!"));
    }

    /**
     * testet geldUeberweisen wenn KontoZu ein Sparbuch ist
     * @throws GesperrtException wird bei einem gesperrten Konto geworfen
     */
    @Test
    void GeldUeberweisenWennKontoZUSparbuchDannFalse() throws GesperrtException {
        Konto mockSparbuch = Mockito.mock(Sparbuch.class);
        Mockito.when(mockBank.geldUeberweisen(mockKonto1.getKontonummer(), mockSparbuch.getKontonummer(), 500, "sollte nicht gehen da Sparbuch nicht überweisungsfäöhig ist")).thenReturn(false);
        assertFalse(bank.geldUeberweisen(1, 3, 500, "sollte nicht gehen da Sparbuch nicht überweisungsfäöhig ist"));

    }

    /**
     * testet geldUeberweisen auf den richtigen Betrag bei KontoZu
     * @throws GesperrtException wird bei einem gesperrten Konto geworfen
     */
    @Test
    void geldUeberweisenWennKontoZuHatKontostandDannEqualsValue() throws GesperrtException{
        Mockito.when(mockBank.geldUeberweisen(mockKonto1.getKontonummer(), mockKonto2.getKontonummer(), 500, "500 Euro fuer Konto 2")).thenReturn(true);
        bank.geldUeberweisen(1, 2, 300, "500 Euro fuer Konto 2");
        assertEquals(300, bank.getKontostand(2));
    }

    /**
     * testet werfen bei geldUeberweisen wenn KontoVon gesperrt ist
     * @throws GesperrtException wird bei einem gesperrten Konto geworfen
     */
    @Test
    void geldUeberweisenWennKontoIstGesperrt() throws GesperrtException {
        Konto mockGesperrt = Mockito.mock(Konto.class);
        mockGesperrt.sperren();
        mockBank.mockEinfuegen(mockGesperrt);
        Mockito.when(mockBank.geldUeberweisen(4545578, 1, 300,"sollte nicht gehen")).thenThrow(GesperrtException.class);
        bank.girokontoErstellen(new Kunde());
        bank.getKonto(4).sperren();
        assertThrowsExactly(GesperrtException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                bank.geldUeberweisen(4, 1, 200,"sollte werfen");
            }
        });

    }

}