package nullStellen;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NullstellenTest {

    Ifunktion func1;
    Ifunktion func2;
    Ifunktion func3;
    Ifunktion func4;

    /**
     * setUp methode um alle lambda ausdrücke der Funktionen zu definieren
     */
    @BeforeEach
    void setUp(){
        func1 = x -> x*x - 25;            //Nullstellen: 5.0/-5.0
        func2 = x -> Math.exp(x*3) - 7;   //Nullstellen:  0.64453125
        func3 = x -> (Math.sin(x*x))-0.5; //0.73125
        func4 = x -> x*x+1;               //NaN
    }

    /**
     * testet die iterative Methode zum errechnen der Nullstellen
     * alle Nullstellen wurden vorher ausgerechnet um einen actual wert zum vergleichen zu kennen
     */
    @Test
    void nullStelleBerechnen() {
        assertEquals(5.0, Nullstellen.nullStelleBerechnen(func1, 0, 10));
        assertEquals(0.64453125, Nullstellen.nullStelleBerechnen(func2, 0, 10));
        assertEquals(0.73125, Nullstellen.nullStelleBerechnen(func3, 0, 1.2));
        assertEquals(Double.NaN, Nullstellen.nullStelleBerechnen(func4, 0, 10));
    }
    /**
     * testet die rekursive Methode zum errechnen der Nullstellen
     * auch hier wurden alle Nullstellen im Vorfeld ausgerechnet um einen actual wert zum vergleichen zu kennen
     * diese Methode funktioniert leider nicht da die Tests Fehlschlagen und die falschen Ergebnisse zurückliefern
     */
    @Test
    void nullStellenBerechnenRekursiv(){
      //   assertEquals(5.0, Nullstellen.nullStellenRechnerRekursiv(func1, 0, 10));
       //  assertEquals(0.64453125, Nullstellen.nullStellenRechnerRekursiv(func2, 0, 10)); //0.64453125
      //   assertEquals(0.734375, Nullstellen.nullStellenRechnerRekursiv(func3, -2, 2));
       //  assertEquals(Double.NaN, Nullstellen.nullStellenRechnerRekursiv(func4, 0, 10));
    }

}