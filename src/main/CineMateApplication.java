package main;

import com.esotericsoftware.minlog.Log;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import main.controllers.ScreenController;


/**
 * Main application class. Initiates JavaFX.
 */
public class CineMateApplication extends Application {


    @SuppressWarnings("StaticVariableMayNotBeInitialized")
    private static Stage primaryStage;

    private static final int DEFAULT_FONT_SIZE = 12;
    private static final String RALEWAY_PATH = "/font/Raleway.ttf";

    //made public so we can reach them from controllers

    //screens
    public static final String STARTUP_SCREEN_ID = "startup_screen";
    public static final String STARTUP_SCREEN_FXML = "/fxml/startup.fxml";
    public static final String MAIN_SCREEN_ID = "main_screen";
    public static final String MAIN_SCREEN_FXML = "/fxml/main.fxml";

    //windows
    public static final String MOVIE_WINDOW_FXML = "/fxml/movie_window.fxml";
    public static final String SERIES_WINDOW_FXML = "/fxml/series_window.fxml";
    public static final String PERSON_WINDOW_FXML = "/fxml/person_window.fxml";
    public static final String SETTINGS_WINDOW_FXML = "/fxml/settings_window.fxml";

    public CineMateApplication() {

    }

    @Override
    public void start(Stage stage) throws Exception {

        Log.DEBUG(); //set logging level to DEBUG as default

        //to reach the stage from outside of our application class, this static field has to be assigned from here
        CineMateApplication.primaryStage = stage;

        Font.loadFont(getClass().getResource(RALEWAY_PATH).toExternalForm(), DEFAULT_FONT_SIZE);

        ScreenController screenContainer = new ScreenController();

        //load up all of our screens that will be used throughout the application
        screenContainer.loadScreen(STARTUP_SCREEN_ID, STARTUP_SCREEN_FXML);
        screenContainer.loadScreen(MAIN_SCREEN_ID, MAIN_SCREEN_FXML);

        //set the first screen to start up in
        screenContainer.setScreen(STARTUP_SCREEN_ID);

        //create a stage/scene for us to switch screens in
        Group root = new Group();
        root.getChildren().addAll(screenContainer); //works because controller is a subclass of Parent which is a Node
        Scene scene = new Scene(root);
        CineMateApplication.primaryStage.setTitle("CineMate by Zino Kader");
        CineMateApplication.primaryStage.setScene(scene);
        CineMateApplication.primaryStage.setResizable(false);
        CineMateApplication.primaryStage.show();
    }

    public static void resizeScreen() {
        //sizes the window automatically to fit the contents of the scene
        CineMateApplication.primaryStage.sizeToScene();
        CineMateApplication.primaryStage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
