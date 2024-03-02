package util.log;

import gui.data.Filepath;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Klasse zum Aufzeichnen von Ereignissen in einer log.txt-Datei zur Laufzeit des Programmes. Für
 * Debug-Zwecke.
 *
 * @author github.com/batscs
 */
public class Log {

    /**
     * Attribut, zur Umwandlung in das gewünschte Zeitformat als ISO8601-Standard.
     */
    private static final DateFormat TIMESTAMP_ISO8601 =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    /**
     * Attribut, dass die Datei initialisiert wurde und nun Ereignisse protokolliert (geloggt)
     * werden sollen.
     */
    private static boolean started = false;

    /**
     * Attribut, dass nicht in die Datei geschrieben werden kann. Falls true wird nicht weiter
     * versucht neue logs zu protokollieren.
     */
    private static boolean denied = false;

    /**
     * Methode säubert dem vorherigen Log, falls vorhanden. Wird am Anfang des Programms, sowie beim
     * Starten eines neuen Spiels ausgeführt.
     */
    public static void start() {

        try {
            File f = new File(Filepath.FILE_APPLICATION_LOG.toString());

            if (f.exists()) {
                if (!f.delete() || !f.createNewFile()) {
                    denied = true;
                }
            }

            started = true;
        } catch (IOException e) {
            denied = true;
        }
    }

    /**
     * Methode zum Aufzeichnen eines Ereignisses in einem festen Format.
     * <p>
     * Format: TIMESTAMP LOGLEVEL - [MODULE] MESSAGE
     *
     * @param msg Beschreibung des Ereignisses
     */
    public static void write(LogLevel level, LogModule module, String msg) {

        if (!started || denied) {
            return;
        }

        String timestamp = TIMESTAMP_ISO8601.format(new Date());

        try (Writer writer = new BufferedWriter(
                new FileWriter(Filepath.FILE_APPLICATION_LOG.toString(), true))) {
            String content = String.format("%s %s - [%s] %s", timestamp, level, module, msg);
            writer.append(content).append("\n");
        } catch (IOException e) {
            denied = true;
        }
    }

}
