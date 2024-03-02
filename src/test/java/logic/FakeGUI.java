package logic;

import gui.data.Error;

import java.util.List;
import java.util.function.Consumer;

/**
 * Implementation des GUIConnectors zum Testen in JUnit.
 * Hier wird nicht erwartet, dass irgendetwas passiert, da kein GUI benötigt wird für die Tests.
 *
 * @author github.com/batscs
 */
public class FakeGUI implements GUIConnector {

    @Override
    public void initializePlayer(int playerIdx, Player player) {

    }

    @Override
    public void alignPlayer(int playerIdx, int position) {

    }

    @Override
    public void movePlayer(int playerIdx, int start, int destination, Runnable afterMoveEvent) {
        afterMoveEvent.run();
    }

    @Override
    public void updateStats(int playerIdx, String username, int carrots, int salads) {

    }

    @Override
    public void setOnTarget(List<Integer> onTarget) {

    }

    @Override
    public void changeFieldBackgroundColor(int index, boolean reachable) {

    }

    @Override
    public void highlightPlayer(int playerIdx) {

    }

    @Override
    public void unhighlightPlayer(int playerIdx) {

    }

    @Override
    public void gameStop() {

    }

    @Override
    public void gameStart(int amountPlayers) {

    }

    @Override
    public void gameWin(int winnerIdx) {

    }

    @Override
    public void alertError(Error error) {

    }

    @Override
    public void alertInfo(Token header, Token description, Object... args) {

    }

    @Override
    public void alertEvent(Token msg, Player player, Runnable event) {
        event.run();
    }

    @Override
    public void alertConfirm(Token message, Consumer<Boolean> func, Object... args) {

    }

    @Override
    public void alertMultipleChoice(Token empty, Consumer<Integer> integerConsumer, Token... options) {

    }
}
