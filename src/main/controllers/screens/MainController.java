package main.controllers.screens;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import main.api.ApiAdapater;
import main.api.ApiService;
import main.config.UserSettings;
import main.controllers.ControlledScreen;
import main.controllers.ScreenController;
import main.exceptions.PropertyAccessException;
import main.exceptions.PropertyLoadException;
import main.model.MediaType;
import main.model.Movie;
import main.model.Person;
import main.model.ResultsPager;
import main.model.Series;
import main.model.TmdbObject;
import main.view.MovieListViewCell;
import main.view.PersonListViewCell;
import main.view.SeriesListViewCell;

import java.net.URL;
import java.util.Collection;
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


    @FXML
    private StackPane searchPane;

    @FXML
    private ListView<Movie> movieListView;
    private static final String MOVIE_LW_FX_ID = "movieListView";

    @FXML
    private ListView<Series> seriesListView;
    private static final String SERIES_LW_FX_ID = "seriesListView";

    @FXML
    private ListView<Person> personListView;
    private static final String PERSON_LW_FX_ID = "personListView";


    private ObservableList<Movie> movieObservableList = FXCollections.observableArrayList();

    private ObservableList<Series> seriesObservableList = FXCollections.observableArrayList();

    private ObservableList<Person> personObservableList = FXCollections.observableArrayList();

    /**
     * Types of objects you can search for in the searchfield
     */
    public enum SEARCHTYPE {
	MOVIES, SERIES, PEOPLE
    }

    private static final Map<Integer, SEARCHTYPE> SEARCH_TYPE_INDICES = new HashMap<>();
    private static final Map<MediaType, Integer> LIST_VIEW_ORDER = new HashMap<>();

    @Override public void initialize(final URL location, final ResourceBundle resources) {
	//init our choicebox with possible values. We add these items as observables so changes are broadcasted.
	searchTypeBox.setItems(FXCollections.observableArrayList(SEARCHTYPE.MOVIES, SEARCHTYPE.SERIES, SEARCHTYPE.PEOPLE));
	listenToSearchTypeChanges();
	SEARCH_TYPE_INDICES.put(0, SEARCHTYPE.MOVIES);
	SEARCH_TYPE_INDICES.put(1, SEARCHTYPE.SERIES);
	SEARCH_TYPE_INDICES.put(2, SEARCHTYPE.PEOPLE);
	LIST_VIEW_ORDER.put(MediaType.MOVIE, 0);
	LIST_VIEW_ORDER.put(MediaType.SERIES, 1);
	LIST_VIEW_ORDER.put(MediaType.PERSON, 2);

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
	    System.out.println("You need to choose a search type and enter a query to search.");
	}
    }

    private void handleSearch(Map<String, String> apiQueries) {

        //All of our search objects implement TmdbObject, "? extends ..." really means "is a subclass of ..."
	ResultsPager<? extends TmdbObject> searchResults = null;

	switch(currentSearchType) {
	    case MOVIES:
		searchResults = apiService.searchMovies(apiQueries);
		List<Movie> movies = (List<Movie>) searchResults.getResults();
		populateList(movies);
		break;
	    case SERIES:
	        searchResults = apiService.searchSeries(apiQueries);
		List<Series> series = (List<Series>) searchResults.getResults();
		populateList(series);
	        break;
	    case PEOPLE:
	        searchResults = apiService.searchPeople(apiQueries);
		List<Person> people = (List<Person>) searchResults.getResults();
		populateList(people);
	}

    }

    private void populateList(List<? extends TmdbObject> listObjects) {
	MediaType mediaType = listObjects.get(0).getMediaType();
	int searchPaneIndex = 0;
        switch(mediaType) {
	    case MOVIE:
		movieObservableList.clear();
  		movieObservableList.addAll((Collection<? extends Movie>) listObjects);
		movieListView.setItems(movieObservableList);
		movieListView.setCellFactory(listView -> new MovieListViewCell());
		for(int i = 0; i < 3; i++) { //find the index in the stackpane of our desired listview to show
		    if(searchPane.getChildren().get(i).getId().equals(MOVIE_LW_FX_ID)) {
			searchPaneIndex = i;
		    }
		}
	        break;
	    case SERIES:
		seriesObservableList.clear();
  		seriesObservableList.addAll((Collection<? extends Series>) listObjects);
		seriesListView.setItems(seriesObservableList);
		seriesListView.setCellFactory(listView -> new SeriesListViewCell());
		for(int i = 0; i < 3; i++) {
		    if(searchPane.getChildren().get(i).getId().equals(SERIES_LW_FX_ID)) {
			searchPaneIndex = i;
		    }
		}
	        break;
	    case PERSON:
		personObservableList.clear();
		personObservableList.addAll((Collection<? extends Person>) listObjects);
		personListView.setItems(personObservableList);
		personListView.setCellFactory(listView -> new PersonListViewCell());
		for(int i = 0; i < 3; i++) {
		    if(searchPane.getChildren().get(i).getId().equals(PERSON_LW_FX_ID)) {
			searchPaneIndex = i;
		    }
		}
	        break;
	}

	searchPane.getChildren().get(searchPaneIndex).toFront();

    }


    @Override public void setScreenParent(final ScreenController screenParent) {
	this.screenController = screenParent;
    }


}
