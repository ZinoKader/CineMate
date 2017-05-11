package main.controllers.implementation;

import com.esotericsoftware.minlog.Log;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import main.CineMateApplication;
import main.api.ApiAdapter;
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
@SuppressWarnings("InstanceVariableMayNotBeInitialized")
public class StartupScreenController implements Initializable, ControlledScreen {

    private ScreenController screenParent;
    private UserSettings userSettings;
    private MessageHelper messageHelper;
    private DelayedTaskHelper delayedTaskHelper;

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
            Log.debug("Failed to load properties file on startup");
            Platform.runLater( () -> messageHelper.showMessage("Failed to load settings. Please contact the developer."));
            e.printStackTrace();
        }

        try {
            apiKey = userSettings.getApiKey();
        } catch (EmptyValueException e) {
            Log.debug("No entry for apiKey exists");
            e.printStackTrace();
        }

        //If userSettings is null, we can not continue with our program and an assertion can be thrown
        //This should rarely happen but if it does, there is no way our program can communicate with the API
        assert userSettings != null : "UserSettings was null. Properties must be loaded correctly for the program to run.";

        //comment this out to not login automatically
        //Platform.runLater(this::delayLogin);

    }

    @Override
    public void delayLogin() {
        delayedTaskHelper.delayedTask(checkForSavedApiKey(), FXConstants.DEFAULT_DELAYED_TASK_TIME);
    }


    /**
     * This is run later because running goToMainScreen() in initialize() would not have required
     * fields available, such as screenParent, because in ScreenController, we have to first initialize the FxmlLoader
     * which in turn will run the initialize() method on all of our controllers. But at this point, our screenParent
     * field has not been assigned yet in any controller, because the screen controller is gotten from the FxmlLoader.
     *
     * So to avoid NullPointerException where we try to reference a screenParent that doesn't exist yet, we have to
     * delay the running of this method to when window is actually showing, which is done in delayLogin()
     *
     */
    private Task<Void> checkForSavedApiKey() {
        //We can continue with logging in if the API key has been set before already
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (apiKey != null && !apiKey.isEmpty()) {
                    messageHelper.showMessage("Using previously stored API key, logging in...");
                    goToMainScreen();
                }
                return null;
            }
        };
    }

    public void handleSubmitApiKey() {
        String apiKey = apiKeyTextField.getText();

        ApiAdapter apiAdapter = new ApiAdapter(apiKey);
        ApiService apiService = apiAdapter.getApiService();

        apiService.getResponse().enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> loginValidation) {
                //UI can't be updated from non-application thread, run this later on the UI thread.
                //Set API key in properties file, so the user only needs to enter it once.
                if(loginValidation.isSuccessful()) {
                    try {
                        Log.debug("API key was successfully set, logging in user");
                        userSettings.setApiKey(apiKeyTextField.getText());
                        Platform.runLater( () -> messageHelper.showMessage("Success! You're being logged in..."));
                        goToMainScreen();
                    } catch (IOException e) {
                        Log.debug("Could not set API key for user: " + e.getCause());
                        Platform.runLater( () -> messageHelper.showMessage("Could not save your API key, please try again"));
                        e.printStackTrace();
                    }
                } else {
                    Log.debug("Authentication services were unreachable - check internet connection");
                    Platform.runLater( () -> messageHelper.showMessage("Could not reach authentication services"));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.debug("The entered API key was invalid ");
                Platform.runLater( () -> messageHelper.showMessage("Your API key is invalid"));
                throwable.printStackTrace();
            }
        });

    }

    private void goToMainScreen() {
        screenParent.setScreen(CineMateApplication.MAIN_SCREEN_ID);
    }


    @Override public void setScreenParent(ScreenController screenController) {
        this.screenParent = screenController;
    }
}
