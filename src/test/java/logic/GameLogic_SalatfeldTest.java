package logic;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameLogic_SalatfeldTest {

    /**
     * Hier wird ein Spiel für Testzwecke erzeugt.
     *
     * @return Das frei erzeugte Spiel
     */
    private GameLogic provideTestGame() {
        return new GameLogic(new FakeGUI(), "Anton", "Berta");
    }

    @Test
    public void salatTest_keineSalate() {
        GameLogic g = provideTestGame();
        Player p = g.getProtagonist();

        p.setSalads(0);
        int position = p.getPosition();

        // Anton versucht sich auf Salatfeld zu bewegen
        g.move(7);

        // Antons Position unverändert, da ungültiger Move. Anton noch am Zug
        assertEquals(position, p.getPosition());
        assertEquals(p, g.getProtagonist());

    }

    @Test
    public void salatTest_firstPlace() {
        GameLogic g = provideTestGame();
        Player p = g.getProtagonist();

        int salads = p.getSalads();
        int carrots = p.getCarrots() - GameBoard.calculatePrice(7);

        // Anton versucht sich auf Salatfeld zu bewegen
        g.move(7);

        // Antons Position unverändert, da ungültiger Move. Anton noch am Zug
        assertEquals(7, p.getPosition());
        assertTrue(p.eatsSalad());
        assertEquals(salads, p.getSalads()); // Erst im nächsten Zug Salat essen
        assertEquals(carrots, p.getCarrots()); // Karottenbinus auch im nächsten Zug
        assertNotEquals(p, g.getProtagonist());

        // Berta irgendein legaler Zug
        g.move(4);

        // Anton hat nun einen Salat gegessen, damit ist sein Zug zuende und Berta ist dran
        assertEquals(salads - 1, p.getSalads());
        assertEquals(carrots + 10, p.getCarrots()); // 10 Bonuskarotten für 1. Platz
        assertNotEquals(p, g.getProtagonist());

    }

    @Test
    public void salatTest_secondPlace() {
        GameLogic g = provideTestGame();
        Player p = g.getProtagonist();

        int salads = p.getSalads();
        int carrots = p.getCarrots() - GameBoard.calculatePrice(7);

        // Anton versucht sich auf Salatfeld zu bewegen
        g.move(7);

        // Antons Position unverändert, da ungültiger Move. Anton noch am Zug
        assertEquals(7, p.getPosition());
        assertTrue(p.eatsSalad());
        assertEquals(salads, p.getSalads()); // Erst im nächsten Zug Salat essen
        assertEquals(carrots, p.getCarrots()); // Karottenbinus auch im nächsten Zug
        assertNotEquals(p, g.getProtagonist());

        // Berta irgendein legaler Zug
        g.move(9);

        // Anton hat nun einen Salat gegessen, damit ist sein Zug zuende und Berta ist dran
        assertEquals(salads - 1, p.getSalads());
        assertEquals(carrots + 20, p.getCarrots()); // 20 Bonuskarotten für 2. Platz
        assertNotEquals(p, g.getProtagonist());

    }

}
