package bankprojekt.verarbeitung;

/**
 * Observer Interface für den Kontostand von Kontoobjekten
 */
public interface KontoObserver {
    void update(Konto konto, double letzterKontostand, double neuerKontostand);
}
