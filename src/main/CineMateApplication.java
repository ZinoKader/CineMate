package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/**
 * Main application class. Initiates JavaFX.
 */
public class CineMateApplication extends Application {

    private static final int DEFAULT_FONT_SIZE = 12;

    public CineMateApplication() {

    }
    

    @Override public void start(final Stage primaryStage) throws Exception {
	Font.loadFont(getClass().getResource("/font/Raleway.ttf").toExternalForm(), DEFAULT_FONT_SIZE);
	Parent root = FXMLLoader.load(getClass().getResource("/fxml/style.fxml"));
	primaryStage.setTitle("CineMate");
	primaryStage.setScene(new Scene(root, 600, 400));
	primaryStage.setResizable(false);
	primaryStage.show();
    }

    public static void main(String[] args) {
	launch(args);
    }
}
