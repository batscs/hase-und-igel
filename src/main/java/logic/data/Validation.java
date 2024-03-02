package logic.data;

/**
 * Enum, welches die verschiedenen auftretenden Fehler repräsentieren kann, oder ein valides Ergenis.
 * Fehler bezogen auf Spiel-Ereignisse.
 *
 * @author github.com/batscs
 */
public enum Validation {

    VALID,
    INVALID_GAME_PLAYERS,
    INVALID_GAME_ONTARGET,
    INVALID_GAME_PROTAGONIST,
    INVALID_GAME_ONTARGET_PLAYER_OUT_OF_BOUNDS,
    INVALID_PLAYER_USERNAME,
    INVALID_PLAYER_POSITION,
    INVALID_PLAYER_CARROTS,
    INVALID_PLAYER_FINISH_TOO_MANY_SALADS,
    INVALID_PLAYER_FINISH_TOO_MANY_CARROTS,
    INVALID_PLAYER_POSITION_FINISH,
    INVALID_PLAYER_NAME_DUPLICATE,
    INVALID_PLAYER_SALADS

}
