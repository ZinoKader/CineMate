package main.controllers.contract;

import javafx.stage.Stage;
import main.controllers.ScreenController;

public interface ControlledWindow {

    void setStage(Stage stage);
    void setScreenParent(ScreenController screenParent);
    void setPassedData(Object passedData);
    void closeWindow();

}
