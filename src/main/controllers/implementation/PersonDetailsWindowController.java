package main.controllers.implementation;

import javafx.fxml.Initializable;
import javafx.stage.Stage;
import main.api.ApiAdapater;
import main.api.ApiService;
import main.controllers.ScreenController;
import main.controllers.contract.ControlledWindow;
import main.controllers.contract.DetailedView;
import main.model.AppendedQueries;
import main.model.Person;
import main.model.TmdbQuery;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Controller implementation for detailed person information window
 */
public class PersonDetailsWindowController implements Initializable, ControlledWindow, DetailedView {

    private ScreenController screenController;
    private Stage stage;

    private ApiAdapater apiAdapater;
    private ApiService apiService;

    private Person person;

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

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
	apiAdapater = new ApiAdapater();
	apiService = apiAdapater.getService();
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
}
