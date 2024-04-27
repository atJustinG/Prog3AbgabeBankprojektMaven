package bankprojekt;

import bankprojekt.verarbeitung.GesperrtException;
import bankprojekt.verarbeitung.Girokonto;
import bankprojekt.verarbeitung.Konto;

/**
 * Testprogramm für Konten
 * @author Doro
 *
 */
public class KontenSpielereien {

	/**
	 * Testprogramm für Konten
	 * @param args wird nicht benutzt
	 * @throws GesperrtException
	 */
	public static void main(String[] args) throws GesperrtException {
		
		Konto meins = new Girokonto(); //ZUweisungskompatibilität
		//boolean geklappt = meins.abheben(100); //in Girokonto
		//System.out.println(meins.toString()); //in Girokonto!!!!!
		meins.aufDerKonsoleAusgeben(); //Ausgabe aus Girokonto
		
/*		
		Kontoart art;
		art = Kontoart.GIROKONTO;
		System.out.println(art.name() + " " + art.ordinal() + ": " + art.getWerbebotschaft());
		String eingabe = "SPARBUCH";
		art = Kontoart.valueOf(eingabe);
		
		System.out.println("Unsere Angebote:");
		Kontoart[] arten = Kontoart.values();
		for(int i=0; i< arten.length; i++)
		{
			art = arten[i];
			System.out.println(art.name() + ": " + art.getWerbebotschaft());
		}
		Kunde ich = new Kunde("Dorothea", "Hubrich", "zuhause", LocalDate.parse("1976-07-13"));

		Girokonto meinGiro = new Girokonto(ich, 1234, 1000.0);
		meinGiro.einzahlen(50);
		System.out.println(meinGiro);
		
		Sparbuch meinSpar = new Sparbuch(ich, 9876);
		meinSpar.einzahlen(50);
		try
		{
			boolean hatGeklappt = meinSpar.abheben(70);
			System.out.println("Abhebung hat geklappt: " + hatGeklappt);
			System.out.println(meinSpar);
		}
		catch (GesperrtException e)
		{
			System.out.println("Zugriff auf gesperrtes Konto - Polizei rufen!");
		}
*/
	}

}
