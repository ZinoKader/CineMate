package main.controllers.contract;

import javafx.stage.Stage;
import main.controllers.ScreenController;

/**
 * Contract for controllers that are opened like new windows
 */
public interface ControlledWindow {

    void setStage(Stage stage);
    void setScreenParent(ScreenController screenParent);
    void setPassedData(Object passedData);
    void closeWindow();

}
