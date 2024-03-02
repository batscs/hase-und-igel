package logic;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class GameLogic_NummerfeldTest {

    public static final String PATH_DIRECTORY =
            String.join(File.separator, "src", "test", "files", "logic", "nummerfeld");

    private String toPath(String file) {
        return PATH_DIRECTORY + File.separator + file;
    }

    @Test
    public void nummerTest_drei() {
        GameLogic g = new GameLogic(new FakeGUI(), "Anton", "Berta", "Cäsar");

        Player p = g.getProtagonist();
        int carrots = p.getCarrots();

        // Anton auf Nummerfeld mit Zahl 3
        g.move(4);

        // Anton sollen erstmal nur die Karotten für den Weg berechnet werden
        int expectedCarrots = carrots - GameBoard.calculatePrice(4);
        assertEquals(expectedCarrots, p.getCarrots());
        carrots = expectedCarrots;

        // Berta auf legales Feld
        g.move(9);

        // Cäsar auf legales Feld
        g.move(10);

        // Anton am Zug
        expectedCarrots = carrots + 3 * 10;
        assertEquals(expectedCarrots, p.getCarrots());

        // Anton kann nun regulär einen Zug machen
        g.move(12);
        assertEquals(12, p.getPosition());
        assertNotEquals(p, g.getProtagonist()); // Spielerwechsel Kontrolle
    }

    @Test
    public void nummerTest_vier() {
        String path = toPath("test_nummerfeld_vier.json");
        File file = new File(path);
        GameData data = GameData.validate(file);

        assertNotNull(data);

        GameLogic g = new GameLogic(new FakeGUI(), data);
        // Anton hat im Spielstand zu beginn 40 Karotten
        int carrots = 40;
        Player p = g.getProtagonist();
        assertEquals(carrots + 4 * 10, p.getCarrots());
        assertEquals(p, g.getProtagonist());
    }

    @Test
    public void nummerTest_vier_aberFalscherPlatz() {
        String path = toPath("test_nummerfeld_vier_aber_falscher_platz.json");
        File file = new File(path);
        GameData data = GameData.validate(file);

        assertNotNull(data);

        GameLogic g = new GameLogic(new FakeGUI(), data);
        // Anton hat im Spielstand zu beginn 40 Karotten
        int carrots = 40;
        Player p = g.getProtagonist();

        // Karottenanzahl unverändert
        assertEquals(carrots, p.getCarrots());
        assertEquals(p, g.getProtagonist());
    }

    @Test
    public void nummerTest_vier_aberDreiSpieler() {
        String path = toPath("test_nummerfeld_vier_aber_drei_spieler.json");
        File file = new File(path);
        GameData data = GameData.validate(file);

        assertNotNull(data);

        GameLogic g = new GameLogic(new FakeGUI(), data);
        // Anton hat im Spielstand zu beginn 40 Karotten
        int carrots = 40;
        Player p = g.getProtagonist();
        assertEquals(carrots, p.getCarrots());
        assertEquals(p, g.getProtagonist());
    }

    @Test
    public void nummerTest_zwei() {
        String path = toPath("test_nummerfeld_zwei.json");
        File file = new File(path);
        GameData data = GameData.validate(file);

        assertNotNull(data);

        GameLogic g = new GameLogic(new FakeGUI(), data);

        // hat im Spielstand zu beginn 0 Karotten
        int carrots = 0;
        Player p = g.getProtagonist();
        assertEquals(carrots + 2 * 10, p.getCarrots());
        assertEquals(p, g.getProtagonist());
    }

    @Test
    public void nummerTest_flagge() {
        String path = toPath("test_nummerfeld_flagge.json");
        File file = new File(path);
        GameData data = GameData.validate(file);

        assertNotNull(data);

        GameLogic g = new GameLogic(new FakeGUI(), data);

        // Alle relevanten Spieler haben hier zu beginn 0 Karotten
        int carrots = 0;

        // Anton steht auf Flaggenfeld, als 1. Spieler in der Rangfolge
        Player p = g.getProtagonist();
        assertEquals(carrots + 10, p.getCarrots());
        assertEquals(p, g.getProtagonist());
        g.move(52); // legaler Spielzug, Spielerwechsel

        // Berta steht auf Flaggenfeld, als 6. Spieler in der Rangfolge
        p = g.getProtagonist();
        assertEquals(carrots + 60, p.getCarrots());
        assertEquals(p, g.getProtagonist());
        g.move(17); // legaler Spielzug, Spielerwechsel

        // Cäsar steht auf Flaggenfeld, als 5. Spieler in der Rangfolge
        p = g.getProtagonist();
        assertEquals(carrots + 50, p.getCarrots());
        assertEquals(p, g.getProtagonist());
        g.move(33); // legaler Spielzug, Spielerwechsel
    }
}
