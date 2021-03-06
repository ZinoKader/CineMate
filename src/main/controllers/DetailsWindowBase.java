package main.controllers;

import com.esotericsoftware.minlog.Log;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import main.api.ApiAdapter;
import main.api.ApiService;
import main.config.UserSettings;
import main.controllers.contract.ControlledWindow;
import main.controllers.contract.DetailedView;
import main.exceptions.EmptyValueException;
import main.exceptions.PropertyLoadException;
import main.helpers.DelayedTaskHelper;
import main.helpers.ImageCache;
import main.helpers.MessageHelper;
import main.model.TmdbObject;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Base implementation of a controller window. Handles setting up helpers, fetching settings and other generic tasks.
 *
 * Making this class abstract allows us to "know" that all of our subclasses definitely implement the interfaces in DetailedView.
 * As such, we can call these methods from our abstract class because we're certain that our subclasses have an implementation of these methods.
 */
@SuppressWarnings("InstanceVariableMayNotBeInitialized")
public abstract class DetailsWindowBase implements Initializable, DetailedView, ControlledWindow {

    protected ApiService apiService;
    protected Stage stage;
    protected ScreenController screenParent;
    protected UserSettings userSettings;
    protected ImageCache imageCache;
    protected DelayedTaskHelper delayedTaskHelper;
    protected MessageHelper messageHelper;

    protected TmdbObject passedInTmdbObject;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String apiKey = "";

        try {
            userSettings = new UserSettings();
            apiKey = userSettings.getApiKey();
        } catch (PropertyLoadException e) {
            Platform.runLater( () -> stage.close());
            Log.debug("Error while loading UserSettings for details window");
            e.printStackTrace();
        } catch (EmptyValueException e) {
            Platform.runLater( () -> stage.close());
            Log.debug("Error while fetching api key from properties");
            e.printStackTrace();
        }

        ApiAdapter apiAdapter = new ApiAdapter(apiKey);
        apiService = apiAdapter.getApiService();
        imageCache = new ImageCache();
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
        if(passedData instanceof TmdbObject) {
            passedInTmdbObject = (TmdbObject) passedData;
        } else {
            Log.debug("Passed data must be of instance " + TmdbObject.class);
            closeWindow();
        }
    }


    @Override
    public void closeWindow() {
        stage.close();
    }

}
