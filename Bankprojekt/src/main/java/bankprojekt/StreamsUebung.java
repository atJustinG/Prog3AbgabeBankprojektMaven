package bankprojekt;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import bankprojekt.verarbeitung.Girokonto;
import bankprojekt.verarbeitung.Konto;
import bankprojekt.verarbeitung.Kunde;
/**
* Klasse mit Übungen zu Streams
*/
public class StreamsUebung {

	/**
	 * Übungen zu Streams
	 * @param args wird nicht verwendet
	 */
	public static void main(String[] args) {
		Kunde hans = new Kunde("Hans", "Meier", "Unterm Regenbogen 19",LocalDate.of(1990, 1, 5));
		Kunde otto = new Kunde("Otto", "Kar", "Hoch über den Wolken 7",LocalDate.of(1992, 2, 25));
		Kunde sabrina = new Kunde("Sabrina", "August", "Im Wald 15",LocalDate.of(1988, 3, 21));
		Konto eins = new Girokonto(hans, 123, 0);
		eins.einzahlen(100);
		Konto zwei = new Girokonto(otto, 234, 0);
		zwei.einzahlen(200);
		Konto drei = new Girokonto(sabrina, 333, 0);
		drei.einzahlen(100);
		Konto vier = new Girokonto(sabrina, 432, 0);
		vier.einzahlen(500);
		Konto fuenf = new Girokonto(otto, 598, 0);
		fuenf.einzahlen(600);
		
		Map<Long, Konto> kontenmap = new HashMap<Long, Konto>();
		kontenmap.put(123L, eins);
		kontenmap.put(234L, zwei);
		kontenmap.put(333L, drei);
		kontenmap.put(432L, vier);
		kontenmap.put(598L, fuenf);


		//Liste aller Kunden ohne doppelte:
		//List<Kunde> a = 
		//System.out.println(a);


		System.out.println("-----------------");
		
		//Liste aller Kunden, sortiert nach ihrem Kontostand:
		//List<Kunde> b = 
		//System.out.println(b);
		System.out.println("-----------------");

		//fängt mindestens ein Kunde mit 'A' an?
		//boolean c = 
		//System.out.println(c);
		System.out.println("-----------------");

		//alle Kundennamen in einem String:
		//String d = 
		//System.out.println(d);		
		System.out.println("-----------------");

		//Haben alle Kunden im Jahr 1990 Geburtstag?
		//boolean e = 	
		//System.out.println(e);
		System.out.println("-----------------");

		//Wie viele verschiedene Kunden gibt es?
		//long f =
		//System.out.println(f);
		System.out.println("-----------------");

		//Map, die zu jedem Kunden alle seine Konten auflistet:
		//Map<Kunde, List<Konto>> g =
		//System.out.println(g);
					
	}

}
