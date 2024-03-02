package logic.fields;

import logic.*;
import logic.data.CardDeck;
import logic.data.CardEvent;
import logic.data.GamePosition;
import logic.data.KarotteChoice;
import util.log.Log;
import util.log.LogLevel;
import util.log.LogModule;

/**
 * Implementation der Field Superklasse: Repräsentiert ein Hasenfeld auf dem Spielbrett. Spieler
 * haben ziehen hierbei die erste Karte von dem Kartendeck.
 * <p>
 * Je nach Karte passieren unterschiedliche Ereignisse mit dem Spieler.
 *
 * @author github.com/batscs
 */
public class FieldHase extends FieldEmpty {

    /**
     * Attribut für das Kartendeck.
     */
    private final CardDeck cardDeck;

    /**
     * Attribut für die oberste Karte auf dem Deck
     */
    private CardEvent card;


    /**
     * Attribut zur Bestimmung wohin der Spieler verschoben werden soll (z.B. Nächste Karotte, eine
     * Position zurück, etc.)
     */
    private GamePosition targetPosition;

    public FieldHase(int position, CardDeck cardDeck) {
        super(position);
        targetPosition = GamePosition.DEFAULT;
        this.cardDeck = cardDeck;
    }

    @Override
    public FieldType getType() {
        return FieldType.HASE;
    }

    @Override
    public void interactBeforeMove(Player target) {
        drawCard(target);
    }

    @Override
    public void interactAfterMove(Player target, GameLogic game, GUIConnector gui) {

        if (targetPosition != GamePosition.DEFAULT) {
            /*
                targetPosition != DEFAULT trifft zu, falls der Spieler eine Karte gezogen hat,
                welche ihn verschiebt (z.B. NEXTCARROT, da dieser zwangsweise auf ein Karottenfeld
                verschoben wird)
            */
            game.blockNextPlayerTurn();
        }

        if (card == CardEvent.TAKEORGIVE) {
            game.enableCarrotsChoosing();
            gui.alertMultipleChoice(Token.FIELD_HASE_TAKEORGIVE, choice -> {
                // 1 = FIELD_HASE_TAKEORGIVE_TAKE
                if (choice == 1) {
                    Log.write(LogLevel.INFO, LogModule.GAME,
                            "User chose to give away 10 Carrots, standing on Hasenfeld");
                    game.carrotsChoice(KarotteChoice.REMOVE);
                    // 2 = FIELD_HASE_TAKEORGIVE_GIVE
                } else if (choice == 2) {
                    Log.write(LogLevel.INFO, LogModule.GAME,
                            "User chose to take additional 10 Carrots, standing on Hasenfeld");
                    game.carrotsChoice(KarotteChoice.ADD);
                    // else = 0 = FIELD_HASE_TAKEORGIVE_DO_NOTHING
                } else {
                    Log.write(LogLevel.INFO, LogModule.GAME,
                            "User chose to do nothing for this turn, standing on Hasenfeld");
                    game.carrotsChoice(KarotteChoice.CONTINUE_HASENFELD);
                }
                    }, Token.FIELD_HASE_TAKEORGIVE_DO_NOTHING, Token.FIELD_HASE_TAKEORGIVE_TAKE,
                    Token.FIELD_HASE_TAKEORGIVE_GIVE);
        } else if (card == CardEvent.MOVEAGAIN) {
            gui.alertConfirm(card.getMessage(), confirmed -> {

                if (!confirmed) {
                    // Optional, dass der Spieler noch ein weiteres mal ziehen darf
                    game.nextPlayerTurn();
                }

            });
        } else if (card != null) {
            gui.updateStats(game.getPlayerID(target), target.getName(), target.getCarrots(),
                    target.getSalads());
            gui.alertEvent(card.getMessage(), target, game::nextPlayerTurn);
        }

    }

    @Override
    public void interactOnIdle(Player target, GameLogic game, GUIConnector gui) {

        // Im vorherigen Zug EATSALAD Karte
        if (target.isSuspended() && target.eatsSalad()) {
            if (target.getSalads() > 0) {
                target.setEatsSalad(true);
                target.setSuspended(false);
                target.setSalads(target.getSalads() - 1);
                Log.write(LogLevel.TRACE, LogModule.GAME, "Player has eaten a salad.");
                gui.updateStats(game.getPlayerID(target), target.getName(), target.getCarrots(),
                        target.getSalads());
                gui.alertEvent(Token.FIELD_HASE_EATSALAD, target, game::nextPlayerTurn);
            } else {
                target.setSuspended(false);
                target.setEatsSalad(false);
                Log.write(LogLevel.TRACE, LogModule.GAME, "Player has no Salad left to eat.");
                gui.alertEvent(Token.FIELD_HASE_IDLE_SUSPEND, target, game::nextPlayerTurn);
            }
        }
        // Im vorherigen Zug SUSPEND Karte (oder Spielstand geladen, wo Spieler suspended ist)
        else if (target.isSuspended() && (card == CardEvent.SUSPEND || card == null)) {
            target.setSuspended(false);
            gui.alertEvent(Token.FIELD_HASE_IDLE_SUSPEND, target, game::nextPlayerTurn);
        }
        // Verarbeitung wohin der Spieler verschoben werden soll, falls überhaupt
        else if (targetPosition != GamePosition.DEFAULT) {
            int position = game.getAvailablePosition(targetPosition);
            Log.write(LogLevel.TRACE, LogModule.GAME,
                    "New Position from Hasenkarte for current Player: " + position);

            // Spieler soll nur Position ändern, wenn eine Verfügbar ist
            if (position != target.getPosition()) {
                // Spieler wird gezwungen auf eine Position zu gehen
                // ohne Beachtung von Spiellogik-Regeln wie Kosten des Weges
                gui.movePlayer(game.getPlayerID(target), target.getPosition(), position, () -> {
                    target.moveTo(position);
                    if (position == GameLogic.FINAL_FIELD_POSITION) {
                        game.onPlayerFinish(target);
                        if (!game.checkForGameEnd()) {
                            game.afterMove();
                        }
                    } else {
                        game.afterMove();
                    }
                });
                // Spielerwechsel wird hierbei bei dem neuen Feld,
                // auf welches der Spieler bewegt wird bei field.gameEventAfterMove() ausgeführt
                // durch das game::afterMove Objekt im gui.movePlayer
            } else {
                gui.alertEvent(Token.FIELD_HASE_POSITION_UNCHANGED, target, game::nextPlayerTurn);
            }

            targetPosition = GamePosition.DEFAULT;
        }

    }

    /**
     * Methode, welche die erste Karte vom Kartendeck zieht und auf einen bestimmten Spieler eine
     * entsprechende Wirkung hat. Nach dem ziehen der Karte wird diese auf den hintersten Platz des
     * Decks verschoben, und alle anderen Karten rücken einen Platz vor.
     *
     * @param target Der Spieler
     */
    private void drawCard(Player target) {
        card = cardDeck.pop();

        Log.write(LogLevel.INFO, LogModule.GAME,
                "Drawing card " + card.name() + " for player " + target.getName());

        switch (card) {
            case REFUND -> {
                target.setCarrots(target.getCarrots() + GameBoard.calculatePrice(
                        position - target.getPosition()));
            }
            case SUSPEND -> {
                target.setSuspended(true);
            }
            case EATSALAD -> {
                target.setSuspended(true);
                target.setEatsSalad(true);
            }
            case FORWARDS -> {
                targetPosition = GamePosition.FORWARDS;
            }
            case BACKWARDS -> {
                targetPosition = GamePosition.BACKWARDS;
            }
            case NEXTCARROT -> {
                targetPosition = GamePosition.NEXTCARROT;
            }
            case PREVIOUSCARROT -> {
                targetPosition = GamePosition.PREVIOUSCARROT;
            }
            default -> {
            }
        }
        ;
    }
}
