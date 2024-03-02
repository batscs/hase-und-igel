package logic.fields;

import logic.GUIConnector;
import logic.GameLogic;
import logic.Player;

/**
 * Interface als Schnittstelle eines Spielfeldes im Hase-Und-Igel-Spiel.
 *
 * @author github.com/batscs
 */
public interface Field {

    /**
     * Methode zum Ermitteln des Types eines feldes.
     *
     * @return das Feldtyp
     */
    FieldType getType();

    /**
     * Methode welche aufgerufen wird, sobald der Spieler seinen Zug auf dieses Feld beginnt.
     * Aufruf erfolgt hierbei bevor der Spieler tatsächlich versetzt wird, um Zugriff auf seine
     * vorherige Position zu haben. (Nützlich für das Erstatten von Zügen bei Hasenkarten, oder
     * bei Igelfeldern welche abhängig von der bewegten Strecke eine gewisse Anzahl an Karotten vergeben)
     *
     * @param target Spieler auf dem Feld
     */
    void interactBeforeMove(Player target);

    /**
     * Methode welche aufgerufen wird, sobald der Spieler auf dem Feld draufsteht,
     * Aufruf erfolgt aus dem GUI nach der move-Methode
     * <p>
     * Bezüglich Moodle TO-DO: Es muss das ganze Game übergeben werden, da unter anderem bei den Hasenkarten sehr
     * Flexibel gearbeitet werden muss. Man könnte die ganzen Methoden in GameBoard (oder eine ähnliche Klasse)
     * extrahieren.
     *
     * @param target Spieler auf dem Feld
     * @param game Referenz zum Spiel
     * @param gui Referenz zur Oberfläche
     */
    void interactAfterMove(Player target, GameLogic game, GUIConnector gui);

    /**
     * Methode welche aufgerufen wird, sobald der Spieler am Zug ist und bevor er sich auf ein neues Feld bewegt.
     * Nützlich für z.B. Salatfelder, welche nicht direkt beim Betreten des Feldes den Salat des Spielers konsumieren,
     * sondern erst das nächste Mal, wenn dieser Spieler am Zug ist.
     * <p>
     * Die ganze Spiellogik wird hier benötigt und nicht nur das Spielbrett, da unter anderem Karotten vergeben werden,
     * und diese zum laufenden Spiel gehören, und nicht zum Spielbrett.
     *
     * @param target Der Spieler, stehend auf diesem Feld
     * @param game   Referenz auf die Spiellogik
     * @param gui    Referenz auf das GUI
     */
    void interactOnIdle(Player target, GameLogic game, GUIConnector gui);

    /**
     * Methode zum Ermitteln der Position des Feldes auf dem Spielbrett
     *
     * @return die Position des Feldes auf dem Spielbrett
     */
    int getPosition();

}
