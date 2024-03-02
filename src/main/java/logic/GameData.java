package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import logic.data.Validation;
import util.log.Log;
import util.log.LogLevel;
import util.log.LogModule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Klasse welche alle notwendigen Informationen eines laufenden Spieles beinhaltet.
 *
 * @author github.com/batscs
 */
public class GameData {

    /**
     * Attribut zur Definierung welcher Spieler aktuell am Zug ist.
     * (Sonst überall 'protagonist', hier aber currPlayer damit passend zu Moodle-Vorgaben)
     */
    @Expose
    private int currPlayer;

    /**
     * Attribut für alle Spieler welche auf dem Ziel sind.
     * Werte entsprechend dem Index den jeweiligen Spieler im players Array.
     */
    @Expose
    private List<Integer> onTarget;


    /**
     * Attribut für alle teilnehmenden Spieler
     */
    @Expose
    private Player[] players;

    /**
     * Attribut für maximale Anzahl von teilnehmenden Spielern
     */
    public static final int GAME_MAX_PLAYERS = 6;


    /**
     * Konstruktor zum Erstellen des Spielstandes durch bereits bekannte Attribute.
     *
     * @param players Die teilnehmenden Spieler
     * @param protagonist Der aktuelle Spieler, welcher am Zug ist
     * @param onTarget Die Spieler welche bereits auf dem Zielfeld sind
     */
    public GameData(Player[] players, int protagonist, List<Integer> onTarget) {
        this.players = players;
        this.currPlayer = protagonist;
        this.onTarget = onTarget;
    }

    /**
     * Gibt das Array der teilnehmenden Spieler zurück.
     *
     * @return die teilnehmenden Spieler
     */
    public Player[] getPlayers() {
        Player[] copy = new Player[players.length];

        for (int i = 0; i < players.length; i++) {
            copy[i] = players[i].copy();
        }

        return copy;
    }

    /**
     * Gibt den Index des aktuellen Spielers, der am Zug ist, zurück.
     *
     * @return Der Index des aktuellen Spielers.
     */
    public int getProtagonist() {
        return currPlayer;
    }

    /**
     * Gibt die Anzahl der teilnehmenden Spieler zurück.
     *
     * @return Die Anzahl der teilnehmenden Spieler.
     */
    public int getParticipants() {
        return players.length;
    }

    /**
     * Gibt die aktuelle Liste von Spieler-Indizes, welche sich im Ziel befinden. Aufsteigend
     * sortiert nach der Reihenfolge, in welcher diese das Ziel betreten haben.
     *
     * @return die Liste
     */
    public List<Integer> getOnTarget() {
        // deep copy für public-getter
        return new LinkedList<>(onTarget);
    }

    /**
     * Methode zum Validieren, ob die Attribute der Spielkonfiguration Regelkonform sind. Wird
     * genutzt beim Laden eines Spielstandes.
     *
     * @return null, falls es Fehler beim validieren gab.
     */
    public static GameData validate(GameData config) {

        if (config.players == null || config.players.length < 2 || config.players.length > GAME_MAX_PLAYERS) {
            Log.write(LogLevel.ERROR, LogModule.GAME, "Invalid GameData.players, none or less than two players");
            return null;
        }

        if (config.onTarget == null || config.onTarget.size() > config.players.length) {
            Log.write(LogLevel.ERROR, LogModule.GAME, "Invalid GameData.onTarget, too many players on final field or null");
            return null;
        }

        if (config.currPlayer < 0 || config.currPlayer >= config.players.length) {
            Log.write(LogLevel.ERROR, LogModule.GAME, "Invalid GameData.currPlayer, protagonist id is not valid");
            return null;
        }

        HashSet<Integer> positions = new HashSet<>();

        for (Player player : config.players) {
            if (player.validate() != Validation.VALID) {
                Log.write(LogLevel.ERROR, LogModule.GAME, "Invalid GameData.players, found invalid player");
                return null;
            }

            int playerPos = player.getPosition();
            if (playerPos != 0 && playerPos != GameLogic.FINAL_FIELD_POSITION) {
                if (!positions.contains(player.getPosition())) {
                    positions.add(playerPos);
                } else {
                    Log.write(LogLevel.ERROR, LogModule.GAME,
                            "Invalid GameData.players, two players on one position");
                    return null;
                }
            }
        }

        for (int finished : config.onTarget) {

            if (finished >= config.players.length) {
                return null;
            }

            Player target = config.players[finished];

            if (target.getPosition() != GameLogic.FINAL_FIELD_POSITION) {
                Log.write(LogLevel.ERROR, LogModule.GAME, "Invalid GameData.onTarget, Position of player on final field is not corresponding with final field index");
                return null;
            }

            if (target.getSalads() > 0) {
                Log.write(LogLevel.ERROR, LogModule.GAME, "Invalid GameData.onTarget, Player on final field has too many salads");
                return null;
            }

            if (target.getCarrots() > 10) {
                Log.write(LogLevel.ERROR, LogModule.GAME, "Invalid GameData.onTarget, Player on final field has too many carrots");
                return null;
            }
        }

        boolean duplicateName = false;
        for (int i = 0; i < config.players.length && !duplicateName; i++) {
            for (int j = i + 1; j < config.players.length; j++) {
                String name_A = config.players[i].getName();
                String name_B = config.players[j].getName();
                if (name_A.equals(name_B)) {
                    duplicateName = true;
                }
            }
        }

        if (duplicateName) {
            Log.write(LogLevel.ERROR, LogModule.GAME, "Invalid GameData.players, two or more players have the same name");
            return null;
        }

        Log.write(LogLevel.INFO, LogModule.GAME, "GameData validated successfully");
        return config;
    }

    /**
     * Methode zum Validieren, ob die Attribute der Spielkonfiguration Regelkonform sind. Wird
     * genutzt beim Laden eines Spielstandes.
     *
     * @return null, falls es Fehler beim validieren gab.
     */
    public static GameData validate(File file) {

        GameData config;
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation()
                .create();

        try {
            String fileContent = Files.readString(Path.of(file.getAbsolutePath()));
            config = gson.fromJson(fileContent, GameData.class);
            Log.write(LogLevel.INFO, LogModule.APPLICATION, "Successfully loaded ");
        } catch (IOException e) {
            Log.write(LogLevel.ERROR, LogModule.APPLICATION,
                    "Could not read content from Game-file while parsing");
            return null;
        } catch (JsonSyntaxException e) {
            Log.write(LogLevel.ERROR, LogModule.APPLICATION,
                    "Could not parse json-Game-file, invalid syntax");
            return null;
        }

        return validate(config);

    }

}
