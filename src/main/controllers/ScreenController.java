package main.controllers;

import com.esotericsoftware.minlog.Log;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.CineMateApplication;
import main.controllers.contract.ControlledScreen;
import main.controllers.contract.ControlledWindow;

import java.io.IOException;
import java.util.HashMap;

/**
 * Holds screens. Creates new windows. Extends StackPane to be able to easily identify and remove screens that are no longer displayed.
 */
public class ScreenController extends StackPane {

    private HashMap<String, Node> screens = new HashMap<>();

    public ScreenController() {
        super(); //create a StackPane instance
    }

    private void addScreen(String screenName, Node screen) {
        screens.put(screenName, screen);
    }

    public void loadScreen(String screenName, String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent screenToLoad = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(screenToLoad));

            ControlledScreen screenController = fxmlLoader.getController();

            screenController.setScreenParent(this);
            addScreen(screenName, screenToLoad);
        } catch (IOException e) {
            Log.debug("Crashed while loading screen: " + screenName);
            e.printStackTrace();
        }
    }

    private boolean unloadScreen(String screenName) {
        if(screens.remove(screenName) != null) {
            return true;
        } else {
            Log.debug("There was no screen with this name to remove.");
            return false;
        }
    }

    public void reloadScreen(String screenName, String fxmlFile) {
        if(unloadScreen(screenName)) {
            loadScreen(screenName, fxmlFile);
            setScreen(screenName);
            Log.debug("Reloaded screen " + screenName + " successfully");
        } else {
            Log.debug("Could not unload screen " + screenName + ", did not reload screen.");
        }
    }

    public void loadWindow(String fxmlFile, Object passedData) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(fxmlLoader.load()));

            ControlledWindow windowController = fxmlLoader.getController();

            windowController.setStage(stage);
            windowController.setScreenParent(this);
            windowController.setPassedData(passedData);
            stage.show();
        } catch (IOException e) {
            Log.debug("Crashed while loading popup window: " + e.getCause());
            e.printStackTrace();
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
                        new KeyFrame(Duration.seconds(1), event -> {
                            //remove current screen and add new screen with fade in animation
                            getChildren().remove(0);
                            getChildren().add(0, screens.get(screenName));
                            CineMateApplication.resizeScreen(); //resize screen normally instead of inheriting size from parent
                            playFadeIn(screenOpacity);
                        }, new KeyValue(screenOpacity, 0)));
                fadeOut.play();
            } else { //if there aren't any screens shown already, we just need to add our new screen
                setOpacity(0);
                getChildren().add(screens.get(screenName));
                playFadeIn(screenOpacity);
            }

        }
    }

    private void playFadeIn(DoubleProperty screenOpacity) {
        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(screenOpacity, 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(screenOpacity, 1)));
        fadeIn.play();
    }


}
