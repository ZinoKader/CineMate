package main.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.HashMap;

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
	    screenController.setScreen(this);
	    addScreen(screenName, screenToLoad);
	    return true;
	} catch (IOException e) {
	    e.printStackTrace();
	    return false;
	}
    }


}
