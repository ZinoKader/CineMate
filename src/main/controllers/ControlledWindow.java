package main.controllers;

import javafx.stage.Stage;

import java.util.List;

public interface ControlledWindow {

    public void setStage(Stage stage);
    public void setPassedData(List<?> passedData);

}
