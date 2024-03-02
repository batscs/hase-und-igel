package gui;

import gui.data.Filepath;
import gui.data.Specification;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Class that starts our application.
 *
 * @author github.com/batscs
 */
public class ApplicationMain extends Application {

    /**
     * Creating the stage and showing it. This is where the initial size and the
     * title of the window are set.
     *
     * @param stage the stage to be shown
     * @throws IOException io exception
     */
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationMain.class.getResource("UserInterface.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setMinHeight(800);
        stage.setMinWidth(1200);
        stage.setTitle(Specification.APPLICATION_TITLE + " " + Specification.APPLICATION_VERSION + " | " + Specification.APPLICATION_AUTHOR);
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(
                ApplicationMain.class.getResourceAsStream(
                        Filepath.FILE_APPLICATION_ICON.toString()))));
        stage.show();

    }

    /**
     * Main method
     *
     * @param args unused
     */
    public static void main(String... args) {
        launch(args);
    }
}
