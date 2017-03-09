package main.controllers.screens;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import main.controllers.ControlledScreen;
import main.controllers.ScreenController;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements Initializable, ControlledScreen {

    @FXML
    private TextField searchTextField;

    @FXML
    private ChoiceBox<SEARCHTYPE> searchTypeBox;

    private ScreenController screenController;


    /**
     * Types of objects you can search for in the searchfield
     */
    public enum SEARCHTYPE {
        MOVIES, SERIES, PEOPLE
    }

    private static final Map<Integer, SEARCHTYPE> SEARCH_TYPE_INDICES = new HashMap<>();

    @Override public void initialize(final URL location, final ResourceBundle resources) {
        //init our choicebox with possible values. We add these items as observables so changes are broadcasted.
        searchTypeBox.setItems(FXCollections.observableArrayList(SEARCHTYPE.MOVIES, SEARCHTYPE.SERIES, SEARCHTYPE.PEOPLE));
	listenToSearchTypeChanges();
	SEARCH_TYPE_INDICES.put(0, SEARCHTYPE.MOVIES);
	SEARCH_TYPE_INDICES.put(1, SEARCHTYPE.SERIES);
	SEARCH_TYPE_INDICES.put(2, SEARCHTYPE.PEOPLE);


    }

    private void listenToSearchTypeChanges() {
	searchTypeBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
	    @Override public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		searchTextField.setPromptText("Search for " + SEARCH_TYPE_INDICES.get(newValue).toString().toLowerCase());
	    }
	});
    }

    @Override public void setScreenParent(final ScreenController screenParent) {
	this.screenController = screenParent;
    }


}
