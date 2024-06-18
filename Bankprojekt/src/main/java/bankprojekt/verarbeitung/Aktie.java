package bankprojekt.verarbeitung;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Eine Aktie, die ständig ihren Kurs verändert
 * @author Doro
 *
 */
public class Aktie {
	
	private static Map<String, Aktie> alleAktien = new HashMap<>();
	private String wkn;
	private double kurs;
	private final Lock l = new ReentrantLock();
	private final Condition kursUnterPreis = l.newCondition();
	private final Condition kursUeberPreis = l.newCondition();

	ScheduledExecutorService schedExService;
	
	/**
	 * gibt die Aktie mit der gewünschten Wertpapierkennnummer zurück
	 * @param wkn Wertpapierkennnummer
	 * @return Aktie mit der angegebenen Wertpapierkennnummer oder null, wenn es diese WKN
	 * 			nicht gibt.
	 */
	public static Aktie getAktie(String wkn)
	{
		return alleAktien.get(wkn);
	}
	
	/**
	 * erstellt eine neu Aktie mit den angegebenen Werten
	 * erstellt einen executor der den Kurs der Aktie immer zufällig um -3% bis 3% ändert
	 * sobald der Kurs unter bzw uber einem bestimmten Preis fällt signalisiert er dies.
 	 * @param wkn Wertpapierkennnummer
	 * @param k aktueller Kurs
	 * @throws IllegalArgumentException wenn einer der Parameter null bzw. negativ ist
	 * 		                            oder es eine Aktie mit dieser WKN bereits gibt
	 */
	public Aktie(String wkn, double k) {
		if(wkn == null || k <= 0 || alleAktien.containsKey(wkn))
			throw new IllegalArgumentException();	
		this.wkn = wkn;
		this.kurs = k;
		alleAktien.put(wkn, this);
		schedExService = Executors.newScheduledThreadPool(0);
		schedExService.scheduleAtFixedRate(() ->{
			l.lock();
			try{
				double preisAendern = (Math.random() * 6) - 3;
				this.kurs += this.kurs * (preisAendern /100);
				kursUnterPreis.signalAll();
				kursUeberPreis.signalAll();
			} finally{
				l.unlock();
			}
		}, 1,1, TimeUnit.SECONDS);



	}

	/**
	 * diese Methode wartet solange wie der Preis nicht unter einem bestimmten Preis fällt um sie zu kaufen
	 * @param preis der Preis unter dem der Kurs sein muss damit die Aktie gekauft wird
	 * @throws InterruptedException falls der Thread interrupted wird
	 */
	public void warteBisKursUnter(double preis) throws InterruptedException {
		l.lock();
		try {
			while (kurs > preis) {
				kursUnterPreis.await();
			}
		} finally {
			l.unlock();
		}
	}

	/**
	 * diese Methode wartet solange der Preis nicht ueber einem bestimmten Preis ist um sie zu verkaufen
	 * @param preis der Preis ueber dem der Kurs sein muss damit die Aktie gekauft wird
	 * @throws InterruptedException falls der Thread interrupted wird
	 */
	public void warteBisKursUeber(double preis) throws InterruptedException {
		l.lock();
		try{
			while(kurs < preis){
				kursUeberPreis.await();
			}
		}finally{
			l.unlock();
		}
	}

	/**
	 * Wertpapierkennnummer
	 * @return WKN der Aktie
	 */
	public String getWkn() {
		return wkn;
	}

	/**
	 * aktueller Kurs
	 * @return Kurs der Aktie
	 */
	public double getKurs() {
		return kurs;
	}
}
