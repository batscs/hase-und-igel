package logic;

import org.junit.Test;

import static org.junit.Assert.*;

public class RequiredGameLogicTest {

    /**
     * Hier wird ein Spiel für Testzwecke erzeugt.
     * @return Das frei erzeugte Spiel
     */
    private GameLogic provideTestGame() {
        GUIConnector gui = new FakeGUI();

        return new GameLogic(gui, "Anton", "Berta", "Cäsar");
    }

    /**
     * Darf der aktuelle Spieler ein bestimmtes Feld betreten?
     * Ein vorausliegendes Feld (mit größerem Index als dem Feldindex des Spielers).
     */
    @Test
    public void isReachableTest_OneForward() {
        GameLogic game = provideTestGame();
        GameBoard board = game.getBoard();

        assertTrue(board.isReachable(game.getProtagonist(), 1));
    }

    /**
     * Darf der aktuelle Spieler ein bestimmtes Feld betreten?
     * Ein Feld, für das die Karottenanzahl nicht ausreicht
     */
    @Test
    public void isReachableTest_NotEnoughCarrots() {
        GameLogic game = provideTestGame();
        GameBoard board = game.getBoard();
        Player protagonist = game.getProtagonist();

        // Mit Karotten ist erreichbar
        assertTrue(board.isReachable(protagonist, 1));

        // Ohne Karotten ist nicht erreichbar
        protagonist.setCarrots(0);
        assertFalse(board.isReachable(protagonist, 1));
    }

    /**
     * Darf der aktuelle Spieler ein bestimmtes Feld betreten?
     * Ein vorausliegendes Igelfeld.
     */
    @Test
    public void isReachableTest_Igel_Ahead() {
        GameLogic game = provideTestGame();
        GameBoard board = game.getBoard();
        Player protagonist = game.getProtagonist();

        protagonist.setCarrots(9000); // Karottenpreis sollen hier nicht der entscheidene Faktor sein

        assertFalse(board.isReachable(protagonist, 8));
    }

    /**
     * Darf der aktuelle Spieler ein bestimmtes Feld betreten?
     * Ein zurückliegendes Igelfeld.
     */
    @Test
    public void isReachableTest_Igel_Behind() {
        GameLogic game = provideTestGame();
        GameBoard board = game.getBoard();
        Player protagonist = game.getProtagonist();

        protagonist.moveTo(9);

        assertTrue(board.isReachable(protagonist, 8));
    }

    /**
     * Darf der aktuelle Spieler ein bestimmtes Feld betreten?
     * Ein Salatfeld mit Salat im Besitz des Spielers.
     */
    @Test
    public void isReachableTest_Salad_HasSalad() {
        GameLogic game = provideTestGame();
        GameBoard board = game.getBoard();
        Player protagonist = game.getProtagonist();

        assertTrue(board.isReachable(protagonist, 7));
    }

    /**
     * Darf der aktuelle Spieler ein bestimmtes Feld betreten?
     * Ein Salatfeld, ohne dass der Spieler noch Salate besitzt.
     */
    @Test
    public void isReachableTest_Salad_NoSalad() {
        GameLogic game = provideTestGame();
        GameBoard board = game.getBoard();
        Player protagonist = game.getProtagonist();

        protagonist.setSalads(0);
        assertFalse(board.isReachable(protagonist, 7));
    }

    /**
     * Wird der korrekte Feldindex ermittelt?
     * vom nächsten freien Karottenfeld
     * wenn das Nächste frei ist
     */
    @Test
    public void getIndex_Carrot_NextFree() {
        GameLogic game = provideTestGame();
        GameBoard board = game.getBoard();
        Player protagonist = game.getProtagonist();

        assertEquals(2, board.getNextCarrotFieldPositionForProtagonist(protagonist));
    }

    /**
     * Wird der korrekte Feldindex ermittelt?
     * vom nächsten freien Karottenfeld
     * wenn das Nächste belegt ist
     */
    @Test
    public void getIndex_Carrot_NextOccupied() {
        GameLogic game = provideTestGame();
        GameBoard board = game.getBoard();

        game.getProtagonist().moveTo(2);
        game.nextPlayerTurn();

        // 0 Expected, da kein Feld gefunden wurde, daher Karte ist belanglos und nichts passiert
        assertEquals(5, board.getNextCarrotFieldPositionForProtagonist(game.getProtagonist()));
    }


    /**
     * Wird der korrekte Feldindex ermittelt?
     * beim Zurückfallen um eine Position
     * Der vorige Spieler steht auf Feldindex 1.
     */
    @Test
    public void getIndex_Fallback_Position_PreviousPlayerIndex1() {
        GameLogic game = provideTestGame();
        GameBoard board = game.getBoard();

        game.getProtagonist().moveTo(1);

        game.nextPlayerTurn();

        game.getProtagonist().moveTo(4);

        assertEquals(0, board.getFreeBackwardPositionForProtagonist(game.getProtagonist()));
    }

    /**
     * Wird der korrekte Feldindex ermittelt?
     * beim Zurückfallen um eine Position
     * Der vorige Spieler steht auf Feldindex 2.
     */
    @Test
    public void getIndex_Fallback_Position_PreviousPlayerIndex2() {
        GameLogic game = provideTestGame();
        GameBoard board = game.getBoard();

        game.getProtagonist().moveTo(2);

        game.nextPlayerTurn();

        game.getProtagonist().moveTo(4);

        assertEquals(1, board.getFreeBackwardPositionForProtagonist(game.getProtagonist()));
    }

    /**
     * Wird der korrekte Feldindex ermittelt?
     * beim Vorrücken um eine Position
     * Der nächste Spieler steht auf Feldindex 61.
     */
    @Test
    public void getIndex_ForwardPosition_NextPlayerIndexNearlyLast() {
        GameLogic game = provideTestGame();
        GameBoard board = game.getBoard();

        game.getProtagonist().moveTo(61);
        game.nextPlayerTurn();

        // Falls man Salate hat
        assertEquals(62, board.getFreeForwardPositionForProtagonist(game.getProtagonist()));

        // Falls man keine Salate hat, kann man nicht auf Salat Feld (62)
        game.getProtagonist().setSalads(0);
        assertEquals(63, board.getFreeForwardPositionForProtagonist(game.getProtagonist()));
    }

    /**
     * Wird der korrekte Feldindex ermittelt?
     * beim Vorrücken um eine Position
     * Der nächste Spieler steht auf Feldindex 63.
     */
    @Test
    public void getIndex_ForwardPosition_NextPlayerIndexLast() {
        GameLogic game = provideTestGame();
        GameBoard board = game.getBoard();

        game.getProtagonist().moveTo(63);

        game.nextPlayerTurn();

        // Zu viele Karotten & Salate
        assertNotEquals(64, board.getFreeForwardPositionForProtagonist(game.getProtagonist()));

        // Zu viele Salate noch
        game.getProtagonist().setCarrots(10);
        assertNotEquals(64, board.getFreeForwardPositionForProtagonist(game.getProtagonist()));

        // Bedingungen erfüllt
        game.getProtagonist().setSalads(0);
        assertEquals(64, board.getFreeForwardPositionForProtagonist(game.getProtagonist()));
    }

    /**
     * Darf der aktuelle Spieler ins Ziel?
     * Als Erster und darf / darf nicht wegen zu viel Karotten / darf nicht wegen Salatbesitzes.
     */
    @Test
    public void canReachFinish_AsFirst() {
        GameLogic game = provideTestGame();
        GameBoard board = game.getBoard();
        Player curr = game.getProtagonist();

        // Strecke zu weit, kann nicht bezahlt werden
        assertFalse(board.canReachFinish(curr));

        // Zu viele Karotten und Salate
        curr.moveTo(60);
        assertFalse(board.canReachFinish(curr));

        // Noch immer zu viele Salate
        curr.setCarrots(10);
        assertFalse(board.canReachFinish(curr));

        // Keine Salate mehr, alle Bedingungen erfüllt
        curr.setSalads(0);
        assertTrue(board.canReachFinish(curr));
    }

    /**
     * Darf der aktuelle Spieler ins Ziel?
     * Obwohl zu viele Karotten, da der Weg dahin auch Karotten kostet und danach die erforderliche Anazhl erreicht wird
     */
    @Test
    public void canReachFinish_AsFirst_ConsiderPriceForMove() {
        GameLogic game = provideTestGame();
        GameBoard board = game.getBoard();

        Player curr = game.getProtagonist();

        // Bedingungen wurden genauer bereits in vorherigen Tests getestet, daher überflüssig
        curr.moveTo(58);
        curr.setCarrots(25); // Weg von 58 zu 64 kostet 21 Karotten
        curr.setSalads(0);

        assertTrue(board.canReachFinish(curr));
    }

    /**
     * Darf der aktuelle Spieler ins Ziel?
     * Als Zweiter und darf / darf nicht wegen zu viel Karotten / darf nicht wegen Salatbesitzes.
     */
    @Test
    public void canReachFinish_AsSecond() {
        GameLogic game = provideTestGame();
        GameBoard board = game.getBoard();

        // Erter Spieler wie im vorherigen Test ins Ziel
        game.getProtagonist().setCarrots(10);
        game.getProtagonist().setSalads(0);
        game.getProtagonist().moveTo(60);
        assertTrue(board.canReachFinish(game.getProtagonist()));
        game.attemptToFinish();

        game.nextPlayerTurn(); // Normalerweise wird nextPlayerTurn über einen Button ausgeführt

        // Zu viele Karotten + Salate
        game.getProtagonist().moveTo(60);
        assertFalse(board.canReachFinish(game.getProtagonist()));

        // Zu viele Salate
        game.getProtagonist().setCarrots(10);
        assertFalse(board.canReachFinish(game.getProtagonist()));

        // Bedingungen erfüllt!
        game.getProtagonist().setSalads(0);
        assertTrue(board.canReachFinish(game.getProtagonist()));
    }

}
