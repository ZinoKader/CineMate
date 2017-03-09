package main.controllers.screens;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import main.api.ApiAdapater;
import main.api.ApiService;
import main.config.UserSettings;
import main.controllers.ControlledScreen;
import main.controllers.ScreenController;
import main.exceptions.PropertyAccessException;
import main.exceptions.PropertyLoadException;
import main.model.Movie;
import main.model.Person;
import main.model.ResultsPager;
import main.model.Series;
import main.model.TmdbObject;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements Initializable, ControlledScreen {

    @FXML
    private TextField searchTextField;

    @FXML
    private ChoiceBox<SEARCHTYPE> searchTypeBox;

    private SEARCHTYPE currentSearchType;
    private ScreenController screenController;
    private UserSettings userSettings;

    private ApiAdapater apiAdapter;
    private ApiService apiService;


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

	try {
	    userSettings = new UserSettings();
	} catch (PropertyLoadException e) {
	    e.printStackTrace();
	}
	apiAdapter = new ApiAdapater();
	apiService = apiAdapter.getService();
    }

    private void listenToSearchTypeChanges() {
	searchTypeBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
	    @Override public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		currentSearchType = SEARCH_TYPE_INDICES.get(newValue);
		searchTextField.setPromptText("Search for " + SEARCH_TYPE_INDICES.get(newValue).toString().toLowerCase());
	    }
	});
    }

    public void searchButtonPressed() throws PropertyAccessException {
	if(!searchTextField.getText().isEmpty() && currentSearchType != null) {

	    String searchQuery = searchTextField.getText();
	    Map<String, String> apiQueries = new HashMap<>();

	    //TODO: Remove this when compiling
	    apiQueries.put("api_key", "4b45808a4d1a83471866761a8d7e5325");
	    //and replace with this: apiQueries.put("api_key", userSettings.getApiKey());
	    apiQueries.put("query", searchQuery);

	    handleSearch(apiQueries);

	} else {
	    System.out.println("no results");
	}
    }

    private void handleSearch(Map<String, String> apiQueries) {

	ResultsPager<? extends TmdbObject> searchResults = null;

	switch(currentSearchType) {
	    case MOVIES:
		searchResults = apiService.searchMovies(apiQueries);
		List<Movie> movies = (List<Movie>) searchResults.getResults();
		populateMovieList(movies);
		break;
	    case SERIES:
	        searchResults = apiService.searchSeries(apiQueries);
	        break;
	    case PEOPLE:
	        searchResults = apiService.searchPeople(apiQueries);
	}


	//TODO: Remove!! for debugging.
	for(TmdbObject tmdbObject : searchResults.getResults()) {

	    switch(tmdbObject.getMediaType()) {
		case MOVIE:
		    Movie movie = (Movie) tmdbObject;
		    System.out.println(movie.getTitle());
		    break;
		case SERIES:
		    Series series = (Series) tmdbObject;
		    System.out.println(series.getTitle());
		    break;
		case PERSON:
		    Person person = (Person) tmdbObject;
		    System.out.println(person.getName());
		    break;
	    }
	}
    }

    private void populateMovieList(List<Movie> movies) {

    }

    @Override public void setScreenParent(final ScreenController screenParent) {
	this.screenController = screenParent;
    }


}
