package tuerme_von_Hanoi;

import java.util.ArrayList;

/**
 * @author Daniel Biedermann, Katerina Milenkovski
 * Klasse zum Lösen der Türme von Hanoi
 */

public class HanoiLoesung {

	private int counter = 0;
	
	
    /**
     * Methode zum berechnen der Schritte zum lösen der Türme von Hanoi
     *
     * @param n Anzahl der Scheiben
     * @return Liste der Schritte
     */
    public ArrayList<Paar<Integer>> hanoi(int n) {
        ArrayList<Paar<Integer>> schritteListe  = new ArrayList<>();
        //Berechne den Algorithmus.
        rekursiv(n, 0, 2, 1, schritteListe );
        System.out.println(counter);
        return schritteListe ;
        
    }
    
    /**
     * Rekurive Methode der einzelnen Schritte des Sortierens
     *
     * @param n Anzahl der Scheiben, start Startturm, end Endturm, ablage Ablageturm, schritte Liste der Schritte
     * 
     */
    private void rekursiv(int n, int start, int ziel, int ablage, ArrayList<Paar<Integer>> moves) {
    	counter ++;
    	if (n == 1) {
            //Füge Schritt hinzu und breche Rekursion ab
            moves.add(new Paar<>(start, ziel)); // bewege disk von start nach ziel
        } else {
            //Weitere Rekursive Aufrufe
            rekursiv(n - 1, start, ablage, ziel, moves);
            moves.add(new Paar<>(start, ziel)); // bewege disk von start nach ziel
            rekursiv(n - 1, ablage, ziel, start, moves);
        }
    }
    
}

