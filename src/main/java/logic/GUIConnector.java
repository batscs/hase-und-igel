package logic;

import gui.data.Error;

import java.util.List;
import java.util.function.Consumer;

/**
 * Interface als Schnittstelle zwischen Game Klasse und Benutzeroberfläche.
 *
 * @author github.com/batscs
 */
public interface GUIConnector {

    /**
     * Initialisiert einen bestimmten Spieler im GUI und positioniert ihn anhand von dem
     * Positions-Attribut. Gleichzeitig wird die Spieler-Instanz als Referenz verwendet, um bei
     * Vergrößerung / Verkleinerung die Position zu behalten.
     *
     * @param playerIdx Identifikation des Spielers.
     * @param player    Der relevante Spieler
     */
    void initializePlayer(int playerIdx, Player player);

    /**
     * Aktualisiert die Position des Spielers auf der Oberfläche.
     *
     * @param playerIdx Identifikation des Spielers.
     * @param position  Die neue Position.
     */
    void alignPlayer(int playerIdx, int position);

    /**
     * Methode zum Visualisieren der Bewegung des Spielers mit Callback-Funktion an das Spiel,
     * dass die Animation vollendet ist.
     *
     * @param playerIdx Der Index des zu bewegenden Spielers
     * @param start Startposition der Bewegung
     * @param destination Endposition der Bewegung
     * @param afterMoveEvent Funktionsobjekt, soll aufgerufen werden NACH der Animation
     */
    void movePlayer(int playerIdx, int start, int destination, Runnable afterMoveEvent);

    /**
     * Methode zum Anzeigen des aktuellen Spielers im GUI, dabei wird der Name sowie die Anzahl der
     * Karotten und Salate
     * angezeigt.
     *
     * @param playerIdx Der Index / Die Identifikation des Spielers
     * @param username Spielername des Spielers
     * @param carrots Anzahl der Karotten des Spielers
     * @param salads Anzahl der Salate des Spielers
     */
    void updateStats(int playerIdx, String username, int carrots, int salads);

    /**
     * Methode welche eine vordefinierte Reihenfolge der Spieler setzt. Wird verwendet für das Laden
     * eines Spielstandes
     *
     * @param onTarget Liste mit Player-Indizes, Reihenfolge entsprechend der Rangordnung
     */
    void setOnTarget(List<Integer> onTarget);

    /**
     * Methode, welche die Visualisierung eines Feldes anhand der Erreichbarkeit verändert.
     *
     * @param index     Index des Feldes
     * @param reachable Erreichbarkeit des Feldes; true, wenn erreichbar
     */
    void changeFieldBackgroundColor(int index, boolean reachable);

    /**
     * Methode zum visuellen Hervorheben eines Spielers
     *
     * @param playerIdx Der Index des Spielers
     */
    void highlightPlayer(int playerIdx);

    /**
     * Methode zum Entfernen der visuellen Hervorhebung eines Spielers
     *
     * @param playerIdx Der Index des Spielers
     */
    void unhighlightPlayer(int playerIdx);

    /**
     * Methode zum Einstellen der Oberfläche beim Spielende
     */
    void gameStop();

    /**
     * Methode zum Einstellen der Oberfläche beim Spielstart
     *
     * @param amountPlayers Anzahl der teilnehmenden Spieler
     */
    void gameStart(int amountPlayers);

    /**
     * Methode, welche der Oberfläche mitteilt, dass ein bestimmter Spieler gewonnen hat.
     *
     * @param winnerIdx Index des Siegers
     */
    void gameWin(int winnerIdx);

    /**
     * Methode zum Anzeigen eines Alerts, falls im Program ein Fehler auftritt
     *
     * @param error Der aufgetretene und nun anzuzeigende Fehler
     */
    void alertError(Error error);

    /**
     * Methode zum Anzeigen eines Alerts zur Programmlaufzeit, welche den Benutzer auf eine
     * Information hinweist, erhält zusätzliche Argumente welche in der description ein '%s'
     * ersetzen können.
     *
     * @param header Überschrift der Informationsmeldung
     * @param description Beschreibung der Informationsmeldung
     * @param args Zusätzliche Argumente für die Beschreibung
     */
    void alertInfo(Token header, Token description, Object... args);

    /**
     * Alert welches Spielerbezogene Tokens ausgeben kann (z.B. {username}) durch den Player Parameter.
     * Nach dem Alert wird das übergebene Runnable event ausgeführt.
     *
     * @param msg Der Text für das Event
     * @param player Der bezogene Spieler
     * @param event Das auszuführende Event nach dem Alert
     */
    void alertEvent(Token msg, Player player, Runnable event);

    /**
     * Methode welche den Benutzer auffordert eine Aktion zu bestätigen oder abzubrechen,
     * verarbeitet wird die Eingabe des Benutzers im Consumer func
     *
     * @param message Nachricht der Aufforderung
     * @param func Consumer für flexible Übergabe von Funktionen
     */
    void alertConfirm(Token message, Consumer<Boolean> func, Object... args);

    /**
     * Methode welche den Benutzer auffordert zwischen mehreren Optionen zu entscheiden.
     * Auswertung über den Consumer func, Rückgabewert ist dabei gleich der Reihenfolge der Option.
     *
     * @param msg   Beschreibung der Aufforderung
     * @param func  Funktionsobjekt zur Auswertung
     * @param options   Strings für die zusätzlichen Optionen.
     *                  Erste Option repräsentiert immer den Abbruch.
     */
    void alertMultipleChoice(Token msg, Consumer<Integer> func, Token... options);
}
