package bankprojekt.verarbeitung;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {

    Bank b;
    Konto mockKonto1;
    Konto mockKonto2;
    Kunde mockKunde;
    Konto mockKontoNichtInListe;

    @BeforeEach
    public void setUp(){
        b = Mockito.mock(Bank.class);
        mockKonto1 = Mockito.mock(Konto.class);
        mockKonto2 = Mockito.mock(Konto.class);
        mockKunde = Mockito.mock(Kunde.class);
        mockKontoNichtInListe = Mockito.mock(Konto.class);
        b.mockEinfuegen(mockKonto1);
        b.mockEinfuegen(mockKonto2);
    }



    @Test
    void kontoLoeschenWennNummerNichtInListeDannFalse() {
        Mockito.when(b.kontoLoeschen(mockKontoNichtInListe.getKontonummer())).thenReturn(false);
    }

    @Test
    void kontoLoeschenWennNummerNichtNullDannTrue() {
        Mockito.when(b.kontoLoeschen(mockKonto1.getKontonummer())).thenReturn(true);
    }

    @Test
    void geldUeberweisenWennGirokontoNichtGedecktDannFalse() throws GesperrtException {
        Konto mockKontoDispo = Mockito.mock(Konto.class);
        b.mockEinfuegen(mockKontoDispo);
        mockKontoDispo.setKontostand(-500);
        Mockito.when(b.geldUeberweisen(mockKontoDispo.getKontonummer(), mockKonto2.getKontonummer(), 500, "sollte nciht gehen!")).thenReturn(false);
    }

    @Test
    void geldUeberweisenWennGirokontoVonExistiertNichtInListeDannFalse() throws GesperrtException {
        Mockito.when(b.geldUeberweisen(mockKontoNichtInListe.getKontonummer(), mockKonto2.getKontonummer(), 1500, "sollte nicht gehen!")).thenReturn(false);
    }

    @Test
    void geldUeberweisenWennKontoGesperrtDannException() throws GesperrtException{
        Konto mockKontoGesperrt = Mockito.mock(Konto.class);
        mockKontoGesperrt.sperren();
        b.mockEinfuegen(mockKontoGesperrt);
        Mockito.when(b.geldUeberweisen(mockKontoGesperrt.getKontonummer(), mockKonto1.getKontonummer(), 5400, "gesperrt")).thenThrow();
    }

    @Test
    void geldUeberweisenWennKontoVonGedecktDannTrue() throws GesperrtException{
        mockKonto1.setKontostand(1500);
        Mockito.when(b.geldUeberweisen(mockKonto1.getKontonummer(), mockKonto2.getKontonummer(), 1000,"sollte funktionieren!")).thenReturn(true);
    }
}