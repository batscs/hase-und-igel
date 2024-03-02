package gui.language;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import gui.data.Filepath;
import gui.data.GUIValidation;
import logic.Token;
import logic.data.HaseUndIgelException;
import util.log.Log;
import util.log.LogLevel;
import util.log.LogModule;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Implementierung des Translator Interfaces, welches Tokens aus dem Back-End ins Front-End zu einer
 * entsprechenden Sprache übersetzt.
 *
 * @author github.com/batscs
 */
public class JSONTranslator implements Translator{

    /**
     * "TypeToken is where the magic happens. It's used to tell Gson what exactly you want your string to get converted to."
     */
    private static final Type mapType = new TypeToken<EnumMap<Token, String>>() {}.getType();

    /**
     * Map für die Tokens und dem korrespondierendem Text für das Front-End
     */
    private Map<Token, String> translator;

    /**
     * Konstruktor zum Initialisieren
     */
    public JSONTranslator() {
        Log.write(LogLevel.INFO, LogModule.TRANSLATOR, "Initializing JSON-Translator Object");
        refresh();
    }

    /**
     * Überprüft eine Datei, ob diese eine gültige Sprachdatei ist welche verwendet werden kann.
     * Static Methode, da Überprüfung immer gleich und unabhängig von einer Instanz abläuft.
     *
     * @param file Pfad der Sprachdatei
     * @return VALID, falls gültig, sonst entsprechender Fehlerwert.
     */
    public static GUIValidation validate(String file) {

        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        Path path = Paths.get(file);;
        Map<Token, String> language;

        try {
            String fileContent = Files.readString(path);
            language = gson.fromJson(fileContent, mapType);
        } catch (IOException e) {
            Log.write(LogLevel.ERROR, LogModule.TRANSLATOR, "Language validation failed, could not read file");
            Log.write(LogLevel.DEBUG, LogModule.TRANSLATOR, "Validated language location " + file);
            return GUIValidation.INVALID_CUSTOM_LANGUAGE_FILE_READ;
        } catch (JsonSyntaxException | NullPointerException e) {
            Log.write(LogLevel.ERROR, LogModule.TRANSLATOR, "Language validation failed, invalid json syntax");
            Log.write(LogLevel.DEBUG, LogModule.TRANSLATOR, "Validated language location " + file);
            return GUIValidation.INVALID_CUSTOM_LANGUAGE_FILE_GSON;
        }

        if (Token.values().length != language.size()) {
            Log.write(LogLevel.ERROR, LogModule.TRANSLATOR, "Language validation failed, not all tokens translated");
            Log.write(LogLevel.DEBUG, LogModule.TRANSLATOR, "Validated language location " + file);
            return GUIValidation.INVALID_CUSTOM_LANGUAGE_FILE_SIZE;
        }

        Log.write(LogLevel.INFO, LogModule.TRANSLATOR, "Language validated successfully");
        return GUIValidation.VALID;
    }

    @Override
    public String translate(Token msg) {

        if (translator.containsKey(msg)) {
            return translator.get(msg);
        }

        return "#" + msg.toString(); // Alternativ zu "???"
    }

    @Override
    public void refresh() {

        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation()
                .create();

        // Dateipfade für beide Möglichen Language Files
        String local = Filepath.FILE_LANGUAGE_DEFAULT.toString();
        String custom = Filepath.FILE_LANGUAGE_IMPORTED.toString();
        boolean customFileIsValid = true;
        File customLanguageFile = new File(custom);

        // Auswertung, ob es eine gültige benutzerdefinierte Language File gibt, sonst wird default benutzt
        if (customLanguageFile.exists() && validate(custom) != GUIValidation.VALID) {
            Log.write(LogLevel.ERROR, LogModule.TRANSLATOR, "Custom language file is invalid, located at: " + custom);
            customFileIsValid = false;
        } else if (!customLanguageFile.exists()) {
            Log.write(LogLevel.INFO, LogModule.TRANSLATOR, "Using default language file because no customized file found");
            customFileIsValid = false;
        }

        if (!customFileIsValid) {
            try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream(local)) {
                Reader reader = new InputStreamReader(Objects.requireNonNull(stream));
                translator = gson.fromJson(reader, mapType);
                reader.close();
            } catch (IOException | NullPointerException e) {
                Log.write(LogLevel.FATAL, LogModule.TRANSLATOR,
                        "Default language file could not be used for the translator");
                Log.write(LogLevel.DEBUG, LogModule.TRANSLATOR,
                        "Error message for default language file: " + e.getMessage());
                throw new HaseUndIgelException(
                        "The default language file is faulty. Can not launch Program.");
            }
        } else {
            try (Reader reader = new BufferedReader(new FileReader(custom))) {
                translator = gson.fromJson(reader, mapType);
            } catch (IOException | JsonSyntaxException e) {
                // Das hier sollte eigentlich nie nie nie nie nie nie niemals auftreten,
                // da das hier bei validate(custom) abgefangen wird, und die Default file verwendet wird.
                Log.write(LogLevel.FATAL, LogModule.TRANSLATOR,
                        "Custom Language File could not be used.");
                throw new HaseUndIgelException(
                        "The default language file is faulty. Can not launch Program.");
            }
        }

    }

}
