package logic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameBoardTest {

    /**
     * Hier wird ein Spiel für Testzwecke erzeugt.
     * @return Das frei erzeugte Spiel
     */
    private GameLogic provideTestGame() {
        GUIConnector gui = new FakeGUI();

        return new GameLogic(gui, "Anton", "Berta", "Cäsar");
    }

    @Test
    public void getPlayerOnPlaceTest() {
        GameLogic game = provideTestGame();
        GameBoard board = game.getBoard();

        Player one = game.getProtagonist();
        game.getProtagonist().moveTo(5); // Anton
        game.nextPlayerTurn();

        Player two = game.getProtagonist();
        game.getProtagonist().moveTo(6); // Berta
        game.nextPlayerTurn();

        Player three = game.getProtagonist();
        game.getProtagonist().moveTo(3); // Cäsar

        assertEquals(two.getName(), board.getPlayerOnPlace(0).getName());
        assertEquals(one.getName(), board.getPlayerOnPlace(1).getName());
        assertEquals(three.getName(), board.getPlayerOnPlace(2).getName());
    }

    @Test
    public void getPlaceOfPlayerTest() {
        GameLogic game = provideTestGame();
        GameBoard board = game.getBoard();

        Player one = game.getProtagonist();
        game.getProtagonist().moveTo(5); // Anton
        game.nextPlayerTurn();

        Player two = game.getProtagonist();
        game.getProtagonist().moveTo(6); // Berta
        game.nextPlayerTurn();

        Player three = game.getProtagonist();
        game.getProtagonist().moveTo(3); // Cäsar

        assertEquals(1, board.getPlaceOfPlayer(one));
        assertEquals(0, board.getPlaceOfPlayer(two));
        assertEquals(2, board.getPlaceOfPlayer(three));
    }

    @Test
    public void calculatePriceTest() {

        assertEquals(1, GameBoard.calculatePrice(1));
        assertEquals(3, GameBoard.calculatePrice(2));
        assertEquals(6, GameBoard.calculatePrice(3));
        assertEquals(15, GameBoard.calculatePrice(5));

        assertEquals(0, GameBoard.calculatePrice(0));
        assertEquals(0, GameBoard.calculatePrice(-5));
    }

}
