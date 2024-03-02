package logic;

import logic.data.CardEvent;
import logic.data.GamePosition;
import logic.data.HaseUndIgelException;
import logic.data.KarotteChoice;
import logic.fields.Field;
import util.log.Log;
import util.log.LogLevel;
import util.log.LogModule;

import java.util.LinkedList;
import java.util.List;

/**
 * Klasse zur Repräsentation eines laufenden Hase-Und-Igel-Spiels.
 *
 * @author github.com/batscs
 */
public class GameLogic {

    /**
     * Konstante für die Anzahl an Felder und gleichzeitig Index des finalen Feldes
     */
    public static int FINAL_FIELD_POSITION = 64;

    /**
     * Konstante für die Bedingung das Ziel zu betreten, entsprechendes Maximum an Salaten.
     */
    public static int FINISH_MAX_SALADS = 0;

    /**
     * Konstante für die Bedingung das Ziel zu betreten, entsprechendes Maximum an Karotten.
     */
    public static int FINISH_MAX_CARROTS = 10;

    /**
     * Attribut für die teilnehmenden Spieler.
     */
    private final Player[] players;

    /**
     * Attribut aller Felder des Spielbretts.
     */
    private final Field[] fields;

    /**
     * Anzahl der teilnehmenden Spieler
     */
    private final int participants;

    /**
     * Attribut ob der nächste Spielerwechsel blockiert wird.
     * Wenn z.B. ein Spieler eine "Ziehe noch einmal!" Hasenkarte zieht.
     */
    private boolean turnBlocked;

    /**
     * Attribut für die Logik des Spielfeldes.
     */
    private final GameBoard board;

    /**
     * Attribut für die Benutzeroberfläche, um Änderungen im Front-End zeigen zu können.
     */
    private final GUIConnector gui;

    /**
     * Attribut für die Anzahl an Karotten zu Beginn eines Spiels, hängt von der Anzahl der
     * teilnehmenden Spieler ab. Ab 5 Spieler beträgt diese 98, ansonsten sind es 68 Karotten.
     */
    private final int PRESET_CARROTS;

    /**
     * Representation des aktuellen Spielers, gleichgültig des Index der teilnehmenden Spieler
     */
    private int protagonist;

    /**
     * Attribut, ob das Spiel vollendet ist.
     */
    private boolean finished = false;

    /**
     * Attribut, ob der aktuelle Spieler eine Karottenauswahl machen muss.
     */
    private boolean choosingCarrots = false;

    /**
     * Initialisiert alle Spieler entsprechend dem übergebenen Namen und einheitlichen Standard-
     * werten für alle.
     *
     * @param gui Referenz auf die Benutzeroberfläche
     * @param names Teilnehmende Spielernamen
     */
    public GameLogic(GUIConnector gui, String... names) {

        int amountPlayers = names.length;

        this.players = new Player[amountPlayers];

        for (int i = 0; i < amountPlayers; i++) {
            players[i] = new Player(names[i]);
        }

        this.gui = gui;

        this.board = new GameBoard(players);
        this.fields = board.getFields();

        this.protagonist = 0;
        this.participants = players.length;

        this.turnBlocked = false;

        final boolean PRESET_SUSPENDED = false;
        final boolean PRESET_EATS_SALAD = false;
        final int PRESET_POSITION = 0;
        final int PRESET_SALADS = 3;
        PRESET_CARROTS = participants < 5 ? 68 : 98;

        gui.gameStart(amountPlayers);

        // Jeden Spieler auf "Werkeinstellungen" zurücksetzen
        for (Player player : players) {
            gui.unhighlightPlayer(getPlayerID(player));
            player.setSuspended(PRESET_SUSPENDED);
            player.setEatsSalad(PRESET_EATS_SALAD);
            player.moveTo(PRESET_POSITION);
            player.setCarrots(PRESET_CARROTS);
            player.setSalads(PRESET_SALADS);
            player.moveTo(PRESET_POSITION);
            gui.initializePlayer(getPlayerID(player), player);
            gui.alignPlayer(getPlayerID(player), player.getPosition());
        }

        // Front End Anpassungen
        Player curr = getProtagonist();
        gui.updateStats(getPlayerID(getProtagonist()), curr.getName(), curr.getCarrots(), curr.getSalads());
        gui.highlightPlayer(getPlayerID(getProtagonist()));

        redrawReachableFields();

        selfValidate();
    }

    /**
     * Konstruiert ein Spiel mitten im Geschehen mithilfe einer GameData config,
     * wird verwendet zum Laden eines vordefinierten Spielstands.
     *
     * @param gui    Die Benutzeroberfläche
     * @param config Die eingelesene Config
     */
    public GameLogic(GUIConnector gui, GameData config) {
        this.protagonist = config.getProtagonist();
        this.participants = config.getParticipants();
        this.players = config.getPlayers();

        int amountPlayers = players.length;
        PRESET_CARROTS = participants < 5 ? 68 : 98;

        this.board = new GameBoard(players);
        this.fields = board.getFields();

        List<Integer> configOnTarget = config.getOnTarget();
        gui.setOnTarget(configOnTarget);

        /*
         * Die Config speichert onTarget als int[], hier ist es aber ein Player[]
         * daher muss der Player Arary hier so erzeugt werden, da alle Informationen dafür bekannt sind.
         */
        for (int targetIdx : configOnTarget) {
            Player targetPlayer = players[targetIdx];

            board.playerFinishEvent(targetPlayer);
        }

        gui.gameStart(amountPlayers);

        for (int i = 0; i < amountPlayers; i++) {
            gui.initializePlayer(i, players[i]);
            gui.unhighlightPlayer(getPlayerID(players[i]));
            gui.alignPlayer(getPlayerID(players[i]), players[i].getPosition());
        }

        this.gui = gui;

        selfValidate();
        calculateLoadedPosition();
    }

    /**
     * Methode zum Initialisieren eines Spielstandes um das laufende Spiel zu beginnen.
     */
    private void calculateLoadedPosition() {

        redraw();

        checkForIdleInteraction();
        checkForPlayerLose();

        if (!checkForGameEnd()) {
            gui.highlightPlayer(getPlayerID(getProtagonist()));
        }

    }

    /**
     * Liefert das Spielbrett als GameConfig Objekt mit allen Informationen.
     *
     * @return GameConfig Objekt mit allen Spielbrett Informationen
     */
    public GameData getConfig() {
        return new GameData(players, protagonist, transformOnTargetToIndices());
    }

    /**
     * Methode zum Validieren, ob diese Instanz den Spielregeln entspricht.
     *
     * @throws HaseUndIgelException falls ungültiger Spielstand
     */
    public void selfValidate() {
        boolean valid = GameData.validate(getConfig()) != null;

        if (!valid) {
            throw new HaseUndIgelException("GameLogic Instance is invalid");
        }
    }

    /**
     * Liefert eine Referenz auf das GameBoard.
     *
     * @return Referenz auf das GameBoard
     */
    GameBoard getBoard() {
        return board;
    }

    /**
     * Blockiert das Wechseln auf den nächsten Spieler
     */
    public void blockNextPlayerTurn() {
        turnBlocked = true;
    }

    /**
     * Versucht den aktuellen Spieler ins Ziel zu bewegen, falls dieses Möglich ist.
     */
    public void attemptToFinish() {
        Player curr = getProtagonist();

        if (!board.canReachFinish(getProtagonist())) {

            int travelPrice = GameBoard.calculatePrice(FINAL_FIELD_POSITION - curr.getPosition());

            Token msg = Token.GAME_REACH_FINISH_UNSUCCESSFUL;

            int playersOnTarget = board.getOnTarget().size();
            int carrotsMultiplier = playersOnTarget + 1;
            int maximumCarrots = (FINISH_MAX_CARROTS * carrotsMultiplier) + travelPrice;

            if (curr.getCarrots() < travelPrice) {
                msg = Token.GAME_REACH_FINISH_UNSUCCESSFUL_CANT_AFFORD;
            } else if (curr.getSalads() > FINISH_MAX_SALADS && curr.getCarrots() > maximumCarrots) {
                msg = Token.GAME_REACH_FINISH_UNSUCCESSFUL_ALL;
            } else if (curr.getSalads() > FINISH_MAX_SALADS) {
                msg = Token.GAME_REACH_FINISH_UNSUCCESSFUL_SALADS;
            } else if (curr.getCarrots() > maximumCarrots) {
                msg = Token.GAME_REACH_FINISH_UNSUCCESSFUL_CARROTS;
            }

            // alertEvent hier, damit der Token mit {username} ordentlich übersetzt wird.
            gui.alertEvent(msg, getProtagonist(), () -> {});
            return;
        }

        int finishPosition = fields.length;
        int price = GameBoard.calculatePrice(finishPosition - curr.getPosition());

        curr.setCarrots(curr.getCarrots() - price);
        board.playerFinishEvent(curr);

        gui.movePlayer(getPlayerID(curr), curr.getPosition(), fields.length - 1, () -> {
            gui.alertEvent(Token.GAME_REACH_FINISH_SUCCESSFUL, curr, () -> {
                curr.moveTo(FINAL_FIELD_POSITION);
                if (!checkForGameEnd()) {
                    afterMove();
                }
            });
        });

    }

    public void onPlayerFinish(Player curr) {
        board.playerFinishEvent(curr);
    }

    /**
     * Überprüft, ob das Spiel abgeschlossen wurde, und alle Spieler sich im Zielfeld befinden.
     *
     * @return True, falls das Spiel von allen Teilnehmern vollendet wurde und ein Sieger feststeht.
     */
    public boolean checkForGameEnd() {
        List<Player> onTarget = board.getOnTarget();

        int playersNotOnFinish = players.length - onTarget.size();
        if (playersNotOnFinish > 1) {
            finished = false;
        } else {
            Player winner = onTarget.get(0);
            protagonist = getPlayerID(winner);

            gui.alertInfo(Token.GAME_PLAYER_HAS_WON_TITLE, Token.GAME_PLAYER_HAS_WON_CONTENT,
                    winner.getName());
            gui.gameWin(protagonist);
            gui.updateStats(protagonist, winner.getName(), winner.getCarrots(), winner.getSalads());
            finished = true;
        }

        return finished;
    }

    /**
     * Ermittelt, ob das Spiel vollendet ist.
     *
     * @return True, falls vollendet.
     */
    public boolean isGameFinished() {
        return finished;
    }

    /**
     * Bewegt den aktuellen Spieler auf eine Position, falls er diese erreichen kann.
     * Wenn der Spieler sich dahin bewegen kann, interagiert das Feld mit ihm.
     *
     * @param position Die Position des Feldes
     */
    public void move(int position) {

        if (choosingCarrots) {
            Log.write(LogLevel.WARN, LogModule.GAME, "User tried to move while choosing Carrots");
            return;
        }

        if (position < 0 || position > FINAL_FIELD_POSITION) {
            Log.write(LogLevel.WARN, LogModule.GAME, "User somehow tried to move to illegal field, position: " + position);
            return;
        }

        if (isGameFinished()) {
            Log.write(LogLevel.WARN, LogModule.GAME,
                    "User somehow tried to move after game has finished");
            return;
        }

        if (position == GameLogic.FINAL_FIELD_POSITION) {
            attemptToFinish();
            return;
        }

        Player curr = getProtagonist();

        int distance = position - curr.getPosition();
        int price = GameBoard.calculatePrice(distance);

        if (board.isReachable(getProtagonist(), position)) {
            // Logik für das Durchführen des Spielzuges
            curr.setCarrots(curr.getCarrots() - price);
            curr.setEatsSalad(false);

            // pre-move Logik
            int oldPosition = curr.getPosition();
            fields[position].interactBeforeMove(curr);

            // on-move Logik
            curr.moveTo(position);

            // after-move Logik
            gui.movePlayer(getPlayerID(curr), oldPosition, position, this::afterMove);
            redrawStats();


            Log.write(LogLevel.TRACE, LogModule.GAME, String.format("Player '%s' moved to position %d", curr.getName(), position));
            Log.write(LogLevel.DEBUG, LogModule.GAME,
                    "Player moved to fieldtype " + fields[position].getType());

            redrawReachableFields();

        }

    }

    /**
     * Methode benötigt, um das Event des Feldes nach dem Zug erst auch nach der Vollendung der Animation
     * in der GUI Klasse auszuführen.
     */
    public void afterMove() {
        Player curr = getProtagonist();
        int index = curr.getPosition();

        redraw();

        if (index >= 0 && index < fields.length) {
            fields[index].interactAfterMove(getProtagonist(), this, gui);
        }
    }

    /**
     * Falls nicht geblockt, wird hier der Spielzug des aktuellen Spielers beendet, und der nächste Spieler
     * wird als Protagonist festgesetzt.
     * <p>
     * Bevor der neue aktuelle Spieler einen Zug machen kann, wird überprüft, ob das Feld, auf dem
     * er steht, mit ihm eine idle Interaktion durchführen kann.
     */
    public void nextPlayerTurn() {

        if (isNextTurnBlocked()) {
            Log.write(LogLevel.DEBUG, LogModule.GAME, "Switching player was blocked this turn");
            turnBlocked = false;
            checkForIdleInteraction(); // Könnte eventuell Katastrophal sein
            return;
        }

        // Vorherigen Protagonisten Effekt entfernen
        gui.unhighlightPlayer(getPlayerID(getProtagonist()));

        do {
            // Protagonist darf nicht ein Spieler sein, der bereits im Ziel ist
            protagonist = (protagonist + 1) % participants;
        } while (board.hasReachedFinish(getProtagonist()) && !isGameFinished());

        Log.write(LogLevel.INFO, LogModule.GAME,
                String.format("Switching currentPlayer to %s", getProtagonist().getName()));

        gui.highlightPlayer(getPlayerID(getProtagonist()));

        // Logik für nach dem Spielzug
        redrawStats();
        redrawReachableFields();

        // Überprüfung falls nächster Spieler eine Interaktion auf dem stehenden Feld hat (z.B. Salat essen oder Karotten nehmen/geben)
        checkForIdleInteraction();

        checkForPlayerLose();

    }

    /**
     * Methode zum Überprüfen ob ein Spieler
     */
    private void checkForPlayerLose() {
        Player curr = getProtagonist();

        if (board.hasReachedFinish(curr)) {
            return;
        }

        if (board.getReachableFields(curr) > 0) {
            return;
        }

        if (curr.isSuspended()) {
            return;
        }

        gui.alertEvent(Token.GAME_PLAYER_FORCE_RESET, curr, () -> {
            gui.movePlayer(getPlayerID(curr), curr.getPosition(), 0, () -> {
                Log.write(LogLevel.INFO, LogModule.GAME,
                        "currentPlayer has been reset for having no legal moves");
                curr.setCarrots(PRESET_CARROTS);
                curr.moveTo(0);
                redrawReachableFields();
                redrawStats();
                nextPlayerTurn();;
            });
        });

    }

    /**
     * Das Spielende wird erzwungen, z.B. durch drücken des Neues Spiel Buttons.
     */
    public void forceGameEnd() {
        gui.gameStop();
    }

    /**
     * Methode zur Überprüfung, ob das Feld auf dem der aktuelle Spieler steht eine Idle Interaktion durchführen kann.
     */
    private void checkForIdleInteraction() {
        Player curr = players[protagonist];
        int index = curr.getPosition();

        // Index ist kleiner als 0, wenn Spieler sich noch auf dem Startpunkt außerhalb des eigentlich Feldes befindet
        // Daher gibt es keine großartige Logik die berechnet werden muss, und es wird einfach returned
        if (index < 0 || index >= fields.length) {
            return;
        }

        fields[index].interactOnIdle(curr, this, gui);

    }

    /**
     * Methode für die Karottenfelder, verändert die Anzahl an Karotten des Spielers
     *
     * @param amount Die Veränderung der Karotten des Spielers
     */
    private void giveCarrots(int amount) {
        Player curr = players[protagonist];

        if (curr.getCarrots() + amount < 0) {
            gui.alertEvent(Token.FIELD_CARROTS_CANT_GIVE, curr, this::checkForIdleInteraction);
        } else {
            curr.setCarrots(curr.getCarrots() + amount);
            nextPlayerTurn();
        }
    }

    /**
     * Methode zur Verarbeitung der Entscheidung zu einer Karottenauswahl.
     *
     * @param choice die Wahl des aktuellen Spielers
     */
    public void carrotsChoice(KarotteChoice choice) {

        if (choosingCarrots) {
            switch (choice) {
                case ADD -> giveCarrots(10);
                case REMOVE -> giveCarrots(-10);
                case CONTINUE_KAROTTENFELD -> redraw();
                case CONTINUE_HASENFELD -> nextPlayerTurn();
            }
            choosingCarrots = false;
        }

    }

    /**
     * Methode, dass der aktuelle Spieler eine Karottenauswahl tätigen muss.
     */
    public void enableCarrotsChoosing() {
        choosingCarrots = true;
    }

    /**
     * Methode, welche die Statistiken auf der Benutzeroberfläche aktualisiert.
     */
    private void redrawStats() {
        Player curr = getProtagonist();
        gui.updateStats(getPlayerID(getProtagonist()), curr.getName(), curr.getCarrots(), curr.getSalads());
    }

    /**
     * Methode, welche die Felder auf der Benutzeroberfläche anhand ihrer Erreichbarkeit
     * aktualisiert.
     */
    private void redrawReachableFields() {
        for (int i = 0; i < fields.length; i++) {
            gui.changeFieldBackgroundColor(i, board.isReachable(getProtagonist(), i));
        }
    }

    /**
     * Methode, welche die Statistiken und Felder aktualisiert.
     */
    public void redraw() {
        redrawStats();
        redrawReachableFields();
    }

    /**
     * Methode, welche den aktuellen Spieler / Protagonisten ermittelt.
     *
     * @return der aktuelle Spieler / Protagonist
     */
    Player getProtagonist() {
        return players[protagonist];
    }

    /**
     * Ermittelt den Index eines Spielers. Wird oft als Identifikation eines Spielers genutzt bei
     * der Kommunikation mit der Benutzeroberfläche.
     *
     * @param player Der Spieler
     * @return Index / ID des Spielers
     */
    public int getPlayerID(Player player) {
        int id = -1;

        for (int i = 0; i < players.length; i++) {
            if (player == players[i]) {
                id = i;
            }
        }

        return id;
    }

    /**
     * Methode zum Ermitteln einer Liste der teilnehmenden Spieler-Indize, sortiert nach ihrer
     * Rangfolge im Zielfeld.
     *
     * @return Die sortierte Liste
     */
    private List<Integer> transformOnTargetToIndices() {
        List<Player> onTarget = board.getOnTarget();
        List<Integer> result = new LinkedList<>();

        for (int i = 0; i < onTarget.size(); i++) {
            result.add(getPlayerID(onTarget.get(i)));
        }

        return result;
    }

    /**
     * Methode zum Ermitteln der Platzierung in der Rangfolge des aktuellen Spielers /
     * Protagonisten. Erster Platz gibt 0 zurück, da 0-indiziert.
     *
     * @return Platzierung in der Rangfolge
     */
    public int getPlaceOfProtagonist() {
        return board.getPlaceOfPlayer(getProtagonist());
    }

    /**
     * Methode zum Ermitteln ob der nächste Spielerwechsel blockiert ist.
     *
     * @return True, falls der nächste Spielerwechsel blockiert ist.
     */
    private boolean isNextTurnBlocked() {
        return turnBlocked;
    }

    /**
     * Methode zum Ermitteln einer genauen Position für eine bestimmte Situation.
     *
     * @param targetPosition zu suchende Zielposition
     * @return gefundene Position, falls nicht vorhanden dann Ursprungsposition.
     */
    public int getAvailablePosition(GamePosition targetPosition) {
        return board.getAvailablePosition(targetPosition, getProtagonist());
    }

    /**
     * Methode zum Testen mit JUnit.
     * Erzwingt eine bestimmte Karte auf die erste Position des Kartendecks.
     *
     * @param card Die eingesetzte Karte
     */
    void forceCard(CardEvent card) {
        this.board.getCardDeck().forcePush(card);
    }
}
