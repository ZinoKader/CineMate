package main;

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


    private static Stage primaryStage;

    private static final int DEFAULT_FONT_SIZE = 12;

    //made public so we can reach them from controllers
    public static final String STARTUP_SCREEN_ID = "screen1";
    public static final String STARTUP_SCREEN_FXML = "/fxml/startup.fxml";
    public static final String MAIN_SCREEN_ID = "screen2";
    public static final String MAIN_SCREEN_FXML = "/fxml/main.fxml";

    public CineMateApplication() {

    }

    @Override public void start(Stage primaryStage) throws Exception {

	CineMateApplication.primaryStage = primaryStage;
	Font.loadFont(getClass().getResource("/font/Raleway.ttf").toExternalForm(), DEFAULT_FONT_SIZE);

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
	primaryStage.setTitle("CineMate");
	primaryStage.setScene(scene);
	primaryStage.setResizable(false);
	primaryStage.show();
    }

    public static void resizeScreen() {
	//sizes the window automatically to fit the contents of the scene
	primaryStage.sizeToScene();
	primaryStage.centerOnScreen();
    }

    public static void main(String[] args) {
	launch(args);
    }
}
