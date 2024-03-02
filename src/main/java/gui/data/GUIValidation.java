package gui.data;

/**
 * Enum, welches die verschiedenen auftretenden Fehler repräsentieren kann, oder ein valides Ergenis.
 * Fehler bezogen auf GUI Ereignisse.
 *
 * @author github.com/batscs
 */
public enum GUIValidation {
    VALID,
    INVALID_CUSTOM_LANGUAGE_FILE_SIZE,
    INVALID_CUSTOM_LANGUAGE_FILE_GSON,
    INVALID_CUSTOM_LANGUAGE_FILE_READ,
}
