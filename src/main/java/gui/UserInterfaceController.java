package gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gui.data.Error;
import gui.data.*;
import gui.language.JSONTranslator;
import gui.language.Translator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import logic.*;
import util.log.Log;
import util.log.LogLevel;
import util.log.LogModule;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

/**
 * Main class for the user interface.
 *
 * @author github.com/batscs
 */
public class UserInterfaceController implements Initializable {

    @FXML private Menu menuHeaderFile;
    @FXML private MenuItem menuItemStart;
    @FXML private MenuItem menuItemNew;
    @FXML private MenuItem menuItemSave;
    @FXML private MenuItem menuItemLoad;
    @FXML private MenuItem menuItemExit;
    @FXML private Menu menuHeaderView;
    @FXML private MenuItem menuItemLangImport;
    @FXML private MenuItem menuItemLangReset;
    @FXML private Menu menuHeaderHelp;
    @FXML private MenuItem menuItemAbout;
    @FXML private MenuItem menuItemGameOverview;
    @FXML private VBox vbxConfiguration;
    @FXML private VBox vbxStats;
    @FXML private ImageView imgP1, imgP2, imgP3, imgP4, imgP5, imgP6;
    @FXML private Pane paneInteractiveContainer;
    @FXML private Pane paneGameContainer;
    @FXML
    private Button btnField00, btnField01, btnField02, btnField03, btnField04, btnField05, btnField06, btnField07, btnField08, btnField09, btnField10,
            btnField11, btnField12, btnField13, btnField14, btnField15, btnField16, btnField17, btnField18, btnField19, btnField20,
            btnField21, btnField22, btnField23, btnField24, btnField25, btnField26, btnField27, btnField28, btnField29, btnField30,
            btnField31, btnField32, btnField33, btnField34, btnField35, btnField36, btnField37, btnField38, btnField39, btnField40,
            btnField41, btnField42, btnField43, btnField44, btnField45, btnField46, btnField47, btnField48, btnField49, btnField50,
            btnField51, btnField52, btnField53, btnField54, btnField55, btnField56, btnField57, btnField58, btnField59, btnField60,
            btnField61, btnField62, btnField63, btnField64;
    @FXML private Label lblPlayerTurn;
    @FXML private Label lblPlayerCarrots;
    @FXML private Label lblPlayerSalads;
    @FXML private Label lblPlayercount;
    @FXML private Label lblPlayernames;
    @FXML private Slider sldrPlayerCount;
    @FXML private Label lblPlayerCountValue;
    @FXML private VBox vbxWinMessage;

    @FXML
    private ImageView imgBoard;
    @FXML
    private TextField inptUsername1;
    @FXML
    private TextField inptUsername2;
    @FXML
    private TextField inptUsername3;
    @FXML
    private TextField inptUsername4;
    @FXML
    private TextField inptUsername5;
    @FXML
    private TextField inptUsername6;
    @FXML
    private Button btnStart;

    private File currDir;

    /**
     * Konstante der Mindestanzahl an Spielern.
     */
    public static final int MIN_PLAYERS = 2;
    /**
     * Konstante der Maximalanzahl an Spielern.
     */
    public static final int MAX_PLAYERS = 6;
    /**
     * Attribut für die Spielernamen.
     */
    private String[] usernames;
    /**
     * Attribut für die SpielLogik.
     */
    private GameLogic game;
    /**
     * Attribut für die aktuelle Anzahl an teilnehmenden Spielern.
     */
    private int amountPlayers = 2;

    /**
     * Zustand der Applikation
     */
    private GameState gamestate;

    /**
     * Referenz zur Oberfläche
     */
    private GUIConnector gui;

    /**
     * Referenz zum Übersetzer der Oberflächentexte
     */
    private Translator translator;

    /**
     * Textfelder der Spielernamen
     */
    private TextField[] inputs;

    /**
     * Bilder der Spielsteine
     */
    private ImageView[] icons;

    /**
     * Buttons für die Spielfelder
     */
    private Button[] buttons;

    /**
     * Initialisiert das UserInterface.
     *
     * @param location  not used
     * @param resources not used
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Log.start();

        Log.write(LogLevel.INFO, LogModule.APPLICATION, "********************************************************************");
        Log.write(LogLevel.INFO, LogModule.APPLICATION, "Launching " + Specification.APPLICATION_TITLE);
        Log.write(LogLevel.INFO, LogModule.APPLICATION,
                "Application launched with version: " + Specification.APPLICATION_VERSION);
        Log.write(LogLevel.INFO, LogModule.APPLICATION, "********************************************************************");

        gamestate = GameState.MENU;

        // Default Focus soll auf Text-Inputs sein
        // So kann man jetzt auch mit Tab-Taste durch alle Text-Inputs sich bewegen und danach
        // zum Startbutton
        sldrPlayerCount.setFocusTraversable(false);

        updateFrontEndAccessibility();

        translator = new JSONTranslator();

        paneInteractiveContainer.setVisible(false);

        this.icons = new ImageView[] {
                imgP1,
                imgP2,
                imgP3,
                imgP4,
                imgP5,
                imgP6
        };

        this.usernames = new String[] {
                "Anton",
                "Berta",
                "Cäsar",
                "Dora",
                "Emil",
                "Frederic"
        };

        this.inputs = new TextField[] {
                inptUsername1,
                inptUsername2,
                inptUsername3,
                inptUsername4,
                inptUsername5,
                inptUsername6
        };

        imgP1.setVisible(false);
        imgP2.setVisible(false);
        imgP3.setVisible(false);
        imgP4.setVisible(false);
        imgP5.setVisible(false);
        imgP6.setVisible(false);

        imgBoard.setPreserveRatio(false);

        imgBoardApplyFilter(true);

        paneGameContainer.widthProperty().addListener((observableValue, number, t1) -> {
            imgBoard.setFitWidth(paneGameContainer.getWidth());

            for (ImageView icon : icons) {
                icon.setPreserveRatio(false);
                icon.setFitWidth(imgBoard.getFitWidth() * 0.06);
            }

            for (Button btn : buttons) {
                btn.setPrefWidth(imgBoard.getFitWidth() * 0.06);
            }

        });

        paneGameContainer.heightProperty().addListener((observableValue, number, t1) -> {
            imgBoard.setFitHeight(paneGameContainer.getHeight());

            for (ImageView icon : icons) {
                icon.setPreserveRatio(false);
                icon.setFitHeight(imgBoard.getFitHeight() * 0.08);
            }

            for (Button btn : buttons) {
                btn.setPrefHeight(imgBoard.getFitHeight() * 0.08);
            }
        });

        this.buttons = new Button[] {
                btnField00,
                btnField01,
                btnField02,
                btnField03,
                btnField04,
                btnField05,
                btnField06,
                btnField07,
                btnField08,
                btnField09,
                btnField10,
                btnField11,
                btnField12,
                btnField13,
                btnField14,
                btnField15,
                btnField16,
                btnField17,
                btnField18,
                btnField19,
                btnField20,
                btnField21,
                btnField22,
                btnField23,
                btnField24,
                btnField25,
                btnField26,
                btnField27,
                btnField28,
                btnField29,
                btnField30,
                btnField31,
                btnField32,
                btnField33,
                btnField34,
                btnField35,
                btnField36,
                btnField37,
                btnField38,
                btnField39,
                btnField40,
                btnField41,
                btnField42,
                btnField43,
                btnField44,
                btnField45,
                btnField46,
                btnField47,
                btnField48,
                btnField49,
                btnField50,
                btnField51,
                btnField52,
                btnField53,
                btnField54,
                btnField55,
                btnField56,
                btnField57,
                btnField58,
                btnField59,
                btnField60,
                btnField61,
                btnField62,
                btnField63,
                btnField64,
        };

        gui = new JavaFXGUI(translator, buttons, icons, paneGameContainer, menuItemSave,
                lblPlayerTurn, lblPlayerCarrots, lblPlayerSalads);

        initializeUsernameFields();

        sldrPlayerCount.setMax(MAX_PLAYERS);
        sldrPlayerCount.setMin(MIN_PLAYERS);

        sldrPlayerCount.setSnapToTicks(true);
        sldrPlayerCount.setMajorTickUnit(1);
        sldrPlayerCount.setMinorTickCount(0);
        sldrPlayerCount.setShowTickMarks(true);
        sldrPlayerCount.setShowTickLabels(true);

        sldrPlayerCount.valueProperty().addListener((observableValue, number, t1) -> {
            setPlayerCount((int) Math.round(sldrPlayerCount.getValue()));
        });

        // Log.write hier hin verschoben, da die valueProperty den Listener bei jeder minimalen Bewegung aufruft
        sldrPlayerCount.focusedProperty().addListener((observableValue, unfocused, t1) -> {
            if (unfocused) {
                Log.write(LogLevel.INFO, LogModule.USER, "Attempting to change player count to " + sldrPlayerCount.getValue());
            }
        });

        setPlayerCount((int) sldrPlayerCount.getValue());

        updateFrontEndLanguage();

        currDir = null;
        try {
            currDir = new File(UserInterfaceController.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI());
        } catch (URISyntaxException ex) {
            // oops... ¯\_(ツ)_/¯
            // Kein FileChooser wird im Verzeichnis der .jar geöffnet werden können :(
            Log.write(LogLevel.ERROR, LogModule.APPLICATION,
                    "Could not locate current working Dir.");
        }

        Log.write(LogLevel.INFO, LogModule.APPLICATION, "Finished Initializing");
        Log.write(LogLevel.INFO, LogModule.APPLICATION,
                "********************************************************************");

    }

    /**
     * Initialisiert die Textfelder für die Spielernamen und setzt die Werte auf ihre
     * Standardwerte.
     */
    private void initializeUsernameFields() {

        for (int i = 0; i < usernames.length && i < inputs.length; i++) {
            inputs[i].setText(usernames[i]);

            int finalI = i;
            inputs[i].focusedProperty().addListener((observableValue, aBoolean, t1) -> {
                boolean unhighlight = aBoolean;

                if (unhighlight && inputs[finalI].getText().isEmpty()) {
                    inputs[finalI].setText(usernames[finalI]);
                } else {
                    usernames[finalI] = inputs[finalI].getText();
                    usernamesContainDuplicates(); // Falls Duplikat, beide rot markieren
                }

            });
        }



    }

    /**
     * Setzt die Anzahl der Spieler fest.
     *
     * @param amount Die Anzahl der Spieler
     */
    private void setPlayerCount(int amount) {

        // Schrift für die Anzahl der teilnehmenden Spieler
        lblPlayerCountValue.setText(String.valueOf(amount));

        // Durch SpinnerValueFactory wird sichergestellt MAX_PLAYERS nie zu überschreiten
        amountPlayers = amount;

        // Bis zu amount werden alle visible = true
        for (int i = 0; i < amount && i < MAX_PLAYERS; i++) {
            inputs[i].setVisible(true);
        }

        // Ab amount bis zum Ende werden alle visible = false
        for (int i = amount; i < MAX_PLAYERS; i++) {
            inputs[i].setVisible(false);
        }

    }

    /**
     * Methode zum Anwenden eines Filters auf das Spielfeld, wenn dieses inaktiv ist.
     *
     * @param apply true, falls Filter angewandt werden soll.
     */
    private void imgBoardApplyFilter(boolean apply) {
        ColorAdjust effect = new ColorAdjust();

        if (apply) {
            effect.setSaturation(-1);
        }

        imgBoard.setEffect(effect);
    }

    /**
     * Ermittel die Spielernamen der teilnehmenden Spieler
     *
     * @return Array mit den Spielernamen
     */
    private String[] getActiveUsernames() {
        String[] activePlayers = new String[amountPlayers];
        System.arraycopy(usernames, 0, activePlayers, 0, amountPlayers);
        return activePlayers;
    }

    /**
     * Modularisierung vom Starten eines Spiels
     */
    private void prepareStartGame() {

        Log.write(LogLevel.INFO, LogModule.APPLICATION, "Preparing to start game");

        gamestate = GameState.IN_GAME;
        updateFrontEndAccessibility();

    }

    /**
     * Aktualisiert die Oberflächentexte und übersetzt die entsprechend.
     */
    private void updateFrontEndLanguage() {
        Log.write(LogLevel.INFO, LogModule.APPLICATION, "Front-End Language is being reloaded");

        translator.refresh();

        btnStart.setText(translator.translate(Token.GUI_BUTTON_START));

        menuHeaderFile.setText(translator.translate(Token.GUI_MENU_HEADER_FILE));
        menuHeaderHelp.setText(translator.translate(Token.GUI_MENU_HEADER_HELP));
        menuHeaderView.setText(translator.translate(Token.GUI_MENU_HEADER_VIEW));

        menuItemNew.setText(translator.translate(Token.GUI_MENU_ITEM_NEW));
        menuItemExit.setText(translator.translate(Token.GUI_MENU_ITEM_EXIT));
        menuItemLoad.setText(translator.translate(Token.GUI_MENU_ITEM_LOAD));
        menuItemSave.setText(translator.translate(Token.GUI_MENU_ITEM_SAVE));
        menuItemStart.setText(translator.translate(Token.GUI_MENU_ITEM_START));

        menuItemGameOverview.setText(translator.translate(Token.GUI_MENU_ITEM_HELP));
        menuItemAbout.setText(translator.translate(Token.GUI_MENU_ITEM_ABOUT));

        menuItemLangImport.setText(translator.translate(Token.GUI_MENU_ITEM_LANGUAGE_IMPORT));
        menuItemLangReset.setText(translator.translate(Token.GUI_MENU_ITEM_LANGUAGE_RESET));

        lblPlayercount.setText(translator.translate(Token.GUI_LABEL_PLAYERCOUNT));
        lblPlayernames.setText(translator.translate(Token.GUI_LABEL_PLAYERNAMES));

        if (gamestate == GameState.IN_GAME) {
            game.redraw();
        }

    }

    /**
     * Aktualisiert die Verwendbarkeit von Elementen auf der Oberfläche anhand vom aktuellen
     * GameState.
     */
    private void updateFrontEndAccessibility() {
        menuItemStart.setDisable(gamestate == GameState.IN_GAME);
        menuItemNew.setDisable(gamestate == GameState.MENU);
        menuItemSave.setDisable(gamestate == GameState.MENU);
        menuItemLoad.setDisable(gamestate == GameState.IN_GAME);
        menuItemLangReset.setDisable(!customLanguageExists());

        btnStart.setVisible(gamestate == GameState.MENU);
        vbxStats.setVisible(gamestate == GameState.IN_GAME);
        vbxConfiguration.setVisible(gamestate == GameState.MENU);
        paneInteractiveContainer.setVisible(gamestate == GameState.IN_GAME);

        imgBoardApplyFilter(gamestate == GameState.MENU);

        vbxWinMessage.setVisible(false);
    }

    /**
     * Überprüft, ob mehrere Spieler einen identischen Spielernamen besitzen und markiert
     * diese farblich.
     *
     * @return true, falls doppelt vorkommende Namen vorhanden sind.
     */
    private boolean usernamesContainDuplicates() {
        String[] names = getActiveUsernames();
        boolean duplicates = false;

        // Alle Namen background default setzen
        for (int i = 0; i < names.length; i++) {
            inputs[i].setStyle("");
        }

        // Duplikate Farbig markieren
        for (int i = 0; i < names.length; i++) {
            for (int j = i + 1; j < names.length; j++) {
                if (names[i].equals(names[j])) {

                    // Indize von players (Klasse: Player) und inputs (Klasse: GUITextfield) sind korrespondierend.
                    // Background-Color wird auf Rot gesetzt (Ungültig)
                    String colorRed = "#F18C8E";
                    inputs[i].setStyle("-fx-background-color: " + colorRed);
                    inputs[j].setStyle("-fx-background-color: " + colorRed);

                    duplicates = true;
                }
            }
        }

        return duplicates;
    }

    /**
     * Ermittelt, ob eine benutzerdefinierte Sprachdatei existiert.
     *
     * @return true, falls existiert
     */
    private boolean customLanguageExists() {
        File f = new File(Filepath.FILE_LANGUAGE_IMPORTED.toString());

        return f.exists() && !f.isDirectory();
    }

    /**
     * Methode zum Starten eines Spieles, falls möglich.
     *
     * @param actionEvent actionEvent
     */
    @FXML
    private void onStartButtonClick(ActionEvent actionEvent) {

        Log.write(LogLevel.INFO, LogModule.USER, "Attempting to start game");

        if (usernamesContainDuplicates()) {
            gui.alertError(Error.ERROR_GAME_CANT_START_DUPLICATE_NAMES);
            Log.write(LogLevel.WARN, LogModule.APPLICATION,
                    "Failed to start game, two or more players have the same name!");
            return;
        }

        if (gamestate == GameState.MENU) {
            gui = new JavaFXGUI(translator, buttons, icons, paneGameContainer, menuItemSave,
                    lblPlayerTurn, lblPlayerCarrots, lblPlayerSalads);
            game = new GameLogic(gui, getActiveUsernames());
            prepareStartGame();
        }
    }

    /**
     * Methode zum Abbrechen eines laufenden Spiels.
     *
     * @param actionEvent actionEvent
     */
    @FXML
    private void onAbortButtonClick(ActionEvent actionEvent) {
        Log.write(LogLevel.INFO, LogModule.USER, "Attempting to create a new game");
        if (gamestate == GameState.IN_GAME) {

            gui.alertConfirm(Token.GAME_CONFIRMATION_ABORT_MATCH_AND_RESET, confirmed -> {

                if (confirmed) {
                    Log.write(LogLevel.WARN, LogModule.APPLICATION, "Current game has been terminated due to user request");
                    gamestate = GameState.MENU;

                    gui.gameStop();

                    updateFrontEndAccessibility();

                    game.forceGameEnd();

                    setPlayerCount(amountPlayers);
                }

            });

        }

    }

    /**
     * Methode zum Verlassen des Programmes.
     *
     * @param actionEvent actionEvent
     */
    @FXML
    private void onExitButtonClick(ActionEvent actionEvent) {
        Log.write(LogLevel.INFO, LogModule.USER, "Attempting to exit program");

        gui.alertConfirm(Token.GAME_CONFIRMATION_EXIT, confirmed -> {
            if (confirmed) {
                Log.write(LogLevel.INFO, LogModule.APPLICATION, "Exiting program");
                Stage stage = (Stage) paneGameContainer.getScene().getWindow();
                stage.close();
            } else {
                Log.write(LogLevel.INFO, LogModule.APPLICATION, "Program exit has been cancelled by user");
            }
        });

    }

    /**
     * Methode zum Speichern des Spielstandes vom aktuell laufenden Spiel.
     *
     * @param actionEvent actionEvent
     */
    @FXML
    private void onSaveButtonClick(ActionEvent actionEvent) {
        Log.write(LogLevel.INFO, LogModule.USER, "Attempting to save game to file");

        GameData config = game.getConfig();

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                translator.translate(Token.GUI_FILE_CHOOSER_EXTENSION_GAME_FILE) + " (*.json)",
                "*.json");

        fileChooser.setTitle(translator.translate(Token.GUI_MENU_ITEM_SAVE));
        fileChooser.getExtensionFilters().add(extFilter);

        if (currDir != null) {
            fileChooser.setInitialDirectory(currDir.getParentFile());
        }

        File file = fileChooser.showSaveDialog(paneGameContainer.getScene().getWindow());


        if (file == null) {
            Log.write(LogLevel.WARN, LogModule.APPLICATION, "File selection has been cancelled or could not read");
            gui.alertError(Error.ERROR_FILE_NO_FILE_SELECTED);
            return;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

        String json = gson.toJson(config);

        try (FileWriter writer = new FileWriter(file.getAbsolutePath())) {
            writer.write(json);
            Log.write(LogLevel.INFO, LogModule.APPLICATION, "Game has been successfully saved");
            Log.write(LogLevel.DEBUG, LogModule.APPLICATION, "Gamefile saved at " + file.getAbsolutePath());
        } catch (IOException e) {
            Log.write(LogLevel.ERROR, LogModule.APPLICATION, "Game file could not be written");
            Log.write(LogLevel.DEBUG, LogModule.APPLICATION, "Game file write error: " + e.getMessage());
            gui.alertError(Error.ERROR_FILE_IMPORT_WRITE);
        }


    }

    /**
     * Methode zum Laden eines Spielstands.
     *
     * @param actionEvent actionEvent
     */
    @FXML
    private void onLoadButtonClick(ActionEvent actionEvent) {
        Log.write(LogLevel.INFO, LogModule.USER, "Attempting to load game from file");

        // --------------------------------------------------------- File Chooser
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                translator.translate(Token.GUI_FILE_CHOOSER_EXTENSION_GAME_FILE) + " (*.json)",
                "*.json");

        fileChooser.getExtensionFilters().add(extFilter);
        if (currDir != null) {
            fileChooser.setInitialDirectory(currDir.getParentFile());
        }
        fileChooser.setInitialDirectory(null);
        fileChooser.setTitle(translator.translate(Token.GUI_MENU_ITEM_LOAD));

        File file = fileChooser.showOpenDialog(paneGameContainer.getScene().getWindow());

        if (file == null) {
            Log.write(LogLevel.WARN, LogModule.USER, "Game-file loading has been cancelled or could not read");
            gui.alertError(Error.ERROR_FILE_NO_FILE_SELECTED);
            return;
        } else {
            Log.write(LogLevel.DEBUG, LogModule.USER, "Game-file loading from path " + file.getAbsolutePath());
        }

        // --------------------------------------------------------- Laden der File mit GSON
        GameData config = GameData.validate(file);

        // --------------------------------------------------------- Validierung
        if (config == null) {
            Log.write(LogLevel.ERROR, LogModule.APPLICATION,
                    "Invalid Game-file Data, stopped loading");
            gui.alertError(Error.ERROR_FILE_IMPORT_GAME_INVALID_DATA);
            return;
        }

        // ---------------------------------------------------------- Confirm
        gui.alertConfirm(Token.GAME_CONFIRMATION_IMPORT, confirmed -> {
            if (confirmed) {
                Log.write(LogLevel.INFO, LogModule.USER, "Confirmed to import game");

                for (ImageView icon : icons) {
                    icon.setVisible(false);
                }

                Player[] importedPlayers = config.getPlayers();

                gui = new JavaFXGUI(translator, buttons, icons, paneGameContainer, menuItemSave,
                        lblPlayerTurn, lblPlayerCarrots, lblPlayerSalads);

                // Front-End die Usernames auf die importierten Namen setzen
                for (int i =
                        0; i < importedPlayers.length && i < usernames.length && i < inputs.length; i++) {
                    Player newPlayer = importedPlayers[i];
                    inputs[i].setText(newPlayer.getName());
                    usernames[i] = newPlayer.getName();
                }

                Log.write(LogLevel.INFO, LogModule.APPLICATION, "Game-file successfully loaded");
                game = new GameLogic(gui, config);
                prepareStartGame();
            } else {
                Log.write(LogLevel.INFO, LogModule.USER, "Cancelled to import game");
            }
        }, config.getParticipants());
    }

    /**
     * Methode für das Interagieren mit einem Feldbutton.
     *
     * @param actionEvent actionEvent
     */
    @FXML
    private void onFieldButtonClick(ActionEvent actionEvent) {

        // Index Ermitteln vom gedrückten Button
        int index = -1;
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].equals(actionEvent.getSource())) {
                index = i;
            }
        }

        if (index == -1) {
            Log.write(LogLevel.WARN, LogModule.APPLICATION,
                    "User tried to interact with an unknown field");
        } else {
            Log.write(LogLevel.INFO, LogModule.USER, "Attempting to move to position " + index);
            game.move(index);
        }

    }

    /**
     * Methode für das Interagieren mit dem Zielfeld.
     *
     * @param actionEvent actionEvent
     */
    @FXML
    private void onFinalFieldButtonClick(ActionEvent actionEvent) {
        Log.write(LogLevel.INFO, LogModule.USER, "Attempting to move to final field (goal)");
        game.attemptToFinish();
    }

    /**
     * Methode zum Importieren einer Sprachdatei.
     *
     * @param actionEvent actionEvent
     */
    @FXML
    private void onImportLanguage(ActionEvent actionEvent) {

        Log.write(LogLevel.INFO, LogModule.USER, "Attempting to import language");

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                translator.translate(Token.GUI_FILE_CHOOSER_EXTENSION_LANGUAGE_FILE) + " (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle(translator.translate(Token.GUI_MENU_ITEM_LANGUAGE_IMPORT));

        File imported = fileChooser.showOpenDialog(null);

        if (imported == null) {
            gui.alertError(Error.ERROR_FILE_NO_FILE_SELECTED);
            Log.write(LogLevel.WARN, LogModule.APPLICATION, "Loading custom language file has been cancelled or could not read");
            return;
        } else {
            Log.write(LogLevel.DEBUG, LogModule.APPLICATION, "Loading custom language file from path " + imported.getAbsolutePath());
        }

        GUIValidation code = JSONTranslator.validate(imported.getAbsolutePath());
        if (code == GUIValidation.INVALID_CUSTOM_LANGUAGE_FILE_READ) {
            gui.alertError(Error.ERROR_FILE_IMPORT_READ);
            return;
        } else if (code == GUIValidation.INVALID_CUSTOM_LANGUAGE_FILE_GSON) {
            gui.alertError(Error.ERROR_FILE_IMPORT_FILE_WRONG_JSON_FORMAT);
            return;
        } else if (code == GUIValidation.INVALID_CUSTOM_LANGUAGE_FILE_SIZE) {
            gui.alertError(Error.ERROR_FILE_IMPORT_LANGUAGE_INVALID_SIZE);
            return;
        } else if (code != GUIValidation.VALID) {
            gui.alertError(Error.ERROR_FILE_IMPORT_LANGUAGE_INVALID_DATA);
            return;
        }

        File destination = new File(Filepath.FILE_LANGUAGE_IMPORTED.toString());

        if (destination.mkdirs()) {
            Log.write(LogLevel.INFO, LogModule.APPLICATION, "Created destination directory for language file");
            Log.write(LogLevel.DEBUG, LogModule.APPLICATION, "Language file directory: " + destination.getAbsolutePath());
        }

        Log.write(LogLevel.INFO, LogModule.APPLICATION, "Language file has been imported");

        try {
            Path result = Files.copy(imported.toPath(), destination.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            Log.write(LogLevel.DEBUG, LogModule.APPLICATION,
                    "Language file written to " + result.toAbsolutePath());

            translator.refresh();
            updateFrontEndLanguage();
            updateFrontEndAccessibility();
            gui.alertInfo(Token.GUI_INFO_LANGUAGE_IMPORT,
                    Token.GUI_INFO_LANGUAGE_IMPORT_DESCRIPTION);
        } catch (IOException e) {
            Log.write(LogLevel.ERROR, LogModule.APPLICATION,
                    "Failed to copy language file to app-directory.");
            Log.write(LogLevel.DEBUG, LogModule.APPLICATION,
                    "Language file written to " + destination.getAbsolutePath());
        }



    }

    /**
     * Methode zum Zurücksetzen der Sprache auf der Oberfläche auf Werkeinstellung.
     *
     * @param actionEvent actionEvent
     */
    @FXML
    private void onResetLanguage(ActionEvent actionEvent) {
        Log.write(LogLevel.INFO, LogModule.USER, "Attempting to reset Language");

        if (customLanguageExists()) {
            String importedPath = Filepath.FILE_LANGUAGE_IMPORTED.toString();
            File f = new File(importedPath);

            try {
                Files.delete(f.toPath());

                Log.write(LogLevel.INFO, LogModule.APPLICATION,
                        "Language file has reset to default");
                gui.alertInfo(Token.GUI_INFO_LANGUAGE_RESET,
                        Token.GUI_INFO_LANGUAGE_RESET_DESCRIPTION);

                updateFrontEndLanguage();
                updateFrontEndAccessibility();
            } catch (IOException e) {
                Log.write(LogLevel.ERROR, LogModule.APPLICATION,
                        "Failed to delete language file in app-directory.");
                Log.write(LogLevel.DEBUG, LogModule.APPLICATION,
                        "Language file written to " + f.getAbsolutePath());
            }

        } else {
            Log.write(LogLevel.WARN, LogModule.APPLICATION, "Could not reset language, already defaulted");
        }


    }

    /**
     * Methode zum Anzeigen von Informationen über den Ursprung des Programmes.
     *
     * @param actionEvent actionEvent
     */
    @FXML
    private void onInformationButtonClick(ActionEvent actionEvent) {
        Log.write(LogLevel.INFO, LogModule.USER, "Attempting to access information");

        gui.alertInfo(Token.GUI_ALERT_INFO_TITLE, Token.GUI_ALERT_INFO_CONTENT);
    }

    /**
     * Methode zum Anzeigen von Spezifikation über das Program, wie Version und Author.
     *
     * @param actionEvent actionEvent
     */
    @FXML
    private void onAboutButtonClick(ActionEvent actionEvent) {
        Log.write(LogLevel.INFO, LogModule.USER, "Attempting to access 'about application'");

        gui.alertInfo(Token.GUI_ALERT_ABOUT_TITLE, Token.GUI_ALERT_ABOUT_CONTENT,
                Specification.APPLICATION_TITLE, Specification.APPLICATION_VERSION,
                Specification.APPLICATION_AUTHOR, Specification.APPLICATION_VERSION_PUBLISHED);
    }


}
