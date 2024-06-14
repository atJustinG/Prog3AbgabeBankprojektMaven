package bankprojekt.verarbeitung;

public class AktienKonto extends Konto{





    @Override
    public boolean abheben(double betrag) throws GesperrtException {
        return false;
    }
}
