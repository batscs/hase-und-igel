package gui.data;

import logic.Token;

/**
 * Enum, welches eine Fehlermeldung repräsentiert, dabei enthält es jeweils einen Fehlercode, sowie
 * eine Fehlermeldung, welche durch eigene Sprachimports verändert werden kann.
 *
 * @author github.com/batscs
 */
public enum Error {

    // 1xx = File Loading Errors
    ERROR_FILE_NO_FILE_SELECTED(100, Token.HEADER_ERROR_FILE_LOADING,
            Token.ERROR_FILE_NO_FILE_SELECTED),
    ERROR_FILE_IMPORT_FILE_WRONG_JSON_FORMAT(103, Token.HEADER_ERROR_FILE_LOADING,
            Token.ERROR_FILE_IMPORT_FILE_WRONG_JSON_FORMAT),

    ERROR_FILE_IMPORT_WRITE(101, Token.HEADER_ERROR_FILE_LOADING, Token.ERROR_FILE_WRITE),

    ERROR_FILE_IMPORT_READ(102, Token.HEADER_ERROR_FILE_LOADING, Token.ERROR_FILE_READ),

    // 2xx = Game Related Errors,
    ERROR_FILE_IMPORT_GAME_INVALID_DATA(200, Token.HEADER_ERROR_GAMEPLAY,
            Token.ERROR_GAME_INVALID_DATA),
    ERROR_GAME_CANT_START_DUPLICATE_NAMES(201, Token.HEADER_ERROR_GAMEPLAY,
            Token.ERROR_GAME_CANT_START_DUPLICATE_NAMES),


    // 3xx = Language Related Errors,
    ERROR_FILE_IMPORT_LANGUAGE_INVALID_DATA(300, Token.HEADER_ERROR_LANGUAGE,
            Token.ERROR_LANGUAGE_INVALID_DATA), // Kann man die invalid_data sachen als eines zusammenfassen?

    ERROR_FILE_IMPORT_LANGUAGE_INVALID_SIZE(301, Token.HEADER_ERROR_LANGUAGE,
            Token.ERROR_LANGUAGE_INVALID_SIZE);

    /**
     * Attribut, welches den Fehlercode repräsentiert.
     */
    private final int code;

    /**
     * Attribut, welches die Fehlermeldung vollständig repräsentiert.
     */
    private final Token msg, header;

    /**
     * Konstruktor zum Initialisieren eines Error-Enums
     *
     * @param code   Der ErrorCode als Ganzzahl
     * @param header Die Überschrift der Fehlermeldung
     * @param msg    Die genauere Beschreibung der Fehlermeldung
     */
    Error(int code, Token header, Token msg) {
        this.code = code;
        this.header = header;
        this.msg = msg;
    }

    /**
     * Getter für die Beschreibung der Fehlermeldung
     *
     * @return der Token
     */
    public Token getMsg() {
        return msg;
    }

    /**
     * Getter für den ErrorCode der Fehlermeldung
     *
     * @return der ErrorCode als Ganzzahl
     */
    public int getCode() {
        return code;
    }

    /**
     * Getter für die Überschrift der Fehlermeldung
     *
     * @return der Token
     */
    public Token getHeader() {
        return header;
    }
}
