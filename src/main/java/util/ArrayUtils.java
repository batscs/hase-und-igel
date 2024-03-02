package util;

import java.util.Random;

/**
 * Klasse mit Methoden zur Verarbeitung von Arrays, um diese Logik von den Spielmechanismen zu
 * trennen.
 *
 * @author github.com/batscs
 */
public class ArrayUtils {

    /**
     * Tauscht die Elemente von zwei bestimmten Positionen im Array. Veränderung direkt im
     * übergebenen Array.
     *
     * @param arr  Der Array
     * @param posA Erste Tauschposition
     * @param posB Zweite Tauschposition
     * @param <T>  Datentyp des Arrays
     */
    public static <T> void swapElementsInArray(T[] arr, int posA, int posB) {
        T temp = arr[posA];
        arr[posA] = arr[posB];
        arr[posB] = temp;
    }

    /**
     * Methode, welche eine zufällige Ganzzahl in einem bestimmten Bereich liefert.
     *
     * @param min Untergrenze der Zahl
     * @param max Obergrenze der Zahl
     * @return Zufällig generierte Zahl, innerhalb der Grenzen.
     */
    public static int rand(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    /**
     * Methode, welche einen übergebenen Array in eine zufällige Abfolge bringt.
     *
     * @param arr Der Array
     * @param <T> Datentyp des Arrays
     */
    public static <T> void shuffleArray(T[] arr) {

        int shuffleTimes = arr.length * arr.length;
        int min = 0;
        int max = arr.length - 1;

        for (int i = 0; i < shuffleTimes; i++) {
            int posA = rand(min, max);
            int posB = rand(min, max);

            swapElementsInArray(arr, posA, posB);
        }

    }

    /**
     * Methode, welche das erste Element eines Arrays an den letzten Platz verschiebt, verwendet für
     * Hasenkarten bei der Ziehung der Eventkarte.
     *
     * @param arr Das zu verändernde Array
     * @param <T> Datentyp des Arrays
     */
    public static <T> void shiftArray(T[] arr) {
        if (arr.length == 0) {
            return;
        }

        T first = arr[0];
        System.arraycopy(arr, 1, arr, 0, arr.length - 1);
        arr[arr.length - 1] = first;
    }

    /**
     * Überprüft, ob ein int-Array eine bestimmte Zahl beinhaltet.
     *
     * @param arr  Der zu untersuchende Array
     * @param item Die zu findende Zahl
     * @return True, falls Zahl enthalten im Array.
     */
    public static boolean arrayContains(int[] arr, int item) {
        boolean found = false;

        for (int i = 0; i < arr.length && !found; i++) {
            if (arr[i] == item) {
                found = true;
            }
        }

        return found;
    }
}
