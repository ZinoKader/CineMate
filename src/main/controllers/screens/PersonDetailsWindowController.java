package main.controllers.screens;

import javafx.fxml.Initializable;
import javafx.stage.Stage;
import main.controllers.ControlledWindow;
import main.controllers.ScreenController;
import main.model.Person;

import java.net.URL;
import java.util.ResourceBundle;

public class PersonDetailsWindowController implements Initializable, ControlledWindow {

    private Stage stage;
    private ScreenController screenController;
    private Person person;

    @Override public void initialize(final URL location, final ResourceBundle resources) {
    }

    @Override public void setStage(Stage stage) {
	this.stage = stage;
    }

    @Override public void setScreenParent(ScreenController screenController) {
	this.screenController = screenController;
    }

    @Override public void setPassedData(Object passedData) {
	if(passedData instanceof Person) {
	    person = (Person) passedData;
	}
    }
}
