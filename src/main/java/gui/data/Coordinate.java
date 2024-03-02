package gui.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import logic.data.HaseUndIgelException;
import util.log.Log;
import util.log.LogLevel;
import util.log.LogModule;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * Klasse zum Arbeiten mit den eingelesenen Koordinaten.
 *
 * @author github.com/batscs
 */
public class Coordinate {

    /**
     * Attribut mit allen x- und y-Koordinaten der Felder auf der Oberfläche.
     * Erste Dimension ist der Index des Feldes, zweites Array enthält bei index = 0 die x-Koordinate
     * und bei index = 1 die y-Koordinate
     */
    private static int[][] coords;

    /**
     * Methode zum Laden der Informationen aus der json-Datei in das Koordinaten-Attribut.
     */
    private static void load() {

        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String fileName = Filepath.FILE_GAME_COORDINATES.toString();
        try (InputStreamReader reader = new InputStreamReader(
                Objects.requireNonNull(Coordinate.class.getResourceAsStream(fileName)))) {
            coords = gson.fromJson(reader, int[][].class);
            Log.write(LogLevel.INFO, LogModule.APPLICATION, "Coordinates loaded successfully from local file");
        } catch (IOException | JsonParseException e) {
            Log.write(LogLevel.FATAL, LogModule.APPLICATION, "Application forcefully shutting down | Could not load Coordinates from " + fileName);
            Log.write(LogLevel.DEBUG, LogModule.APPLICATION, "Coordinate exception: " + e.getMessage());
            throw new HaseUndIgelException("Could not fetch Data for Coordinates on the GUI");
        }

    }

    /**
     * Methode zum Ermitteln der Koordinaten eines bestimmten Feldindexes. Methode lädt die
     * Informationen in das Koordinaten-Attribut, falls noch nicht geschehen.
     *
     * @param idx Der Index des Feldes
     * @return Array mit 2 Elementen, x- und y-Koordinate
     */
    public static int[] get(int idx) {
        if (coords == null)
            load();

        if (idx < 0 || idx >= coords.length) {
            Log.write(LogLevel.ERROR, LogModule.APPLICATION,
                    "Tried to access invalid coordinate for field = " + idx);
            return new int[] { 0, 0 };
        }

        return coords[idx];
    }

}
