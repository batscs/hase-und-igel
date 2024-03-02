package logic;

import logic.data.KarotteChoice;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class GameLogic_KarottenfeldTest {

    /**
     * Hier wird ein Spiel für Testzwecke erzeugt.
     *
     * @return Das frei erzeugte Spiel
     */
    private GameLogic provideTestGame() {
        return new GameLogic(new FakeGUI(), "Anton", "Berta");
    }

    @Test
    public void karottenTest_nehmen() {
        GameLogic g = provideTestGame();
        Player p = g.getProtagonist();

        // Anton bewegt sich auf Karottenfeld
        g.move(2);

        // Bertas Zug
        g.move(4);

        // Anton hat nun Karottenwahl
        assertEquals(p, g.getProtagonist());
        int carrots = p.getCarrots();

        g.carrotsChoice(KarotteChoice.ADD);

        int expectedCarrots = carrots + 10;

        assertEquals(expectedCarrots, p.getCarrots());
        assertNotEquals(g.getProtagonist(), p); // Spielerwechsel nach dieser Karottenwahl
    }

    @Test
    public void karottenTest_geben() {
        GameLogic g = provideTestGame();
        Player p = g.getProtagonist();

        // Anton bewegt sich auf Karottenfeld
        g.move(2);

        // Bertas Zug
        g.move(4);

        // Anton hat nun Karottenwahl
        assertEquals(p, g.getProtagonist());
        int carrots = p.getCarrots();

        g.carrotsChoice(KarotteChoice.REMOVE);

        int expectedCarrots = carrots - 10;

        // Spieler soll nun
        assertEquals(expectedCarrots, p.getCarrots());
        assertNotEquals(g.getProtagonist(), p); // Spielerwechsel nach dieser Karottenwahl
    }

    @Test
    public void karottenTest_weiterziehen() {
        GameLogic g = provideTestGame();
        Player p = g.getProtagonist();

        // Anton bewegt sich auf Karottenfeld
        g.move(2);

        // Bertas Zug
        g.move(4);

        // Anton hat nun Karottenwahl
        assertEquals(p, g.getProtagonist());
        int carrots = p.getCarrots();

        g.carrotsChoice(KarotteChoice.CONTINUE_KAROTTENFELD);

        // Karotten unverändert
        assertEquals(carrots, p.getCarrots());

        // Anton noch am Zug
        assertEquals(g.getProtagonist(), p);

        // Anton bewegt sich auf Feld 9
        g.move(9);
        assertEquals(9, p.getPosition());

        // Regulärer Spielerwechsel
        assertNotEquals(p, g.getProtagonist());
    }

    @Test
    public void karottenTest_geben_10Karotten() {
        GameLogic g = provideTestGame();
        Player p = g.getProtagonist();

        // Anton bewegt sich auf Karottenfeld
        g.move(2);

        // Bertas Zug
        g.move(4);

        // Test: Anton hat 10 Karotten
        p.setCarrots(10);

        // Anton hat nun Karottenwahl
        assertEquals(p, g.getProtagonist());
        int carrots = p.getCarrots();

        g.carrotsChoice(KarotteChoice.REMOVE);

        int expectedCarrots = carrots - 10;
        assertEquals(expectedCarrots, p.getCarrots());

        // Spielerwechsel nach Wahl
        assertNotEquals(g.getProtagonist(), p);
    }

    @Test
    public void karottenTest_geben_notEnough() {
        GameLogic g = provideTestGame();
        Player p = g.getProtagonist();

        // Anton bewegt sich auf Karottenfeld
        g.move(2);

        // Bertas Zug
        g.move(4);

        // Test: Anton hat 9 Karotten
        p.setCarrots(9);

        // Anton hat nun Karottenwahl
        assertEquals(p, g.getProtagonist());
        int carrots = p.getCarrots();

        g.carrotsChoice(KarotteChoice.REMOVE);

        // Unverändert, da nicht genügend zum abgeben
        assertEquals(carrots, p.getCarrots());

        // Kein Spielerwechsel bei ungültiger Aktion
        assertEquals(g.getProtagonist(), p);
    }

    @Test
    public void karottenTest_security() {

        // game.carrotsChoice soll nur funktioniert wenn erwünscht
        // in diesem Fall erwünscht durch Karottenfeld

        GameLogic g = provideTestGame();
        Player p = g.getProtagonist();

        // Anton bewegt sich NICHT auf Karottenfeld
        g.move(9);

        // Bertas Zug
        g.move(4);

        // Anton ist am Zug, hat nun KEINE Karottenwahl
        assertEquals(p, g.getProtagonist());
        int carrots = p.getCarrots();

        g.carrotsChoice(KarotteChoice.REMOVE);

        // Karotten sollen unverändert bleiben, da ungültig
        assertEquals(carrots, p.getCarrots());

        // Anton ist noch regulär am Zug
        assertEquals(g.getProtagonist(), p); // Spielerwechsel nach dieser Karottenwahl
    }

    @Test
    public void karottenTest_tryToMove() {
        GameLogic g = provideTestGame();
        Player p = g.getProtagonist();

        // Anton bewegt sich auf Karottenfeld
        g.move(2);

        // Bertas Zug
        g.move(4);

        // Anton hat nun Karottenwahl
        assertEquals(p, g.getProtagonist());
        int position = p.getPosition();

        // Anton versucht sich zu bewegen, ist aber ein illegaler move!!!!
        g.move(9);

        // Antons Position soll unverändert bleiben, da er Karottenwahl machen muss!
        assertEquals(position, p.getPosition());
        assertEquals(p, g.getProtagonist());
    }
}
