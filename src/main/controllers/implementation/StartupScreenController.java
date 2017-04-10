package main.controllers.implementation;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import main.CineMateApplication;
import main.api.ApiAdapater;
import main.api.ApiService;
import main.config.UserSettings;
import main.constants.FXConstants;
import main.controllers.ScreenController;
import main.controllers.contract.ControlledScreen;
import main.exceptions.EmptyValueException;
import main.exceptions.PropertyLoadException;
import main.helpers.DelayedTaskHelper;
import main.helpers.MessageHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for startup screen. Handles implementations of logic.
 */
public class StartupScreenController implements Initializable, ControlledScreen {

	private ScreenController screenParent;
	private UserSettings userSettings;
	private MessageHelper messageHelper;
	private DelayedTaskHelper delayedTaskHelper;

	private ApiAdapater apiAdapater;
	private ApiService apiService;

	@FXML
	private Parent rootParent;

	@FXML
	private JFXTextField apiKeyTextField;

    private String apiKey;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

		messageHelper = new MessageHelper(rootParent);
        delayedTaskHelper = new DelayedTaskHelper();

		//FOR DEBUGGING: REMOVE LATER
		apiKeyTextField.setText("4b45808a4d1a83471866761a8d7e5325");

        try {
            userSettings = new UserSettings();
        } catch (PropertyLoadException e) {
            e.printStackTrace();
        }

        try {
            apiKey = userSettings.getApiKey();
        } catch (EmptyValueException e) {
            e.printStackTrace();
        }

        listenToWindowShown();

	}

	@Override
    public void listenToWindowShown() {
        Task<Void> checkApiTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater( () -> checkForSavedApiKey());
                return null;
            }
        };

        delayedTaskHelper.delayedTask(checkApiTask, FXConstants.DEFAULT_DELAYED_TASK_TIME);
    }


    /**
     * Design decision: this is run later because running goToMainScreen() in initialize() would not have required
     * fields available, such as screenParent, because in ScreenController, we have to first initialize the FxmlLoader
     * which in turn will run the initialize() method on all of our controllers. But at this point, our screenParent
     * field has not been assigned yet in any controller, because the screen controller is gotten from the FxmlLoader.
     *
     * So to avoid NullPointerException where we try to reference a screenParent that doesn't exist yet, we have to
     * delay the running of this method to when window is actually showing, which is done in listenToWindowShown()
     *
     */
	private void checkForSavedApiKey() {
        //FOR DEBUGGING: APIKEY 4b45808a4d1a83471866761a8d7e5325
        //We can continue with logging in if the API key has been set before already
        if(apiKey != null && !apiKey.isEmpty()) {
            messageHelper.showMessage("API key is already stored, logging in...");
            goToMainScreen();
        }

    }

	public void handleSubmitApiKey() {
		String apiKey = apiKeyTextField.getText();

        apiAdapater = new ApiAdapater(apiKey);
        apiService = apiAdapater.getApiService();

		apiService.getResponse().enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> loginValidation) {
                //UI can't be updated from non-application thread, run this later on the UI thread.
                //Set API key in properties file, so the user only needs to enter it once.
                if(loginValidation.isSuccessful()) {
                    try {
                        userSettings.setApiKey(apiKeyTextField.getText());
                        Platform.runLater(() -> messageHelper.showMessage("Success! You're being logged in..."));
                        goToMainScreen();
                    } catch (IOException e) {
                        messageHelper.showMessage("Could not save your API key.");
                        e.printStackTrace();
                    }
                } else {
                    Platform.runLater( () -> messageHelper.showMessage("Could not reach authentication services"));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Platform.runLater( () -> messageHelper.showMessage("Your API key is incorrect or invalid"));
            }
        });

	}

	private void goToMainScreen() {
		screenParent.setScreen(CineMateApplication.MAIN_SCREEN_ID);
	}


    @Override public void setScreenParent(ScreenController screenParent) {
		this.screenParent = screenParent;
	}
}
