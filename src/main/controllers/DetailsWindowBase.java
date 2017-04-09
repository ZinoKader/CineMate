package main.controllers;

import com.esotericsoftware.minlog.Log;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import main.api.ApiAdapater;
import main.api.ApiService;
import main.config.UserSettings;
import main.controllers.contract.DetailedView;
import main.exceptions.EmptyValueException;
import main.exceptions.PropertyAccessException;
import main.exceptions.PropertyLoadException;
import main.helpers.DelayedTaskHelper;
import main.helpers.ImageHelper;
import main.helpers.MessageHelper;

/**
 * Making this class abstract allows us to "know" that all of our subclasses definitely implement the interfaces in DetailedView.
 * As such, we can call these methods from our abstract class because we're certain that our subclasses have an implementation of these methods.
 */
public abstract class DetailsWindowBase implements DetailedView {

    protected ApiService apiService;
    protected Stage stage;
    protected ScreenController screenParent;
    protected ImageHelper imageHelper;
    protected DelayedTaskHelper delayedTaskHelper;
    protected MessageHelper messageHelper;

    protected void initialize() {

        String apiKey = "";

        try {
            UserSettings userSettings = new UserSettings();
            apiKey = userSettings.getApiKey();
        } catch (PropertyLoadException e) {
            stage.close();
            Log.debug("Error while loading UserSettings for details window");
            e.printStackTrace();
        } catch (EmptyValueException e) {
            stage.close();
            Log.debug("Error while fetching api key from properties");
            e.printStackTrace();
        }

        ApiAdapater apiAdapater = new ApiAdapater(apiKey);
        apiService = apiAdapater.getApiService();
        imageHelper = new ImageHelper();
        delayedTaskHelper = new DelayedTaskHelper();

        Platform.runLater( () -> { //run when stage has been set
            ObservableList<Node> children = stage.getScene().getRoot().getChildrenUnmodifiable();
            Parent targetParent = (Parent) children.get(0); //first child should be target Pane for messages
            messageHelper = new MessageHelper(targetParent);
        });

        /*
	    Ensures that we get our data when the window has been initialized and setPassedData() has been called
	    Pretty cool Java 8 thing here, we can directly reference the method if it has no parameters instead of creating
	    a lambda expression with empty parameters
	     */
        Platform.runLater(this::delegateSetData);

    }

}
