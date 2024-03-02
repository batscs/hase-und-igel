package logic;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class GameLogic_IgelfeldTest {

    public static final String PATH_DIRECTORY =
            String.join(File.separator, "src", "test", "files", "logic", "igelfeld");

    private String toPath(String file) {
        return PATH_DIRECTORY + File.separator + file;
    }

    /**
     * Hier wird ein Spiel für Testzwecke erzeugt.
     *
     * @return Das frei erzeugte Spiel
     */
    private GameLogic provideTestGame() {
        return new GameLogic(new FakeGUI(), "Anton", "Berta");
    }

    @Test
    public void igelTest_zurueckZiehen_1Feld() {
        GameLogic g = provideTestGame();

        Player p = g.getProtagonist();

        // Kalibrierung der Startwerte
        int carrots = 68;
        assertEquals(0, p.getPosition());
        assertEquals(carrots, p.getCarrots());

        // Antons Zug
        g.move(9);
        carrots = carrots - GameBoard.calculatePrice(9);
        assertEquals(9, p.getPosition());
        assertEquals(carrots, p.getCarrots());

        // Bertas Zug
        g.move(4);

        // Antons Zug, rückwärts auf Igelfeld
        g.move(8);
        carrots = carrots + 10; // + 10, da ein Feld bewegt zum Igelfeld
        assertEquals(8, p.getPosition());
        assertEquals(carrots, p.getCarrots());
    }

    @Test
    public void igelTest_zurueckZiehen_2Felder() {
        GameLogic g = provideTestGame();

        Player p = g.getProtagonist();

        // Kalibrierung der Startwerte
        int carrots = 68;
        assertEquals(0, p.getPosition());
        assertEquals(carrots, p.getCarrots());

        // Antons Zug
        g.move(10);
        carrots = carrots - GameBoard.calculatePrice(10);
        assertEquals(10, p.getPosition());
        assertEquals(carrots, p.getCarrots());

        // Bertas Zug
        g.move(4);

        // Antons Zug, rückwärts auf Igelfeld
        g.move(8);
        carrots = carrots + 20; // + 10, da ein Feld bewegt zum Igelfeld
        assertEquals(8, p.getPosition());
        assertEquals(carrots, p.getCarrots());
    }

}
