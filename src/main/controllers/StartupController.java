package main.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.CineMateApplication;
import main.api.ApiService;
import main.api.MovieApi;
import main.config.UserSettings;
import main.exceptions.PropertyAccessException;
import main.exceptions.PropertyLoadException;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for startup screen. Handles implementations of logic.
 */
public class StartupController implements Initializable, ControlledScreen {

    private ScreenController screenController;
    private UserSettings settings = null;
    private MovieApi movieApi = new MovieApi();
    private ApiService apiService = movieApi.getService();

    //@FXML exposes the fields to the fxml file while keeping the fields private, nice!

    @FXML
    private TextField apiKeyTextField;

    @FXML
    private Label entryValidityStatusText;

    @Override public void initialize(final URL location, final ResourceBundle resources) {
	try {
	    settings = new UserSettings();
	} catch (PropertyLoadException e) {
	    e.printStackTrace();
	}
	assert settings != null;

	String apiKey = "";
	try {
	    apiKey = settings.getApiKey();
	} catch (PropertyAccessException e) {
	    e.printStackTrace();
	}

	System.out.println(apiKey + " DICK");

	//FOR DEBUGGING: APIKEY 4b45808a4d1a83471866761a8d7e5325
	//We can continue with logging in if the API key has been set before already
	if(apiKey != null && !apiKey.equals("null") && !apiKey.isEmpty()) {
	    System.out.println("Api key is already stored, going to main screen! Key: " + apiKey);
	    goToMainScreen();
	}
    }

    public void handleSubmitApiKey(ActionEvent actionEvent) {
	String apiKey = apiKeyTextField.getText();

	apiService.getResponse(apiKey, new Callback<Response>() {
	    @Override public void success(final Response response, final Response response2) {
		//UI can't be updated from non-application thread, run this later on the UI thread.
		//Set API key in properties file, so the user only needs to enter it once.
		settings.setApiKey(apiKeyTextField.getText());
	        Platform.runLater( () -> entryValidityStatusText.setText("Success! You're being logged in..."));
	        goToMainScreen();
	    }

	    @Override public void failure(final RetrofitError retrofitError) {
		// -11-
	        Platform.runLater( () -> entryValidityStatusText.setText("Your API key is incorrect or invalid."));
	    }
	});
    }

    public void goToMainScreen() {
	screenController.setScreen(CineMateApplication.MAIN_SCREEN_ID);
    }

    @Override public void setScreenParent(ScreenController screenParent) {
	this.screenController = screenParent;
    }
}
