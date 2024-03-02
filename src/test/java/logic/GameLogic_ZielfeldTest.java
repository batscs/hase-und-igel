package logic;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameLogic_ZielfeldTest {

    /**
     * Hier wird ein Spiel für Testzwecke erzeugt.
     *
     * @return Das frei erzeugte Spiel
     */
    private GameLogic provideTestGame() {
        return new GameLogic(new FakeGUI(), "Anton", "Berta");
    }

    @Test
    public void zielfeldTest_cantReach_notEnoughCarrots() {
        GameLogic g = provideTestGame();
        Player p = g.getProtagonist();
        int position = p.getPosition();
        int carrots = p.getCarrots();
        int salad = p.getSalads();

        g.move(GameLogic.FINAL_FIELD_POSITION);

        // Karotten, Salate und Position sollen unverändert bleiben, da der Spieler
        // nicht das Ziel erreichen kann
        assertEquals(position, p.getPosition());
        assertEquals(carrots, p.getCarrots());
        assertEquals(salad, p.getSalads());
    }

    @Test
    public void zielfeldTest_canReach_10Carrots() {
        GameLogic g = provideTestGame();
        Player p = g.getProtagonist();

        p.moveTo(60);
        int position = p.getPosition();
        p.setSalads(0);

        // Zu viele Karotten zum bewegen
        p.setCarrots(200);
        g.move(GameLogic.FINAL_FIELD_POSITION);
        assertEquals(position, p.getPosition());

        // Nun ausreichend Karottens
        p.setCarrots(10);
        g.move(GameLogic.FINAL_FIELD_POSITION);
        assertEquals(GameLogic.FINAL_FIELD_POSITION, p.getPosition());
    }

    @Test
    public void zielfeldTest_canReach_enoughCarrotsConsideringTravelCost() {
        GameLogic g = provideTestGame();
        Player p = g.getProtagonist();

        p.setSalads(0);
        p.moveTo(60);
        int position = p.getPosition();

        int travelCost = GameBoard.calculatePrice(GameLogic.FINAL_FIELD_POSITION - position);
        assertNotEquals(0, travelCost);

        p.setCarrots(10 + travelCost);
        g.move(GameLogic.FINAL_FIELD_POSITION);

        assertEquals(GameLogic.FINAL_FIELD_POSITION, p.getPosition());
    }

    @Test
    public void zielfeldTest_canReach_increasingPrice() {
        GameLogic g = new GameLogic(new FakeGUI(), "A", "B", "C", "D", "E", "F");

        int position;
        int travelCost;
        int maximumCarrots;

        // Erster Spieler für 10 Karotten ins Ziel
        Player p = g.getProtagonist();
        p.setSalads(0);
        p.moveTo(60);
        position = p.getPosition();
        travelCost = GameBoard.calculatePrice(GameLogic.FINAL_FIELD_POSITION - position);
        maximumCarrots = 10 + travelCost;
        // 1 Karotte zu viel zum Betreten, Position also unverändert
        p.setCarrots(maximumCarrots + 1);
        g.move(GameLogic.FINAL_FIELD_POSITION);
        assertNotEquals(GameLogic.FINAL_FIELD_POSITION, p.getPosition());
        // Bedingung erfüllt zum Betreten des Zielfeldes, Position wird verändert
        p.setCarrots(maximumCarrots);
        g.move(GameLogic.FINAL_FIELD_POSITION);
        assertEquals(GameLogic.FINAL_FIELD_POSITION, p.getPosition());

        // Zweiter Spieler für 20 Karotten ins Ziel
        p = g.getProtagonist();
        p.setSalads(0);
        p.moveTo(60);
        position = p.getPosition();
        travelCost = GameBoard.calculatePrice(GameLogic.FINAL_FIELD_POSITION - position);
        maximumCarrots = 20 + travelCost;
        // 1 Karotte zu viel zum Betreten, Position also unverändert
        p.setCarrots(maximumCarrots + 1);
        g.move(GameLogic.FINAL_FIELD_POSITION);
        assertNotEquals(GameLogic.FINAL_FIELD_POSITION, p.getPosition());
        // Bedingung erfüllt zum Betreten des Zielfeldes, Position wird verändert
        p.setCarrots(maximumCarrots);
        g.move(GameLogic.FINAL_FIELD_POSITION);
        assertEquals(GameLogic.FINAL_FIELD_POSITION, p.getPosition());

        // Dritter Spieler für 30 Karotten ins Ziel
        p = g.getProtagonist();
        p.setSalads(0);
        p.moveTo(60);
        position = p.getPosition();
        travelCost = GameBoard.calculatePrice(GameLogic.FINAL_FIELD_POSITION - position);
        maximumCarrots = 30 + travelCost;
        // 1 Karotte zu viel zum Betreten, Position also unverändert
        p.setCarrots(maximumCarrots + 1);
        g.move(GameLogic.FINAL_FIELD_POSITION);
        assertNotEquals(GameLogic.FINAL_FIELD_POSITION, p.getPosition());
        // Bedingung erfüllt zum Betreten des Zielfeldes, Position wird verändert
        p.setCarrots(maximumCarrots);
        g.move(GameLogic.FINAL_FIELD_POSITION);
        assertEquals(GameLogic.FINAL_FIELD_POSITION, p.getPosition());

        // Vierter Spieler für 40 Karotten ins Ziel
        p = g.getProtagonist();
        p.setSalads(0);
        p.moveTo(60);
        position = p.getPosition();
        travelCost = GameBoard.calculatePrice(GameLogic.FINAL_FIELD_POSITION - position);
        maximumCarrots = 40 + travelCost;
        // 1 Karotte zu viel zum Betreten, Position also unverändert
        p.setCarrots(maximumCarrots + 1);
        g.move(GameLogic.FINAL_FIELD_POSITION);
        assertNotEquals(GameLogic.FINAL_FIELD_POSITION, p.getPosition());
        // Bedingung erfüllt zum Betreten des Zielfeldes, Position wird verändert
        p.setCarrots(maximumCarrots);
        g.move(GameLogic.FINAL_FIELD_POSITION);
        assertEquals(GameLogic.FINAL_FIELD_POSITION, p.getPosition());

        // Fünfter Spieler für 50 Karotten ins Ziel
        p = g.getProtagonist();
        p.setSalads(0);
        p.moveTo(60);
        position = p.getPosition();
        travelCost = GameBoard.calculatePrice(GameLogic.FINAL_FIELD_POSITION - position);
        maximumCarrots = 50 + travelCost;
        // 1 Karotte zu viel zum Betreten, Position also unverändert
        p.setCarrots(maximumCarrots + 1);
        g.move(GameLogic.FINAL_FIELD_POSITION);
        assertNotEquals(GameLogic.FINAL_FIELD_POSITION, p.getPosition());
        // Bedingung erfüllt zum Betreten des Zielfeldes, Position wird verändert
        p.setCarrots(maximumCarrots);
        g.move(GameLogic.FINAL_FIELD_POSITION);
        assertEquals(GameLogic.FINAL_FIELD_POSITION, p.getPosition());

        // Spiel nun beendet, da nur noch ein Spieler spielt
        p = g.getProtagonist(); // Sechster Spieler hiervor unbewegt, steht auf Position 0
        position = p.getPosition();
        assertTrue(g.isGameFinished());
        g.move(position + 1);
        // Position soll sich nicht verändern, da Spiel beendet
        assertEquals(position, p.getPosition());

    }
}
