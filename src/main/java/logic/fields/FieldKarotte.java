package logic.fields;

import logic.GUIConnector;
import logic.GameLogic;
import logic.Player;
import logic.Token;
import logic.data.KarotteChoice;
import util.log.Log;
import util.log.LogLevel;
import util.log.LogModule;

/**
 * Implementation des Field Superklasse:
 * Repräsentiert ein Karottenfeld auf dem Spielbrett.
 * Spieler haben hierbei die Wahl Karotten zu nehmen oder abzugeben, oder weiterzuzihene.
 *
 * @author github.com/batscs
 */
public class FieldKarotte extends FieldEmpty {

    public FieldKarotte(int position) {
        super(position);
    }

    @Override
    public FieldType getType() {
        return FieldType.KAROTTE;
    }

    @Override
    public void interactOnIdle(Player target, GameLogic game, GUIConnector gui) {

        game.enableCarrotsChoosing();
        gui.alertMultipleChoice(Token.FIELD_CARROT_CHOICE, choice -> {
            if (choice == 1) {
                Log.write(LogLevel.INFO, LogModule.GAME, "User chose to give away 10 Carrots, standing on Karottenfeld");
                game.carrotsChoice(KarotteChoice.ADD);
            } else if (choice == 2) {
                Log.write(LogLevel.INFO, LogModule.GAME, "User chose to take additional 10 Carrots, standing on Karrotenkarte");
                game.carrotsChoice(KarotteChoice.REMOVE);
            } else {
                Log.write(LogLevel.INFO, LogModule.GAME, "User chose to continue to draw new cards, standing on Karrotenkarte");
                game.carrotsChoice(KarotteChoice.CONTINUE_KAROTTENFELD);
            }
        }, Token.FIELD_CARROT_CONTINUE_DRAWING, Token.FIELD_CARROT_GIVE, Token.FIELD_CARROT_TAKE);

        // Hier kein nextPlayerTurn(), da der nächste Zug über die Front-End Buttons entschieden wird
    }

}
