package main.controllers.implementation;

import com.esotericsoftware.minlog.Log;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.CineMateApplication;
import main.constants.FXConstants;
import main.controllers.DetailsWindowBase;
import main.controllers.ScreenController;
import main.controllers.contract.ControlledWindow;
import main.exceptions.EmptyValueException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller implementation for detailed person information window
 */
public class SettingsWindowController extends DetailsWindowBase implements Initializable, ControlledWindow {

    @FXML
    private TextField apiKeyTextField;

    private String apiKey;

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setScreenParent(ScreenController screenParent) {
        this.screenParent = screenParent;
    }

    @Override
    public void setPassedData(Object passedData) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        delegateSetData();
    }

    @Override
    public void delegateSetData() {

        try {
            apiKey = userSettings.getApiKey();
        } catch (EmptyValueException e) {
            Log.debug("There was no API key to assign to the text field.");
            e.printStackTrace();
        }

        setBaseDetails();
    }

    @Override
    public void setBaseDetails() {
        if(apiKey != null) {
            apiKeyTextField.setText(apiKey);
        }
    }

    @FXML
    private void saveButtonPressed() {
        if(apiKeyTextField.getText() != null && !apiKeyTextField.getText().isEmpty()) {
            try {
                userSettings.setApiKey(apiKeyTextField.getText());
            } catch (IOException e) {
                messageHelper.showMessage("Failed to save API key");
                Log.debug("Failed to save API key.");
                e.printStackTrace();
            }
        }

        messageHelper.showMessage("Settings saved, closing...");
        delayedTaskHelper.delayedClose(stage, FXConstants.DEFAULT_DELAYED_CLOSE_TIME);
        screenParent.reloadScreen(CineMateApplication.MAIN_SCREEN_ID, CineMateApplication.MAIN_SCREEN_FXML);
    }

}
