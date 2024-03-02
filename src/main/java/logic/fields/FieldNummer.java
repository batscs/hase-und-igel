package logic.fields;

import logic.GUIConnector;
import logic.GameLogic;
import logic.Player;
import logic.Token;
import logic.data.NummerFlag;
import util.ArrayUtils;

/**
 * Implementation der Field Superklasse:
 * Repräsentiert eine Zahlen- oder Flaggenkarte auf dem Spielbrett.
 * <p>
 * Spieler bekommen hierbei Karotten geschenkt wenn ihr Platz einstimmig mit der Zahl auf der Karte ist.
 *
 * @author github.com/batscs
 */
public class FieldNummer extends FieldEmpty {

    /**
     * Attribut zur Definierung welche Zahl (oder Flagge) auf dem Feld steht.
     * Der Spieler strebt dabei eine gleichgültige Platzierung an.
     */
    private final NummerFlag desiredPlace;

    /**
     * Konstruktor zum Initialisieren eines NummernFeldes mit einer bestimmten Zahl auf dem Feld,
     * welche vom Spieler angestrebt wird, dass diese den Platz in der Rangfolge im Spiel gleicht.
     *
     * @param position      Position auf dem Spielfeld
     * @param desiredPlace Nummer auf dem Feld
     */
    public FieldNummer(int position, NummerFlag desiredPlace) {
        super(position);
        this.desiredPlace = desiredPlace;
    }

    @Override
    public FieldType getType() {
        return FieldType.NUMMER;
    }

    @Override
    public void interactBeforeMove(Player target) {
        // NICHTS
    }

    @Override
    public void interactOnIdle(Player target, GameLogic game, GUIConnector gui) {
        // +1 Da getPlaceOfProtagonist intern bei 0 anfängt
        // und die Nummern der Felder bei 1 anfangen
        int placement = game.getPlaceOfProtagonist() + 1;
        if (ArrayUtils.arrayContains(desiredPlace.getDesiredPlace(), placement)) {
            int gainedCarrots = placement * 10;
            target.setCarrots(target.getCarrots() + gainedCarrots);
            gui.updateStats(game.getPlayerID(target), target.getName(), target.getCarrots(), target.getSalads());
            gui.alertEvent(Token.FIELD_NUMBER_GAINED_CARROTS, target, game::redraw);
        }
    }
}
