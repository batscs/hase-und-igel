package util.log;

/**
 * Aufzählungstyp für die verschiedenen Log-Level beim Loggen von Ereignissen.
 *
 * @author github.com/batscs
 */
public enum LogLevel {

    /**
     * Loggt Informationen zu Ereignissen
     */
    INFO,

    /**
     * Loggt nachvollziehbar Benutzerverhalten im Spielverlauf
     */
    TRACE,

    /**
     * Loggt zusätzliche Informationen zum debuggen
     */
    DEBUG,

    /**
     * Loggt Warnungen: Hindert nicht das Programm bei einer Aktion fortzufahren
     */
    WARN,

    /**
     * Loggt Fehler: Hindert das Programm bei einer Aktion weiter fortzufahren
     */
    ERROR,

    /**
     * Loggt fatale Fehler: Das Programm muss beendet werden, da eine notwendige Aktion nicht
     * abgeschlossen werden kann
     */
    FATAL,

}
