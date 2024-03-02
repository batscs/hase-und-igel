package logic;

import logic.data.HaseUndIgelException;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameLogicTest {

    /**
     * Hier wird ein Spiel für Testzwecke erzeugt.
     * @return Das frei erzeugte Spiel
     */
    private GameLogic provideTestGame() {
        GUIConnector gui = new FakeGUI();

        return new GameLogic(gui, "Anton", "Berta", "Cäsar");
    }

    @Test(expected = HaseUndIgelException.class)
    public void invalidTest_duplicateNames() {
        GameLogic game = new GameLogic(new FakeGUI(), "Anton", "Anton");
    }

    @Test(expected = HaseUndIgelException.class)
    public void invalidTest_emptyName() {
        GameLogic game = new GameLogic(new FakeGUI(), "", "Anton");
    }

    @Test(expected = HaseUndIgelException.class)
    public void invalidTest_notEnoughPlayers() {
        GameLogic game = new GameLogic(new FakeGUI(), "Anton");
    }

    @Test(expected = HaseUndIgelException.class)
    public void invalidTest_tooManyPlayers() {
        GameLogic game = new GameLogic(new FakeGUI(), "A", "B", "C", "D", "E", "F", "G");
    }

    @Test
    public void moveTest_AfterMoveNextPlayer() {
        GameLogic game = provideTestGame();

        Player first = game.getProtagonist();
        game.move(5);

        assertNotEquals(first, game.getProtagonist());
    }

    @Test
    public void moveTest_ChangesPlayerPosition() {
        GameLogic game = provideTestGame();

        Player p = game.getProtagonist();
        assertEquals(0, p.getPosition());

        game.move(5);

        assertEquals(5, p.getPosition());
    }

    @Test
    public void moveTest_PositionOccupied() {
        GameLogic game = provideTestGame();

        game.move(5);

        Player second = game.getProtagonist();
        game.move(5);

        // Ungültiger Spielzug, es soll kein Spielerwechsel durchgeführt werden
        assertEquals(second, game.getProtagonist());

        // Spieler soll noch auf dem Startpunkt stehen
        assertEquals(0, second.getPosition());
    }

    @Test
    public void moveTest_BackwardsNotIgelfeld() {
        GameLogic game = provideTestGame();

        Player protagonist = game.getProtagonist();
        protagonist.moveTo(5);
        game.move(3);

        assertEquals(5, protagonist.getPosition());
    }

    @Test
    public void moveTest_Salatfeld_eatsSalad() {
        GameLogic game = provideTestGame();

        Player protagonist = game.getProtagonist();
        game.move(7);

        assertTrue(protagonist.eatsSalad());
    }

    @Test
    public void moveTest_Nummerfeld_Karottenbonus() {
        GameLogic game = provideTestGame();

        //
        Player anton = game.getProtagonist();
        int startwertKarotten = anton.getCarrots();

        // Antons Zug
        // Verliert hierbei 10 Karotten
        game.move(4);
        assertEquals(startwertKarotten - 10, anton.getCarrots());

        // Bertas Zug
        Player berta = game.getProtagonist();
        game.move(9);
        ;

        // Cäsars Zug
        Player cesar = game.getProtagonist();
        game.move(10);

        // Anton bekommt nun 30 Karotten nach dem Spielerwechsel in game.move()

        assertEquals(startwertKarotten - 10 + 30, anton.getCarrots());
    }

}
