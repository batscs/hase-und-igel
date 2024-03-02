package logic;

import logic.data.CardDeck;
import logic.data.GamePosition;
import logic.data.NummerFlag;
import logic.fields.*;
import util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse zur Repräsentation des Spielbrettes.
 * Enthält Methoden bezogen auf das Spielbrett welche in der Game-Klasse verwendet werden.
 *
 * @author github.com/batscs
 */
public class GameBoard {

    private final Field[] fields;

    /**
     * Attribut für die Spieler, welche ins Ziel gelangt sind.
     * Reihenfolge entsprechend der Reihenfolge der Platzierung
     *
     */
    private final List<Player> onTarget;

    /**
     * Informationen aller relevanten Spieler des aktuellen Spielbretts.
     * um z.B. zu ermitteln auf welche Position ein Spieler muss um eine Position aufzurücken.
     */
    private final Player[] players;

    /**
     * Attribut für das Kartendeck der Hasenkarten.
     * Alle Hasenfelder verwenden das gleiche Kartendeck.
     */
    private final CardDeck cardDeck;

    /**
     * Konstruktor zum Initialisieren eines Spielfeldes anhand von teilnehmenden Spielern.
     *
     * @param players Die Daten der teilnehmenden Spieler
     */
    public GameBoard(Player[] players) {
        this.players = players;
        this.onTarget = new ArrayList<>();
        this.cardDeck = new CardDeck();

        this.fields = presetField();
    }

    /**
     * Methode zum Berechnen des Preises zum Bewegen eines Spielers.
     *
     * @param distance Distanz, der zu überquerenden Felder
     * @return Preis in Karotten
     */
    public static int calculatePrice(int distance) {

        // Negativen "Preis" gibt es nicht zu zahlen
        if (distance <= 0) {
            return 0;
        }

        // Gaußsche Summenformel
        return (distance * (distance + 1)) / 2;
    }

    /**
     * Liefert alle Felder des Spielfeldes.
     *
     * @return die Felder
     */
    Field[] getFields() {
        return fields;
    }

    /**
     * Gibt die aktuelle Liste von Spielern-Instanzen, welche sich im Ziel befinden. Aufsteigend
     * sortiert nach der Reihenfolge, in welcher diese das Ziel betreten haben.
     *
     * @return die Liste
     */
    List<Player> getOnTarget() {
        return onTarget;
    }

    /**
     * Ermittelt, ob ein Feld bereits besetzt ist.
     *
     * @param position Die Positon des zu ermittelnden Feldes
     * @return True, wenn das Feld besetzt ist.
     */
    boolean isOccupied(int position) {

        boolean occupied = false;

        for (Player player : players) {
            if (player.getPosition() == position) {
                occupied = true;
            }
        }

        return occupied;
    }

    /**
     * Methode, welche eine neue Instanz auf das Feld dem offiziellen Spielplan entsprechend
     * zurückgibt.
     *
     * @return Das Spielfeld
     */
    private Field[] presetField() {

        return new Field[] {
                new FieldEmpty(0), new FieldHase(1, cardDeck), new FieldKarotte(2), new FieldHase(3, cardDeck),
                new FieldNummer(4, NummerFlag.THREE), new FieldKarotte(5),
                new FieldHase(6, cardDeck), new FieldSalat(7), new FieldIgel(8),
                new FieldNummer(9, NummerFlag.FOUR), new FieldNummer(10, NummerFlag.TWO),
                new FieldIgel(11), new FieldNummer(12, NummerFlag.THREE), new FieldKarotte(13),
                new FieldHase(14, cardDeck), new FieldIgel(15),
                new FieldNummer(16, NummerFlag.FLAG), new FieldNummer(17, NummerFlag.TWO),
                new FieldNummer(18, NummerFlag.FOUR), new FieldIgel(19),
                new FieldNummer(20, NummerFlag.TWO), new FieldKarotte(21), new FieldSalat(22),
                new FieldNummer(23, NummerFlag.TWO),
                new FieldIgel(24), new FieldHase(25, cardDeck), new FieldKarotte(26),
                new FieldNummer(27, NummerFlag.FOUR), new FieldNummer(28, NummerFlag.THREE),
                new FieldNummer(29, NummerFlag.TWO), new FieldIgel(30), new FieldHase(31, cardDeck),
                new FieldNummer(32, NummerFlag.FLAG), new FieldKarotte(33),
                new FieldHase(34, cardDeck), new FieldNummer(35, NummerFlag.TWO),
                new FieldNummer(36, NummerFlag.THREE), new FieldIgel(37), new FieldKarotte(38),
                new FieldHase(39, cardDeck), new FieldKarotte(40),
                new FieldNummer(41, NummerFlag.TWO), new FieldSalat(42), new FieldIgel(43),
                new FieldNummer(44, NummerFlag.THREE), new FieldNummer(45, NummerFlag.FOUR),
                new FieldHase(46, cardDeck), new FieldNummer(47, NummerFlag.TWO),
                new FieldNummer(48, NummerFlag.FLAG),
                new FieldKarotte(49), new FieldIgel(50), new FieldHase(51, cardDeck),
                new FieldNummer(52, NummerFlag.THREE), new FieldNummer(53, NummerFlag.TWO),
                new FieldNummer(54, NummerFlag.FOUR),
                new FieldKarotte(55), new FieldIgel(56), new FieldSalat(57), new FieldHase(58, cardDeck),
                new FieldKarotte(59), new FieldNummer(60, NummerFlag.TWO),
                new FieldHase(61, cardDeck),
                new FieldSalat(62),
                new FieldHase(63, cardDeck),
                new FieldEmpty(64), // Finales Feld (Ziel)
        };
    }

    /**
     * Ermittelt die Position des nächsten Karottenfeldes ausgehend vom Spieler.
     *
     * @param protagonist Der Spieler
     * @return Index des gefundenen Feldes, oder Ursprungsposition, falls keines gefunden.
     */
    int getNextCarrotFieldPositionForProtagonist(Player protagonist) {
        int index = protagonist.getPosition();
        boolean found = false;

        for (int i = index; i < fields.length && !found; i++) {
            if (fields[i].getType().equals(FieldType.KAROTTE)) {
                if (!isOccupied(i)) {
                    found = true;
                    index = i;
                }
            }
        }

        if (!found) {
            return protagonist.getPosition();
        }

        return index;
    }

    /**
     * Ermittelt die Position des vorherigen Karottenfeldes ausgehend vom Spieler.
     *
     * @param protagonist Der Spieler
     * @return Index des gefundenen Feldes, oder Ursprungsposition, falls keines gefunden.
     */
    int getPreviousCarrotFieldPositionForProtagonist(Player protagonist) {
        int index = protagonist.getPosition();
        boolean found = false;

        for (int i = index; i >= 0 && !found; i--) {
            if (fields[i].getType().equals(FieldType.KAROTTE)) {
                if (!isOccupied(i)) {
                    index = i;
                    found = true;
                }
            }
        }

        return index;
    }

    /**
     * Ermittelt die Position des nächsten freien Feldes ausgehend vom Spieler zum Überholen des
     * nächsten voraus liegenden Spielers.
     *
     * @param protagonist Der Spieler
     * @return Index des gefundenen Feldes, oder Ursprungsposition, falls keines gefunden.
     */
    int getFreeForwardPositionForProtagonist(Player protagonist) {

        int currPlace = getPlaceOfPlayer(protagonist);

        // Erster Platz kann nicht weiter nach vorne Rücken (Platzierung fängt bei 0 an)
        if (currPlace > 0) {
            int desiredPosition = getPlayerOnPlace(currPlace - 1).getPosition() + 1;

            // Einzuholender Spieler darf nicht im Ziel sein (und muss innerhalb des Spielfeldes sein)
            if (desiredPosition > GameLogic.FINAL_FIELD_POSITION) {
                return protagonist.getPosition();
            }

            if (isReachableIgnorePrice(protagonist, desiredPosition)) {
                return desiredPosition;
            }

            int adjustedPosition = desiredPosition;

            // desiredPosition wird so lange angepasst, bis dies eine gültige Position ist
            for (int i = adjustedPosition; i < fields.length - 1 && adjustedPosition == desiredPosition; i++) {
                if (isReachableIgnorePrice(protagonist, i) && !isOccupied(i)) {
                    adjustedPosition = i;
                }
            }

            // Falls man auf das Zielfeld rutschen will, braucht man 0 Salate und weniger als 10 Karotten
            // Der Preis für den Weg ist hierbei belanglos
            if (adjustedPosition == GameLogic.FINAL_FIELD_POSITION) {
                if (protagonist.getSalads() == 0 && protagonist.getCarrots() <= 10) {
                    return adjustedPosition;
                } else {
                    return protagonist.getPosition();
                }
            }

            return adjustedPosition;
        }

        // Default man bleibt stehen wenn man bereits erster ist?
        return protagonist.getPosition();
    }

    /**
     * Wrapper Methode für die spezifischen zu ermittelnden Feldpositionen
     *
     * @param targetPosition das neue Feld wessen Position ermittelt werden soll
     * @return Die gefundene Position
     */
    int getAvailablePosition(GamePosition targetPosition, Player protagonist) {
        return switch (targetPosition) {
            case NEXTCARROT -> getNextCarrotFieldPositionForProtagonist(protagonist);
            case PREVIOUSCARROT -> getPreviousCarrotFieldPositionForProtagonist(protagonist);
            case FORWARDS -> getFreeForwardPositionForProtagonist(protagonist);
            case BACKWARDS -> getFreeBackwardPositionForProtagonist(protagonist);
            default -> -1;
        };
    }

    /**
     * Ermittelt die Position des nächsten freien Feldes ausgehend vom Spieler zum Zurückfallen
     * hinter den nächsten zurückliegenden Spieler.
     *
     * @param protagonist Der Spieler
     * @return Index des gefundenen Feldes, oder Ursprungsposition, falls keines gefunden.
     */
    int getFreeBackwardPositionForProtagonist(Player protagonist) {
        int currPlace = getPlaceOfPlayer(protagonist);

        if (currPlace < players.length - 1) {

            Player desiredPlayer = getPlayerOnPlace(currPlace + 1);

            // desiredPosition ist die Position direkt hinter dem Spieler,
            // hinter welchen man zurückziehen will
            int desiredPosition = desiredPlayer.getPosition() - 1;

            if (desiredPosition <= 0) {
                return 0;
            }

            if (!isOccupied(desiredPosition)) {

                if (!(fields[desiredPosition].getType().equals(FieldType.SALAT))) {
                    return desiredPosition;
                }

                // Auf ein Salat Feld kann man nur, wenn man noch Salate zur Verfügung hat
                if (protagonist.getSalads() > 0) {
                    return desiredPosition;
                }

            }

            // Falls die desiredPosition nicht erreichbar ist, wird mit der adjustedPosition soweit zurückgezogen,
            // bis man eine Position erhält, welche erreichbar ist. Im schlimmsten Fall wird bis auf den Startpunkt = 0
            // zurückgezogen
            int adjustedPosition = desiredPosition;

            for (int i = adjustedPosition; i >= 0 && adjustedPosition == desiredPosition; i--) {
                if (!isOccupied(i)) {

                    if (!(fields[i].getType().equals(FieldType.SALAT))) {
                        adjustedPosition = i;
                    } else {
                        if (protagonist.getSalads() > 0) {
                            return desiredPosition;
                        }
                    }

                }
            }

            return adjustedPosition == desiredPosition ? 0 : adjustedPosition;
        }

        return protagonist.getPosition(); // Letzter Platz bleibt stehen
    }

    /**
     * Liefert den Spieler auf dem gewünschten Platz in der Rangfolge.
     *
     * @param place Platz in der Rangfolge, 0-indiziert.
     * @return Der Spieler
     */
    Player getPlayerOnPlace(int place) {
        Player[] list = getSortedPlayers();
        if (place >= list.length || place < 0) {
            return null;
        }

        return list[place];
    }

    /**
     * Liefert den Platz in der Reihenfolge von einem bestimmten Spieler.
     *
     * @param player Der Spieler
     * @return Der Platz, 0-indiziert.
     */
    int getPlaceOfPlayer(Player player) {
        Player[] list = getSortedPlayers();

        int place = -1;

        for (int i = 0; i < list.length; i++) {
            if (player == list[i]) {
                place = i;
            }
        }

        return place;
    }

    /**
     * Gibt die Spieler in sortierter Reihenfolge (anhand der Position auf dem Feld) zurück.
     * Reihenfolge ist absteigend und beachtet nicht die Reihenfolge des Betretens in das Zielfeld.
     *
     * @return sortierter Spieler Array
     */
    private Player[] getSortedPlayers() {
        int participants = players.length;
        Player[] result = new Player[participants];

        // deep copy jedoch nicht für die Player-Instanzen
        System.arraycopy(players, 0, result, 0, participants);

        // Selection Sort
        for (int i = 0; i < participants; i++) {
            int maxIndex = i;
            int maxPosition = result[maxIndex].getPosition();

            for (int j = i; j < participants; j++) {
                if (maxPosition < result[j].getPosition()) {
                    maxIndex = j;
                    maxPosition = result[maxIndex].getPosition();
                }
            }

            ArrayUtils.swapElementsInArray(result, i, maxIndex);
        }

        return result;
    }

    /**
     * Ermittelt, ob eine Position für den Spieler erreichbar ist, ohne dabei den Preis für die
     * Distanz zu betrachten.
     *
     * @param protagonist Der zu betrachtende Spieler
     * @param destination Die Zielposition
     * @return True, falls erreichbar, sonst false
     */
    private boolean isReachableIgnorePrice(Player protagonist, int destination) {
        int distance = destination - protagonist.getPosition();

        // Suspended darf nichts
        if (protagonist.isSuspended()) {
            return false;
        }

        // Auf das Startfeld lässt sich nicht zurückziehen
        if (destination == 0) {
            return false;
        }

        // Alle Felder dürfen nicht besetzt sein
        if (isOccupied(destination)) {
            return false;
        }

        // Igel Felder kann man nicht beim Vorwärtsziehen erreichen
        if (distance > 0 && fields[destination].getType().equals(FieldType.IGEL)) {
            return false;
        }

        // Nur das erste hinterlegene unbesetzte Igelfeld darf besucht werden
        if (distance < 0 && fields[destination].getType().equals(FieldType.IGEL) && isIgelBetween(
                protagonist, destination)) {
            return false;
        }

        // Nach hinten darf man nur auf Igel Felder
        if (distance < 0 && !(fields[destination].getType().equals(FieldType.IGEL))) {
            return false;
        }

        // Salat Felder dürfen nur betreten werden, wenn man noch Salat essen kann
        if (fields[destination].getType().equals(FieldType.SALAT) && protagonist.getSalads() <= 0) {
            return false;
        }

        // Das letzte Feld ist das Ziel, dafür gibt es spezielle Konditionen
        if (destination == fields.length - 1 && !canReachFinish(protagonist)) {
            return false;
        }

        return true;
    }

    /**
     * Ermittelt, ob eine Position für den Spieler erreichbar ist.
     *
     * @param protagonist Der zu betrachtende Spieler
     * @param destination Die Zielposition
     * @return True, falls erreichbar, sonst false
     */
    boolean isReachable(Player protagonist, int destination) {

        int distance = destination - protagonist.getPosition();
        int price = calculatePrice(distance);

        // Prüft Regeln des Spieles ab
        if (!isReachableIgnorePrice(protagonist, destination)) {
            return false;
        }

        // Preis der Karotten zum bewegen darf nicht zu hoch sein
        return protagonist.getCarrots() >= price;
    }

    /**
     * Methode, welche überprüft, ob zwischen dem Spieler und einem Feld ein IgelFeld zwischen ist.
     * Benötigt, da laut Regeln nur auf das nächste zurückliegende freie Igelfeld gezogen werden darf.
     *
     * @param protagonist Der zuüberprüfende Spieler
     * @param destination position BEHIND (!!!!!!!) the protagonist
     * @return False, wenn kein benutzbares Igelfeld dazwischen ist.
     */
    private boolean isIgelBetween(Player protagonist, int destination) {

        int protagonistPosition = protagonist.getPosition();
        boolean between = false;

        for (int i = protagonistPosition - 1; i > destination && !between; i--) {
            if (fields[i].getType().equals(FieldType.IGEL) && !isOccupied(i)) {
                between = true;
            }
        }

        return between;
    }

    /**
     * Überprüft, ob der aktuelle Spieler das Ziel erreichen kann.
     *
     * @return True, wenn der Spieler das Ziel erreichen kann, ansonsten False.
     */
    boolean canReachFinish(Player protagonist) {
        int finishPosition = fields.length - 1;

        int price = calculatePrice(finishPosition - protagonist.getPosition());
        int maxCarrots = 10 + onTarget.size() * 10;
        int maxSalads = 0;

        if (protagonist.getCarrots() - price > maxCarrots) {
            return false;
        }

        if (protagonist.getSalads() > maxSalads) {
            return false;
        }

        if (price > protagonist.getCarrots()) {
            return false;
        }

        if (protagonist.isSuspended()) {
            return false;
        }

        return true;
    }

    /**
     * Liefert die Anzahl an Felder, welche für den Spieler erreichbar sind.
     *
     * @param target zu betrachtender Spieler.
     * @return Anzahl der Felder
     */
    int getReachableFields(Player target) {
        int sum = 0;

        for (int i = 0; i < fields.length; i++) {
            if (isReachable(target, i)) {
                sum++;
            }
        }

        return sum;
    }

    /**
     * Ermittelt, ob ein Spieler sich im Ziel befindet.
     *
     * @param player zu betrachtende Spieler
     * @return True, falls im Ziel
     */
    boolean hasReachedFinish(Player player) {
        return player.getPosition() == GameLogic.FINAL_FIELD_POSITION;
    }

    /**
     * Methode, welche beim Betreten des Ziels ausgeführt wird.
     *
     * @param curr Der aktuelle Spieler
     */
    void playerFinishEvent(Player curr) {
        onTarget.add(curr);
    }

    /**
     * Methode zum Testen mit JUnit. Liefert für die GameLogik eine Referenz auf das Kartendeck,
     * damit dieses manipuliert werden kann.
     *
     * @return Referenz auf das Kartendeck
     */
    CardDeck getCardDeck() {
        return cardDeck;
    }
}
