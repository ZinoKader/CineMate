package main.controllers.screens;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import main.CineMateApplication;
import main.api.ApiAdapater;
import main.api.ApiService;
import main.config.UserSettings;
import main.controllers.ControlledScreen;
import main.controllers.ScreenController;
import main.exceptions.PropertyAccessException;
import main.exceptions.PropertyLoadException;
import main.helpers.MessageHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for startup screen. Handles implementations of logic.
 */
public class StartupScreenController implements Initializable, ControlledScreen {

    private ScreenController screenController;
    private UserSettings settings;
    private MessageHelper messageHelper;
    private ApiAdapater apiAdapater = new ApiAdapater();
    private ApiService apiService = apiAdapater.getService();

    //@FXML exposes the fields to the fxml file while keeping the fields private, nice!

    @FXML
    private VBox startupPane;

    @FXML
    private JFXTextField apiKeyTextField;

    @Override public void initialize(final URL location, final ResourceBundle resources) {

        messageHelper = new MessageHelper(startupPane);

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

	//FOR DEBUGGING: APIKEY 4b45808a4d1a83471866761a8d7e5325
	//We can continue with logging in if the API key has been set before already
	if(apiKey != null && !apiKey.equals("null") && !apiKey.isEmpty()) {
	    messageHelper.showMessage("Api key is already stored, going to main screen! Key: " + apiKey);
	    goToMainScreen();
	}

	//FOR DEBUGGING: REMOVE LATER
	apiKeyTextField.setText("4b45808a4d1a83471866761a8d7e5325");
    }

    public void handleSubmitApiKey(ActionEvent actionEvent) {
	String apiKey = apiKeyTextField.getText();

	apiService.getResponse(apiKey, new Callback<Response>() {
	    @Override public void success(final Response response, final Response response2) {
		//UI can't be updated from non-application thread, run this later on the UI thread.
		//Set API key in properties file, so the user only needs to enter it once.
		settings.setApiKey(apiKeyTextField.getText());
		Platform.runLater( () -> messageHelper.showMessage("Success! You're being logged in..."));
	        goToMainScreen();
	    }

	    @Override public void failure(final RetrofitError retrofitError) {
		// -11-
	        Platform.runLater( () -> messageHelper.showMessage("Your API key is incorrect or invalid"));
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
