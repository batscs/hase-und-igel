package logic.data;

import logic.Token;

/**
 * Enum, welches die verschiedenen Möglichkeiten einer Hasenkarte repräsentiert,
 * sowie die Nachricht im Front-End als Attribut besitzt.
 *
 * @author github.com/batscs
 */
public enum CardEvent {

    REFUND(Token.FIELD_HASE_REFUND),
    TAKEORGIVE(Token.FIELD_HASE_TAKEORGIVE),
    SUSPEND(Token.FIELD_HASE_SUSPEND),
    BACKWARDS(Token.FIELD_HASE_BACKWARDS),
    FORWARDS(Token.FIELD_HASE_FORWARDS),
    EATSALAD(Token.FIELD_HASE_EATSALAD),
    MOVEAGAIN(Token.FIELD_HASE_MOVEAGAIN),
    NEXTCARROT(Token.FIELD_HASE_NEXTCARROT),
    PREVIOUSCARROT(Token.FIELD_HASE_PREVIOUSCARROT);

    /**
     * Attribut für die zu darstellende Nachricht im Front-End.
     */
    private final Token message;

    /**
     * Initialisiert das Enum mit einem zugehörigen Token.
     *
     * @param message der Token
     */
    CardEvent(Token message) {
        this.message = message;
    }

    /**
     * Liefert den Token des Events.
     *
     * @return der Token
     */
    public Token getMessage() {
        return message;
    }

}
