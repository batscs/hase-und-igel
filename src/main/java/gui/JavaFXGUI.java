package gui;

import gui.data.Coordinate;
import gui.data.Error;
import gui.language.Translator;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.GUIConnector;
import logic.GameLogic;
import logic.Player;
import logic.Token;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Klasse, welche die GUIConnector Schnittstelle für die JavaFX Oberfläche implementiert.
 *
 * @author github.com/batscs
 */
public class JavaFXGUI implements GUIConnector {

    /**
     * Attribut für das Spielfeld
     */
    final private Pane gamePane;

    /**
     * Attribute für die Schriften des aktuellen Spielers
     */
    final private Label lblPlayerTurn, lblPlayerCarrots, lblPlayerSalads;

    /**
     * Attribut für den Knopf zum Speichern
     */
    final private MenuItem menuItemSave;

    /**
     * Attribut für die ganzen Buttons der Felder
     */
    final private Button[] buttons;

    /**
     * Attribute für die Spielsteine der Spieler
     */
    final private ImageView[] icons;

    /**
     * Attribute für die bestimmten Farben der Spieler
     */
    final private Color[] colors;

    /**
     * Attribut des Übersetzers
     */
    private final Translator translator;

    /**
     * Attribut für die Spieler auf dem Zielfeld. Reihenfolge entspricht der Reihenfolge des
     * Betretens des Zielfeldes.
     */
    List<Integer> onTarget = new LinkedList<>();

    /**
     * Falls eine Karte das Spiel unterbricht durch eine Anzeige wird das hier auf True gesetzt,
     * damit nach der Animation der Spieler-Bewegung das spiel nicht zwanghaft entblockt wird.
     */
    private boolean gameBlocked;

    /**
     * Konstruktor zum Initialisieren.
     *
     * @param translator       Übersetzer der Oberflächentexte
     * @param buttons          Buttons der Spielfelder
     * @param icons            Spielsteine der Spieler
     * @param gamePane         Spielbereich
     * @param menuItemSave     Knopf zum Speichern
     * @param lblPlayerTurn    Label des aktuellen Spielers
     * @param lblPlayerCarrots Label der Anzahl der Karotten des aktuellen Spielers
     * @param lblPlayerSalads  Label der Anzahl der Salate des aktuellen Spielers
     */
    public JavaFXGUI(Translator translator, Button[] buttons, ImageView[] icons, Pane gamePane,
            MenuItem menuItemSave, Label lblPlayerTurn, Label lblPlayerCarrots,
            Label lblPlayerSalads) {
        this.translator = translator;
        this.buttons = buttons;
        this.icons = icons;
        this.gamePane = gamePane;
        this.menuItemSave = menuItemSave;
        this.lblPlayerTurn = lblPlayerTurn;
        this.lblPlayerCarrots = lblPlayerCarrots;
        this.lblPlayerSalads = lblPlayerSalads;

        colors = new Color[icons.length]; // Gleich viele Farben wie Spieler!
        colors[0] = Color.web("#19619C");
        colors[1] = Color.web("#DECD1B");
        colors[2] = Color.web("#519F17");
        colors[3] = Color.web("#DF5922");
        colors[4] = Color.web("#B42A22");
        colors[5] = Color.web("#AC9E79");

        for (int i = 0; i < buttons.length; i++) {
            initializeField(i);
        }
    }

    /**
     * Initialisiert ein Spielfeld
     *
     * @param fieldIdx Index des Spielfeldes.
     */
    private void initializeField(int fieldIdx) {

        Button button = buttons[fieldIdx];
        button.setText("");

        button.setTextFill(Color.BLACK);
        button.setOpacity(0);

        if (fieldIdx != 0) {
            button.setCursor(Cursor.HAND);
        }

        // Sicherstellung, dass der Button (die "Hitbox") eines Feldes beim resizen erhalten bleibt.
        button.widthProperty().addListener((observableValue, number, t1) -> alignField(fieldIdx));
        button.heightProperty().addListener((observableValue, number, t1) -> alignField(fieldIdx));

        // Positioniert den Button auf der entsprechenden Position auf der Oberfläche.
        alignField(fieldIdx);

        // Da das Zielfeld sowie das Startfeld (entsprechend letzter und erster Index) NICHT aufleuchten soll beim hovern
        if (!(button == buttons[buttons.length - 1] || button == buttons[0])) {
            button.hoverProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    float opacity = t1 ? 0.4f : 0;
                    button.setOpacity(opacity);
                }
            });
        }

    }

    /**
     * Methode zum Positionieren eines bestimmten Spielfeldes auf der Oberfläche.
     *
     * @param fieldIdx Index des Spielfeldes
     */
    private void alignField(int fieldIdx) {

        Button button = buttons[fieldIdx];
        Pane board = gamePane;

        // 1 / Original Resolution
        float ASPECT_RATIO_WIDTH = 1f / 1000f;
        float ASPECT_RATIO_HEIGHT = 1f / 750f;

        float FIELDSIZE = 60;

        button.setPrefWidth(FIELDSIZE * board.getWidth() * ASPECT_RATIO_WIDTH);
        button.setPrefHeight(FIELDSIZE * board.getHeight() * ASPECT_RATIO_HEIGHT);

        // Offset berechnen erst nach Resize
        int offsetX = (int) (button.getWidth() / 2);
        int offsetY = (int) (button.getHeight() / 2);

        int originalX = Coordinate.get(fieldIdx)[0];
        int originalY = Coordinate.get(fieldIdx)[1];

        double relativeX = originalX * board.getWidth() * ASPECT_RATIO_WIDTH;
        double relativeY = originalY * board.getHeight() * ASPECT_RATIO_HEIGHT;

        double finalX = relativeX - offsetX;
        double finalY = relativeY - offsetY;

        button.setLayoutX(finalX);
        button.setLayoutY(finalY);
    }

    /**
     * Benötigt damit das GUI die Informationen enthält welcher Spieler in welcher Reihenfolge das Ziel betritt
     * @param playerIdx
     */
    private void onPlayerReachFinish(int playerIdx) {
        if (onTarget.contains(playerIdx)) {
            return;
        }

        onTarget.add(playerIdx);
    }

    /**
     * Ermittelt die Platzierung eines Spielers in der Rangfolge, welcher auf dem Zielfeld steht.
     *
     * @param playerIdx Der Spieler
     * @return Platzierung
     */
    private int getPlaceOnTarget(int playerIdx) {
        if (!onTarget.contains(playerIdx)) {
            return -1;
        }

        return onTarget.indexOf(playerIdx);
    }

    /**
     * Ermittelt, ob das GamePane blockiert ist.
     *
     * @return true, falls blockiert.
     */
    private boolean isGameBlocked() {
        return gameBlocked;
    }

    /**
     * Berechnet die Koordinaten auf der Oberfläche im GamePane eines Spielers anhand der Position.
     * Bewegt den Spieler in das Ziel, falls die Position dem Zielfeld entspricht.
     * Koordinaten werden angepasst, dass diese proportional gleich bleiben beim Verändern
     * der Größe der Oberfläche.
     *
     * @param playerIdx Der Spieler
     * @param position Die neue Position
     * @return Array mit zwei Werten, {x-Koordinate, y-Koordinate}
     */
    private double[] calculatedPlayerCoordinates(int playerIdx, int position) {
        double[] coords = new double[2];

        ImageView icon = icons[playerIdx];
        Pane board = gamePane;

        int startX_offset = 40;
        int endX_offset = 40;


        int placeOnFinish = -1;

        if (position == GameLogic.FINAL_FIELD_POSITION) {
            onPlayerReachFinish(playerIdx);
            placeOnFinish = getPlaceOnTarget(playerIdx);
        }

        // die end-Koordinaten werden nicht von den Buttons verwendet, da das Zielfeld nicht auf der Position im GUI "sitzt"
        // wo die Spieler sich später stapeln sollen, und die Spieler so den Button auch nicht überdecken.
        int endX = 540 + endX_offset * placeOnFinish;
        int endY = 350;

        int offsetX = (int) (icon.getFitWidth() / 2);
        int offsetY = (int) (icon.getFitHeight() / 2);

        final int lastFieldPosition = 64;

        // Position == 0 heißt es wird die Startposition gewählt, diese ist von Stein zu Stein unterschiedlich
        // -1 Da Array bei Index 0 anfängt, aber Position bei 1 wegen Start
        int originalX = position == lastFieldPosition ? endX : Coordinate.get(position)[0];

        int originalY = switch (position) {
            case lastFieldPosition -> endY;
            default -> Coordinate.get(position)[1];
        };

        // Ausnahme Regel, da beim Startpunkt die Spieler leicht versetzt stehen sollen, je nach ihrer Reihenfolge
        if (position == 0) {
            originalX += startX_offset * playerIdx;
        }

        // 1 / Originale Resolution
        float ASPECT_RATIO_WIDTH = 1f / 1000f;
        float ASPECT_RATIO_HEIGHT = 1f / 750f;

        double relativeX = originalX * board.getWidth() * ASPECT_RATIO_WIDTH;
        double relativeY = originalY * board.getHeight() * ASPECT_RATIO_HEIGHT;

        double finalX = relativeX - offsetX;
        double finalY = relativeY - offsetY;

        coords[0] = finalX;
        coords[1] = finalY;

        return coords;
    }

    /**
     * Methode zum Übersetzen eines Tokens in die anzuzeigende Sprache im GUI
     *
     * @param msg Der zu übersetzende Token
     * @return Der übersetzte Text als String
     */
    private String translate(Token msg) {
        return translator.translate(msg);
    }

    /**
     * Überladene Methode zum Übersetzen von spielerbezogenen Tokens mit den jeweiligen Attributen
     *
     * @param msg    Der zu übersetzende Token
     * @param player Der relevante Spieler
     * @return Der übersetzte Text als STring
     */
    private String translate(Token msg, Player player) {
        String output = translate(msg);

        if (player != null) {
            output = output.replace("{username}", player.getName());
            output = output.replace("{carrots}", String.valueOf(player.getCarrots()));
            output = output.replace("{salads}", String.valueOf(player.getSalads()));
        }

        return output;
    }

    /**
     * Methode, welche die Interaktionen mit der Oberfläche deaktiviert. Wird genutzt nach dem
     * Vollenden eines Spiels durch das Erreichen des Zieles von allen Spielern.
     *
     * @param value true, falls gamepane disabled sein soll
     */
    private void disableGamePane(boolean value) {
        gameBlocked = value;
        gamePane.setDisable(value);
    }

    @Override
    public void initializePlayer(int playerIdx, Player player) {

        ImageView icon = icons[playerIdx];

        icon.fitWidthProperty().addListener(
                (observableValue, number, t1) -> alignPlayer(playerIdx, player.getPosition()));

        icon.fitHeightProperty().addListener(
                (observableValue, number, t1) -> alignPlayer(playerIdx, player.getPosition()));
    }

    @Override
    public void setOnTarget(List<Integer> onTarget) {
        this.onTarget = onTarget;
    }

    @Override
    public void alignPlayer(int playerIdx, int position) {
        ImageView icon = icons[playerIdx];
        double[] coordinates = calculatedPlayerCoordinates(playerIdx, position);

        double finalX = coordinates[0];
        double finalY = coordinates[1];

        icon.setTranslateX(finalX);
        icon.setTranslateY(finalY);
    }

    @Override
    public void movePlayer(int playerIdx, int start, int destination, Runnable afterMoveEvent) {

        Timeline timelineX = new Timeline();
        Timeline timelineY = new Timeline();

        int duration = 300;
        ImageView icon = icons[playerIdx];

        int iteration = 0;
        int distance = Math.abs(destination - start);

        // Falls sich rückwärts bewegt wird (z.B. auf ein Igel-Feld) wird directionalMultiplier interessant,
        // da dann je Iteration die Position des Spielers verkleinert wird
        int directionalMultiplier = destination > start ? 1 : -1;

        while (distance >= iteration) {
            double[] coords = calculatedPlayerCoordinates(playerIdx, start + (iteration * directionalMultiplier));
            double x = coords[0];
            double y = coords[1];

            KeyValue valueX = new KeyValue(icon.translateXProperty(), x);
            KeyValue valueY = new KeyValue(icon.translateYProperty(), y);

            int startDuration = duration * iteration;

            KeyFrame frameX = new KeyFrame(Duration.millis(startDuration), valueX);
            KeyFrame frameY = new KeyFrame(Duration.millis(startDuration), valueY);

            timelineX.getKeyFrames().add(frameX);
            timelineY.getKeyFrames().add(frameY);
            iteration++;
        }

        Transition transition = new ParallelTransition(
                timelineX,
                timelineY
        );


        // Einschränkungen während der Animation, z.B. Resizing aus & menuItemSave disabled
        gamePane.setDisable(true);
        menuItemSave.setDisable(true);
        Stage stage = (Stage) gamePane.getScene().getWindow();
        stage.setResizable(false);


        transition.setOnFinished(actionEvent -> {

            stage.setResizable(true);
            menuItemSave.setDisable(false);

            /*
                Spiel wird nur entblockt, wenn es nicht davor durch eine Karte schon blockiert wurde
                benötigt da das gamePane während der Animation sowieso deaktiviert ist und diese
                zusätzliche Information benötigt wird
             */
            if (!isGameBlocked()) {
                disableGamePane(false);
            }

            afterMoveEvent.run();

        });

        transition.play();
    }

    @Override
    public void updateStats(int playerIdx, String username, int carrots, int salads) {
        lblPlayerTurn.setTextFill(colors[playerIdx]);

        lblPlayerTurn.setText(translate(Token.GAME_STATS_TURN) + username);
        lblPlayerCarrots.setText(translate(Token.GAME_STATS_CARROTS) + carrots);
        lblPlayerSalads.setText(translate(Token.GAME_STATS_SALADS) + salads);
    }

    @Override
    public void changeFieldBackgroundColor(int index, boolean reachabe) {

        Color color = reachabe ? Color.GREEN : Color.RED;

        buttons[index].setBackground(new Background(new BackgroundFill(color, new CornerRadii(0), Insets.EMPTY)));
    }

    @Override
    public void highlightPlayer(int playerIdx) {
        ImageView icon = icons[playerIdx];

        DropShadow redRing = new DropShadow(BlurType.GAUSSIAN, Color.RED, 7, 0.9f, 0, 0);

        // Der Spieler der am Zug ist, bekommt einen Roten Ring
        icon.setEffect(redRing);
        icon.toFront();
    }

    @Override
    public void unhighlightPlayer(int playerIdx) {
        ImageView icon = icons[playerIdx];

        // Spieler, die nicht aktuell am Zug sind, werden leicht ausgegraut,
        ColorAdjust effect = new ColorAdjust();
        effect.setSaturation(-0.4);

        icon.setEffect(effect);
    }

    @Override
    public void alertError(Error error) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(translator.translate(Token.TITLE_GUI_ERROR));
            alert.setHeaderText(translator.translate(error.getHeader()));
            alert.setContentText(translator.translate(error.getMsg()) + "\n\n" + error.getCode());
            alert.showAndWait();
        });
    }

    @Override
    public void alertInfo(Token header, Token description, Object... args) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle(translator.translate(Token.TITLE_GUI_ERROR));
            alert.setHeaderText(translator.translate(header));
            String content = translator.translate(description);

            int index = 0;
            while (content.contains("%s") && index < args.length) {
                content = content.replaceFirst("%s", args[index].toString());
                index++;
            }

            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    @Override
    public void alertConfirm(Token message, Consumer<Boolean> func, Object... args) {
        Platform.runLater(() -> {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(translator.translate(Token.TITLE_GUI_ALERT_GAME_EVENT));
            String content = translator.translate(message);

            int index = 0;
            while (content.contains("%s") && index < args.length) {
                content = content.replaceFirst("%s", args[index].toString());
                index++;
            }

            alert.setHeaderText(content);
            Optional<ButtonType> result = alert.showAndWait();

            boolean confirmed = result.isPresent() && result.get() == ButtonType.OK;

            func.accept(confirmed);

        });
    }

    @Override
    public void alertEvent(Token msg, Player player, Runnable event) {
        Platform.runLater(() -> {
            String message = translate(msg, player);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(translator.translate(Token.TITLE_GUI_ALERT_GAME_EVENT));
            alert.setHeaderText(message);
            alert.showAndWait();
            event.run();
        });
    }

    @Override
    public void alertMultipleChoice(Token msg, Consumer<Integer> func, Token... options) {
        Platform.runLater(() -> {

            ButtonType[] btns = new ButtonType[options.length];

            for (int i = 0; i < btns.length; i++) {
                String btnText = translator.translate(options[i]);
                // Ein CANCEL_CLOSE Button muss vorhanden sein, damit das X in der MenuLeiste vom Alert funktioniert
                ButtonBar.ButtonData btnType = i == 0 ? ButtonBar.ButtonData.CANCEL_CLOSE : ButtonBar.ButtonData.OTHER;
                btns[i] = new ButtonType(btnText, btnType);
            }

            Alert alert = new Alert(Alert.AlertType.NONE, translator.translate(msg), btns);
            alert.setTitle(translator.translate(Token.TITLE_GUI_MULTIPLE_CHOICE));
            alert.getButtonTypes().setAll(btns);
            ButtonBar buttonBar=(ButtonBar)alert.getDialogPane().lookup(".button-bar");
            buttonBar.setButtonOrder(ButtonBar.BUTTON_ORDER_NONE);

            Optional<ButtonType> result = alert.showAndWait();

            int idx = 0;

            if (result.isPresent()) {
                for (int i = 0; i < btns.length && idx == 0; i++) {

                    if (result.get().equals(btns[i])) {
                        idx = i;
                    }
                }
            }

            func.accept(idx);

        });
    }

    @Override
    public void gameStop() {
        disableGamePane(true);
        for (ImageView icon : icons) {
            icon.setVisible(false);
        }
    }

    @Override
    public void gameStart(int amountPlayers) {
        disableGamePane(false);
        for (int i = 0; i < amountPlayers && i < icons.length; i++) {
            icons[i].setVisible(true);
        }
    }

    @Override
    public void gameWin(int winnerIdx) {
        for (int i = 0; i < icons.length; i++) {
            unhighlightPlayer(i);
        }
        disableGamePane(true);
        highlightPlayer(winnerIdx);
    }

}
