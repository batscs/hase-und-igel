package gui.data;

import java.io.File;

/**
 * Klasse zum Verwalten von Dateipfaden.
 *
 * @author github.com/batscs
 */
public enum Filepath {

    DIRECTORY_APPDATA(
            String.join(File.separator, System.getProperty("user.home"), ".HaseUndIgel_Cwik")),
    FILE_LANGUAGE_IMPORTED(
            String.join(File.separator, DIRECTORY_APPDATA.toString(), "user", "language.json")),
    FILE_LANGUAGE_DEFAULT("gui/language/de_DE.json"),
    FILE_APPLICATION_ICON("/gui/img/Icon.png"),
    FILE_APPLICATION_LOG("log.txt"),
    FILE_GAME_COORDINATES("/gui/data/Feldmittelpunkte.json");

    /**
     * Attribut für den Dateipfad des zugehörigen Enums.
     */
    private final String path;

    /**
     * Konstruktor zum Initialisieren eines Enums zusammen mit einem Dateipfad.
     *
     * @param path Der Dateipfad
     */
    Filepath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
