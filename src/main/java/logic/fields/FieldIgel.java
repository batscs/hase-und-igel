package logic.fields;

import logic.GUIConnector;
import logic.GameLogic;
import logic.Player;

/**
 * Implementation der Field Superklasse:
 * Repräsentiert ein Igelfeld auf dem Spielbrett.
 * <p>
 * Spieler können nur beim Zurückziehen auf ein Igelfeld und bekommen dabei Karotten geschenkt.
 *
 * @author github.com/batscs
 */
public class FieldIgel extends FieldEmpty {

    public FieldIgel(int position) {
        super(position);
    }

    @Override
    public FieldType getType() {
        return FieldType.IGEL;
    }

    @Override
    public void interactBeforeMove(Player target) {
        // Distanz kann berechnet werden, da zuerst interact() ausgeführt wird,
        // und danach erst der Spieler bewegt wird
        int distance = target.getPosition() - position;
        target.setCarrots(target.getCarrots() + (10 * distance));
    }

    @Override
    public void interactOnIdle(Player target, GameLogic game, GUIConnector gui) {
        // nichts passiert hier
    }

}
