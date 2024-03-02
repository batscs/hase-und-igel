package logic.fields;

import logic.GUIConnector;
import logic.GameLogic;
import logic.Player;

/**
 * Superklasse, welche ein generisches Feld auf dem Spielbrett repräsentiert.
 * Benutzt Referenzen zur Oberfläche um Spielzüge und Veränderungen dort zu repräsentieren
 * <p>
 * Kein Interface, da Attribute sowie Konstruktor vererbt werden. Wird als Start- und Zielfeld
 * verwendet.
 *
 * @author github.com/batscs
 */
public class FieldEmpty implements Field{

    /**
     * Attribut für die Position des Feldes im Spielfeld
     */
    protected int position;

    /**
     * Default Konstruktor zum Initialisieren eines Feldes mit einer bestimmten Position.
     *
     * @param position Die Position auf dem Spielfeld
     */
    public FieldEmpty(int position) {
        this.position = position;
    }

    @Override
    public FieldType getType() {
        return FieldType.EMPTY;
    }

    @Override
    public void interactBeforeMove(Player target) {
        // Nichts soll passieren beim Feld
    }

    @Override
    public void interactAfterMove(Player target, GameLogic game, GUIConnector gui) {
        game.nextPlayerTurn();
    }

    @Override
    public void interactOnIdle(Player target, GameLogic game, GUIConnector gui) {
        // Nichts soll passieren beim Feld
    }

    @Override
    public int getPosition() {
        return position;
    }

}
