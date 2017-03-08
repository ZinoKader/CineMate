package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.api.MovieApi;
import main.api.ApiService;
import main.config.UserSettings;
import main.exceptions.PropertyAccessException;
import main.exceptions.PropertyLoadException;


/**
 * Main application class. Initiates JavaFX.
 */
public class CineMateApplication extends Application {

    public CineMateApplication() {

    }

    private static final String RALEWAY_FONT_LIGHT_PATH = "https://fonts.googleapis.com/css?family=Raleway:300";

    @Override public void start(final Stage primaryStage) throws Exception {
	Parent root = FXMLLoader.load(getClass().getResource("/fxml/style.fxml"));


	/*
	Group root = new Group();
	root.getStylesheets().add(RALEWAY_FONT_LIGHT_PATH);
	GridPane gridPane = new GridPane();
	gridPane.setHgap(20);
	gridPane.setGridLinesVisible(true); //for debugging

	Label searchLabel = new Label("Search for movies, series, people...");
	searchLabel.setStyle(FontHelper.getWebFont("Raleway", 18));

	Button searchButton = new Button();
	searchButton.setText("Search!");

	TextField searchField = new TextField();
	gridPane.add(searchLabel, 0, 0);
	gridPane.add(searchField, 0, 1);
	gridPane.add(searchButton, 1, 1);

	root.getChildren().add(gridPane);


	searchButton.setOnAction(event -> System.out.println(searchField.getText()));
	*/

	primaryStage.setTitle("CineMate");
	primaryStage.setScene(new Scene(root, 600, 400));
	primaryStage.show();
    }

    public static void main(String[] args) {

	UserSettings settings = null;
	try {
	    settings = new UserSettings();
	} catch (PropertyLoadException e) {
	    e.printStackTrace();
	}

	assert settings != null;
	settings.setApiKey("4b45808a4d1a83471866761a8d7e5325");

	String apiKey = "";
	try {
	    System.out.println("API Key: " + settings.getApiKey());
	    apiKey = settings.getApiKey();
	} catch (PropertyAccessException e) {
	    e.printStackTrace();
	}

	MovieApi movieApi = new MovieApi();
	ApiService apiService = movieApi.getService();

	final String finalApiKey = apiKey;

	launch(args);

    }
}
