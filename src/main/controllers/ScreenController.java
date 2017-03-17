package main.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.CineMateApplication;
import main.helpers.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Holds screens. Extends StackPane to be able to easily identify and remove screens that are no longer displayed.
 */
public class ScreenController extends StackPane {

    private HashMap<String, Node> screens = new HashMap<>();

    public ScreenController() {
        super(); //create a StackPane instance
    }

    public void addScreen(String screenName, Node screen) {
        screens.put(screenName, screen);
    }

    public void getScreen(String screenName) {
        screens.get(screenName);
    }

    public boolean loadScreen(String screenName, String fxmlFile) {
        try {
	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
	    Parent screenToLoad = fxmlLoader.load();
	    ControlledScreen screenController = fxmlLoader.getController();
	    screenController.setScreenParent(this);
	    addScreen(screenName, screenToLoad);
	    return true;
	} catch (IOException e) {
            Log.debug("Crashed while loading screen: " + e.getCause());
	    return false;
	}
    }

    public boolean unloadScreen(String screenName) {
        if(screens.remove(screenName) != null) {
            return true;
	} else {
	    Log.debug("There was no screen with this name to remove.");
	    return false;
	}
    }

    public void setScreen(String screenName) {
        if(screens.get(screenName) != null) { //screen loaded already

	    DoubleProperty screenOpacity = opacityProperty();

	    //if there are screens shown already, we have to handle removing and adding the old and new ones
	    if(!getChildren().isEmpty()) {
	        //Play fade out animation, go from full opacity with the current screen to no opacity
		//All the while, go from no opacity on the screen to be added to full opacity
	        Timeline fadeOut = new Timeline(
	        	new KeyFrame(Duration.ZERO, new KeyValue(screenOpacity, 1)),
			new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
			    @Override public void handle(final ActionEvent event) {
			        //remove current screen and add new screen with fade in animation
				getChildren().remove(0);
				getChildren().add(0, screens.get(screenName));
				CineMateApplication.resizeScreen(); //resize screen normally instead of inheriting size from parent
				playFadeIn(screenOpacity);
			    }
			}, new KeyValue(screenOpacity, 0)));
	        fadeOut.play();
	    } else { //if there aren't any screens shown already, we just need to add our new screen
	        setOpacity(0);
	        getChildren().add(screens.get(screenName));
	        playFadeIn(screenOpacity);
	    }

	}
    }

    public void setNewPopupWindow(String fxmlFile, List<?> passedData) {
	try {
	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
	    Stage stage = new Stage();
	    stage.setScene(new Scene(fxmlLoader.load()));
	    ControlledWindow windowController = fxmlLoader.getController();
	    windowController.setPassedData(passedData);
	    stage.show();
	} catch (IOException e) {
	    Log.debug("Crashed while loading popup window: " + e.getCause());
	}
    }

    public void playFadeIn(DoubleProperty screenOpacity) {
	Timeline fadeIn = new Timeline(
 	new KeyFrame(Duration.ZERO, new KeyValue(screenOpacity, 0)),
	new KeyFrame(Duration.seconds(1), new KeyValue(screenOpacity, 1)));
	fadeIn.play();
    }


}
