package main.controllers;

import javafx.stage.Stage;

public interface ControlledWindow {

    public void setStage(Stage stage);
    public void setScreenParent(ScreenController screenController);
    public void setPassedData(Object passedData);

}
