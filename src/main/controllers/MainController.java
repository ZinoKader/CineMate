package main.controllers;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable, ControlledScreen {

    private ScreenController screenController;

    @Override public void initialize(final URL location, final ResourceBundle resources) {

    }

    @Override public void setScreenParent(final ScreenController screenParent) {
	this.screenController = screenParent;
    }
}
