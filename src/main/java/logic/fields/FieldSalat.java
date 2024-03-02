package logic.fields;

import logic.GUIConnector;
import logic.GameLogic;
import logic.Player;
import logic.Token;

/**
 * Implementation der Field Superklasse:
 * Repräsentiert eine Salatkarte auf dem Spielbrett.
 * <p>
 * Spieler können hier ihren Salat loswerden, da für einen Sieg der Spieler keine Salate mehr übrig haben darf.
 *
 * @author github.com/batscs
 */
public class FieldSalat extends FieldEmpty {

    public FieldSalat(int position) {
        super(position);
    }

    @Override
    public FieldType getType() {
        return FieldType.SALAT;
    }

    @Override
    public void interactBeforeMove(Player target) {
        if (target.getSalads() > 0) {
            target.setEatsSalad(true);
        }
    }

    @Override
    public void interactOnIdle(Player target, GameLogic game, GUIConnector gui) {
        if (target.eatsSalad() && target.getSalads() > 0) {
            target.setEatsSalad(false);
            target.setSalads(target.getSalads() - 1);
            target.setCarrots(target.getCarrots() + (game.getPlaceOfProtagonist() + 1) * 10);

            gui.updateStats(game.getPlayerID(target), target.getName(), target.getCarrots(), target.getSalads());
            gui.alertEvent(Token.FIELD_SALAD_EAT, target, game::nextPlayerTurn);
        }
    }

}
