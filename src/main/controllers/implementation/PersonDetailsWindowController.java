package main.controllers.implementation;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.CineMateApplication;
import main.constants.FXConstants;
import main.controllers.DetailsWindowBase;
import main.controllers.ScreenController;
import main.controllers.contract.ControlledWindow;
import main.model.AppendedQueries;
import main.model.Movie;
import main.model.Person;
import main.model.TmdbQuery;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Controller implementation for detailed person information window
 */
public class PersonDetailsWindowController extends DetailsWindowBase implements Initializable, ControlledWindow {

    @FXML
    private JFXListView<Movie> knownForListView;

    private Person person;

    @Override
    public void setStage(Stage stage) {
	this.stage = stage;
    }

    @Override
    public void setScreenParent(ScreenController screenController) {
	this.screenController = screenController;
    }

    @Override
    public void setPassedData(Object passedData) {
	if(passedData instanceof Person) {
	    person = (Person) passedData;
	}
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize();
    }

    @Override
    public void delegateSetData() {
	//TODO: Remove manual API key
	AppendedQueries appendedQueries = new AppendedQueries(Arrays.asList(TmdbQuery.MOVIE_CREDITS, TmdbQuery.TV_CREDITS, TmdbQuery.IMAGES));

	person = apiService.getPersonDetailed(person.getId(), appendedQueries, "4b45808a4d1a83471866761a8d7e5325");
	stage.setTitle(person.getName());

	setBaseDetails();
    }

    @Override public void setBaseDetails() {

    }

    public void handleKnownForClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == FXConstants.DOUBLE_CLICK_COUNT) {
	    Movie selectedMovie = knownForListView.getSelectionModel().getSelectedItem();
	    screenController.loadWindow(CineMateApplication.MOVIE_WINDOW_FXML, selectedMovie);
	}
    }
}
