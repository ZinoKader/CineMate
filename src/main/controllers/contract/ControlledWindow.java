package main.controllers.contract;

import javafx.stage.Stage;
import main.controllers.ScreenController;

public interface ControlledWindow {

    public void setStage(Stage stage);
    public void setScreenParent(ScreenController screenController);
    public void setPassedData(Object passedData);

}
