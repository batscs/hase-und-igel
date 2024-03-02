package logic;

import logic.data.CardEvent;
import logic.data.KarotteChoice;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class GameLogic_HasenfeldTest {

    /**
     * Hier wird ein Spiel für Testzwecke erzeugt.
     * @return Das frei erzeugte Spiel
     */
    private GameLogic provideTestGame() {
        return new GameLogic(new FakeGUI(), "Anton", "Berta", "Cäsar");
    }

    public static final String PATH_DIRECTORY =
            String.join(File.separator, "src", "test", "files", "logic", "hasenfeld");

    private String toPath(String file) {
        return PATH_DIRECTORY + File.separator + file;
    }

    @Test
    public void hasenkarteTest_refund() {
        GameLogic game = provideTestGame();

        Player first = game.getProtagonist();
        int before = first.getCarrots();

        game.forceCard(CardEvent.REFUND);

        game.move(3);

        assertEquals(before, first.getCarrots());
    }

    @Test
    public void hasenkarteTest_eatSalad() {
        GameLogic game = new GameLogic(new FakeGUI(), "Anton", "Berta");
        game.forceCard(CardEvent.EATSALAD);

        // Anton geht auf Salatfeld
        Player p = game.getProtagonist();
        int salads = p.getSalads();
        game.move(1);

        assertTrue(p.eatsSalad());

        // Berta macht Zug
        game.move(4);

        // Anton hat nun Salat gegessen
        assertEquals(salads - 1, p.getSalads());

        // Da Anton einen Salat gegessen, wird er diesen Zug übersprungen
        assertNotEquals(p, game.getProtagonist());
    }

    @Test
    public void hasenkarteTest_eatSalad_hasNoSalads() {
        GameLogic game = new GameLogic(new FakeGUI(), "Anton", "Berta");
        game.forceCard(CardEvent.EATSALAD);

        // Anton geht auf Salatfeld
        Player p = game.getProtagonist();
        p.setSalads(0);
        game.move(1);

        // Anton soll einen Salat essen
        assertTrue(p.eatsSalad());

        // Berta macht Zug
        game.move(4);

        // Anton kriegt keine negative Anzahl an Salaten, seine Zahl bleibt unverändert
        assertEquals(0, p.getSalads());
        assertFalse(p.isSuspended()); // Anton isst keinen Salat mehr

        // Anton ist nun am Zug
        assertNotEquals(p, game.getProtagonist());
    }

    @Test
    public void hasenfeldTest_karottenNehmen() {
        GameLogic g = new GameLogic(new FakeGUI(), "Anton", "Berta");
        Player p = g.getProtagonist();

        g.forceCard(CardEvent.TAKEORGIVE);

        // Anton bewegt sich auf Hasenfeld
        g.move(1);

        // Noch kein Spielerwechsel, erst nach dem Karottenzug
        assertEquals(p, g.getProtagonist());

        int carrots = p.getCarrots();

        g.carrotsChoice(KarotteChoice.ADD);

        assertEquals(carrots + 10, p.getCarrots());
        assertNotEquals(p, g.getProtagonist());
    }

    @Test
    public void hasenfeldTest_karottenGeben() {
        GameLogic g = new GameLogic(new FakeGUI(), "Anton", "Berta");
        Player p = g.getProtagonist();

        g.forceCard(CardEvent.TAKEORGIVE);

        // Anton bewegt sich auf Hasenfeld
        g.move(1);

        // Noch kein Spielerwechsel, erst nach dem Karottenzug
        assertEquals(p, g.getProtagonist());

        int carrots = p.getCarrots();

        g.carrotsChoice(KarotteChoice.REMOVE);

        assertEquals(carrots - 10, p.getCarrots());
        assertNotEquals(p, g.getProtagonist());
    }

    @Test
    public void hasenfeldTest_karottenGeben_notEnough() {
        GameLogic g = new GameLogic(new FakeGUI(), "Anton", "Berta");
        Player p = g.getProtagonist();

        g.forceCard(CardEvent.TAKEORGIVE);

        // Anton bewegt sich auf Hasenfeld
        g.move(1);

        // Noch kein Spielerwechsel, erst nach dem Karottenzug
        assertEquals(p, g.getProtagonist());
        p.setCarrots(9);
        int carrots = p.getCarrots();

        g.carrotsChoice(KarotteChoice.REMOVE);

        // Karotten unverändert, da nicht genügend zum abgeben
        assertEquals(carrots, p.getCarrots());

        // Anton noch am Zug, da keine gültige Aktion
        assertEquals(p, g.getProtagonist());
    }

    @Test
    public void hasenfeldTest_karottenWeiterziehen() {
        GameLogic g = new GameLogic(new FakeGUI(), "Anton", "Berta");
        Player p = g.getProtagonist();

        g.forceCard(CardEvent.TAKEORGIVE);

        // Anton bewegt sich auf Hasenfeld
        g.move(1);

        // Noch kein Spielerwechsel, erst nach dem Karottenzug
        assertEquals(p, g.getProtagonist());

        int carrots = p.getCarrots();

        g.carrotsChoice(KarotteChoice.CONTINUE_HASENFELD);

        assertEquals(carrots, p.getCarrots());

        assertNotEquals(p, g.getProtagonist());
    }

    @Test
    public void hasenfeldTest_karottenSecurity() {
        GameLogic g = new GameLogic(new FakeGUI(), "Anton", "Berta");
        Player p = g.getProtagonist();

        g.forceCard(CardEvent.MOVEAGAIN);

        // Anton bewegt sich auf Hasenfeld
        g.move(1);

        // Noch kein Spielerwechsel
        assertEquals(p, g.getProtagonist());

        int carrots = p.getCarrots();

        g.carrotsChoice(KarotteChoice.ADD);

        // Keine Änderung von Karotten, da Karte MOVEAGAIN ist und nicht die für Karotten
        assertEquals(carrots, p.getCarrots());
    }

    @Test
    public void hasenkarteTest_moveAgain() {
        GameLogic game = provideTestGame();

        Player first = game.getProtagonist();

        game.forceCard(CardEvent.MOVEAGAIN);

        game.move(3);
        game.move(4); // es wird immer noch first bewegt, wegen MOVEAGAIN

        assertEquals(4, first.getPosition());
    }

    @Test
    public void hasenkarteTest_suspend() {
        GameLogic game = new GameLogic(new FakeGUI(), "Anton", "Berta");

        Player first = game.getProtagonist();
        game.forceCard(CardEvent.SUSPEND);

        // Antons Zug, wird hier Suspended
        game.move(3);
        assertTrue(first.isSuspended());

        // Bertas Zug
        game.move(4);

        // Wieder Bertas Zug, da Anton suspended war
        assertNotEquals(first, game.getProtagonist());
        game.move(9);

        // Antons Zug wieder, nicht mehr suspended
        assertEquals(first, game.getProtagonist());
        assertFalse(first.isSuspended());

    }

    @Test
    public void hasenkarteTest_einholen() {
        String path = toPath("test_hasenfeld_einholen.json");
        File file = new File(path);
        GameData data = GameData.validate(file);

        assertNotNull(data);

        GameLogic game = new GameLogic(new FakeGUI(), data);
        game.forceCard(CardEvent.FORWARDS);
        Player player = game.getProtagonist();

        game.move(1);
        int expectedPosition = 9;

        assertEquals(expectedPosition, player.getPosition());
    }

    @Test
    public void hasenkarteTest_einholen_mehrere_spieler() {
        String path = toPath("test_hasenfeld_einholen_mehrere_spieler.json");
        File file = new File(path);
        GameData data = GameData.validate(file);

        assertNotNull(data);

        GameLogic game = new GameLogic(new FakeGUI(), data);
        game.forceCard(CardEvent.FORWARDS);
        Player player = game.getProtagonist();

        game.move(1);
        int expectedPosition = 14;

        assertEquals(expectedPosition, player.getPosition());
    }

    @Test
    public void hasenkarteTest_einholen_rangfolge() {
        String path = toPath("test_hasenfeld_einholen_rangfolge.json");
        File file = new File(path);
        GameData data = GameData.validate(file);

        assertNotNull(data);

        GameLogic game = new GameLogic(new FakeGUI(), data);
        game.forceCard(CardEvent.FORWARDS);
        Player player = game.getProtagonist();

        game.move(14);
        int expectedPosition = 16;

        assertEquals(expectedPosition, player.getPosition());
    }

    @Test
    public void hasenkarteTest_einholen_vor_finale() {
        String path = toPath("test_hasenfeld_einholen_vor_finale.json");
        File file = new File(path);
        GameData data = GameData.validate(file);

        assertNotNull(data);

        GameLogic game = new GameLogic(new FakeGUI(), data);
        game.forceCard(CardEvent.FORWARDS);
        Player player = game.getProtagonist();

        game.move(58);
        int expectedPosition = GameLogic.FINAL_FIELD_POSITION;

        assertEquals(expectedPosition, player.getPosition());
    }

    @Test
    public void hasenkarteTest_einholen_erster_platz() {
        GameLogic game = provideTestGame();
        game.forceCard(CardEvent.FORWARDS);
        Player player = game.getProtagonist();

        game.move(1);
        int expectedPosition = 1;

        assertEquals(expectedPosition, player.getPosition());
    }

    @Test
    public void hasenkarteTest_zurueck() {
        String path = toPath("test_hasenfeld_zurueck.json");
        File file = new File(path);
        GameData data = GameData.validate(file);

        assertNotNull(data);

        GameLogic game = new GameLogic(new FakeGUI(), data);
        game.forceCard(CardEvent.BACKWARDS);
        Player player = game.getProtagonist();
        int carrots = player.getCarrots();

        game.move(14);

        int expectedPosition = 9;
        int expectedCarrots = carrots - 1 - 2;

        assertEquals(expectedPosition, player.getPosition());
        assertEquals(expectedCarrots, player.getCarrots());
    }

    @Test
    public void hasenkarteTest_zurueck_igelfeld() {
        String path = toPath("test_hasenfeld_zurueck_igelfeld.json");
        File file = new File(path);
        GameData data = GameData.validate(file);

        assertNotNull(data);

        GameLogic game = new GameLogic(new FakeGUI(), data);
        game.forceCard(CardEvent.BACKWARDS);
        Player player = game.getProtagonist();
        int carrots = player.getCarrots();

        game.move(14);
        int expectedPosition = 8;
        int expectedCarrots = carrots - 1 - 2 - 3 - 4;

        // Karotten soll nur der Wegpreis abgezogen werden
        // aber nicht noch zusätzlich Karottenerhalt durch Igelfeld

        assertEquals(expectedPosition, player.getPosition());
        assertEquals(expectedCarrots, player.getCarrots());
    }

    @Test
    public void hasenkarteTest_zurueck_salatfeld_ohne_salat() {
        String path = toPath("test_hasenfeld_zurueck_salatfeld_ohne_salate.json");
        File file = new File(path);
        GameData data = GameData.validate(file);

        assertNotNull(data);

        GameLogic game = new GameLogic(new FakeGUI(), data);
        game.forceCard(CardEvent.BACKWARDS);
        Player player = game.getProtagonist();

        game.move(14);
        int expectedPosition = 6;

        assertEquals(expectedPosition, player.getPosition());
    }

    @Test
    public void hasenkarteTest_zurueck_erstes_feld() {
        String path = toPath("test_hasenfeld_zurueck_erstes_feld.json");
        File file = new File(path);
        GameData data = GameData.validate(file);

        assertNotNull(data);

        GameLogic game = new GameLogic(new FakeGUI(), data);
        game.forceCard(CardEvent.BACKWARDS);
        Player player = game.getProtagonist();

        game.move(3);
        int expectedPosition = 0;

        assertEquals(expectedPosition, player.getPosition());
    }

    @Test
    public void hasenkarteTest_zurueck_startfeld() {
        String path = toPath("test_hasenfeld_zurueck_startfeld.json");
        File file = new File(path);
        GameData data = GameData.validate(file);

        assertNotNull(data);

        GameLogic game = new GameLogic(new FakeGUI(), data);
        game.forceCard(CardEvent.BACKWARDS);
        Player player = game.getProtagonist();

        game.move(3);
        int expectedPosition = 0;

        assertEquals(expectedPosition, player.getPosition());
    }

    @Test
    public void hasenkarteTest_zurueck_letzter_platz() {
        String path = toPath("test_hasenfeld_zurueck_letzter_platz.json");
        File file = new File(path);
        GameData data = GameData.validate(file);

        assertNotNull(data);

        GameLogic game = new GameLogic(new FakeGUI(), data);
        game.forceCard(CardEvent.BACKWARDS);
        Player player = game.getProtagonist();

        game.move(1);

        int expectedPosition = 1;
        assertEquals(expectedPosition, player.getPosition());
    }

    @Test
    public void hasenkarteTest_nextcarrot_hasNext() {
        GameLogic game = provideTestGame();
        game.forceCard(CardEvent.NEXTCARROT);
        Player p = game.getProtagonist();

        int carotts = p.getCarrots();
        game.move(1);

        int expectedPosition = 2;
        // Karotten sollen nur für den Weg auf das Hasenfeld abgezogen werden
        // nicht noch zusätzlich für den Weg zum Karottenfeld
        int expectedCarrots = carotts - GameBoard.calculatePrice(1);


        assertEquals(expectedPosition, p.getPosition());
        assertEquals(expectedCarrots, p.getCarrots());
    }

    @Test
    public void hasenkarteTest_nextcarrot_noNext() {
        GameLogic game = provideTestGame();
        game.forceCard(CardEvent.NEXTCARROT);
        Player p = game.getProtagonist();

        // Künstliches verschieben auf ein Feld, wonach kein Karottenfeld folgt
        p.moveTo(60);

        game.move(61);

        int expectedPosition = 61;
        assertEquals(expectedPosition, p.getPosition());
    }

    @Test
    public void hasenkarteTest_nextcarrot_occupiedNext() {
        GameLogic game = provideTestGame();
        game.forceCard(CardEvent.NEXTCARROT);
        Player other = game.getProtagonist();
        game.move(2);

        Player p = game.getProtagonist();
        game.move(1);

        int expectedPosition = 5;
        assertEquals(expectedPosition, p.getPosition());
        assertNotEquals(other, p);
    }

    @Test
    public void hasenkarteTest_prevcarrot_hasPrev() {
        GameLogic game = provideTestGame();
        game.forceCard(CardEvent.PREVIOUSCARROT);

        Player p = game.getProtagonist();
        game.move(3);

        int expectedPosition = 2;
        assertEquals(expectedPosition, p.getPosition());
    }

    @Test
    public void hasenkarteTest_prevcarrot_noPrev() {
        GameLogic game = provideTestGame();
        game.forceCard(CardEvent.PREVIOUSCARROT);

        Player p = game.getProtagonist();
        game.move(1);

        int expectedPosition = 1;
        assertEquals(expectedPosition, p.getPosition());
    }

    @Test
    public void hasenkarteTest_prevcarrot_occupiedPrev_noBefore() {
        GameLogic game = provideTestGame();

        Player other = game.getProtagonist();
        game.move(2);

        Player p = game.getProtagonist();
        game.forceCard(CardEvent.PREVIOUSCARROT);
        game.move(3);

        int expectedPosition = 3;
        assertEquals(expectedPosition, p.getPosition());
        assertNotEquals(other, p);
    }

    @Test
    public void hasenkarteTest_prevcarrot_occupiedPrev_hasBefore() {
        GameLogic game = provideTestGame();

        Player other = game.getProtagonist();
        game.move(5);

        Player p = game.getProtagonist();
        game.forceCard(CardEvent.PREVIOUSCARROT);
        game.move(6);

        int expectedPosition = 2;
        assertEquals(expectedPosition, p.getPosition());
        assertNotEquals(other, p);
    }

}
