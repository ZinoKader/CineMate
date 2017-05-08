package main.controllers.implementation;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import main.CineMateApplication;
import main.api.ApiAdapater;
import main.api.ApiService;
import main.config.UserSettings;
import main.constants.FXConstants;
import main.controllers.ScreenController;
import main.controllers.contract.ControlledScreen;
import main.exceptions.EmptyValueException;
import main.exceptions.PropertyLoadException;
import main.helpers.MessageHelper;
import main.model.*;
import main.view.MovieListViewCell;
import main.view.PersonListViewCell;
import main.view.SeriesListViewCell;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller implementation for main application window
 */
@SuppressWarnings("InstanceVariableMayNotBeInitialized")
public class MainScreenController implements Initializable, ControlledScreen {

    @FXML
    private Parent rootParent;

    @FXML
    private StackPane rootPane;

	@FXML
	private JFXTextField searchTextField;

	@FXML
	private JFXComboBox<SearchType> searchTypeBox;

	@FXML
	private JFXListView<Movie> movieListView;
	private static final String MOVIE_LW_FX_ID = "movieListView";

	@FXML
	private JFXListView<Series> seriesListView;
	private static final String SERIES_LW_FX_ID = "seriesListView";

	@FXML
	private JFXListView<Person> personListView;
	private static final String PERSON_LW_FX_ID = "personListView";


	private ObservableList<Movie> movieObservableList = FXCollections.observableArrayList();
	private ObservableList<Series> seriesObservableList = FXCollections.observableArrayList();
	private ObservableList<Person> personObservableList = FXCollections.observableArrayList();

	private SearchType currentSearchType;
	private ScreenController screenParent;
	private UserSettings userSettings;
	private MessageHelper messageHelper;
	private ApiAdapater apiAdapter;
	private ApiService apiService;

    /**
	 * Types you can search for in the search field
	 */
	public enum SearchType {
		MOVIES, SERIES, PEOPLE
	}


	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		//init our choicebox with possible values. We add these items as observables so changes are broadcasted
		searchTypeBox.setItems(FXCollections.observableArrayList(SearchType.MOVIES, SearchType.SERIES, SearchType.PEOPLE));

		listenToSearchTypeChanges();
		listenToItemClicks(movieListView);
		listenToItemClicks(seriesListView);
		listenToItemClicks(personListView);
		delayLogin();

		messageHelper = new MessageHelper(rootPane);


        searchTextField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER) {
                searchButtonPressed(); //simulate button press (this function is bound to FXML)
            }
        });
    }

	@Override
	public void delayLogin() {
	    //Because of JavaFX tomfoolery, we have to observe the parent node to know when the window is shown
        Platform.runLater( () -> rootParent.sceneProperty().addListener((sceneObservable, oldScene, newScene) -> {
            //Reload user settings on window reload
            loadUserSettings();
            //Clear all search results on window reload
            clearAllSearchResults();
        }));
    }

    private void loadUserSettings() {
        try {
            userSettings = new UserSettings();
        } catch (PropertyLoadException e) {
            e.printStackTrace();
        }

        try {
            apiAdapter = new ApiAdapater(userSettings.getApiKey());
        } catch (EmptyValueException e) {
            e.printStackTrace();
        }

        apiService = apiAdapter.getApiService();
    }

    @FXML
    private void settingsButtonPressed() {
	    screenParent.loadWindow(CineMateApplication.SETTINGS_WINDOW_FXML, null);
    }


    private void listenToSearchTypeChanges() {
		searchTypeBox.getSelectionModel().selectedItemProperty().addListener( (observable, oldSearchType, newSearchType) ->  {
			currentSearchType = newSearchType;
			searchTextField.setPromptText("Search for " + currentSearchType.toString().toLowerCase());
		});
	}

	private void listenToItemClicks(ListView<?> listView) {
	    listView.setOnMouseClicked( clickEvent -> {
	        if(!listView.getSelectionModel().isEmpty() && clickEvent.getClickCount() == FXConstants.DOUBLE_CLICK_COUNT) {
				switch(currentSearchType) {
					case MOVIES:
						Movie selectedMovie = movieListView.getSelectionModel().getSelectedItem();
						screenParent.loadWindow(CineMateApplication.MOVIE_WINDOW_FXML, selectedMovie);
						break;
					case SERIES:
						Series selectedSeries = seriesListView.getSelectionModel().getSelectedItem();
						screenParent.loadWindow(CineMateApplication.SERIES_WINDOW_FXML, selectedSeries);
						break;
					case PEOPLE:
						Person selectedPerson = personListView.getSelectionModel().getSelectedItem();
						screenParent.loadWindow(CineMateApplication.PERSON_WINDOW_FXML, selectedPerson);
						break;
				}
			}
		});

	}

    @FXML
	private void searchButtonPressed() {
		if(!searchTextField.getText().isEmpty() && currentSearchType != null) {
            try {
                handleSearch(searchTextField.getText());
            } catch (IOException e) {
                messageHelper.showMessage("Could not fetch search results from TMDB");
                e.printStackTrace();
            }
        } else {
			messageHelper.showMessage("You need to choose a search type and enter a query to search");
		}
	}

	private void handleSearch(String query) throws IOException {
		//All of our search objects implement TmdbObject, "? extends ..." really means "is a subclass of ..."
		ResultsPager<? extends TmdbObject> searchResults = new ResultsPager<>();
		MediaType mediaType = MediaType.NONE;

		switch(currentSearchType) {
			case MOVIES:
				mediaType = MediaType.MOVIE;
				searchResults = apiService.searchMovies(query).execute().body();
				break;
			case SERIES:
				mediaType = MediaType.SERIES;
				searchResults = apiService.searchSeries(query).execute().body();
				break;
			case PEOPLE:
				mediaType = MediaType.PERSON;
				searchResults = apiService.searchPeople(query).execute().body();
				break;
		}

		if(searchResults != null) {
            if(searchResults.getResults() != null && !searchResults.getResults().isEmpty()) {
                populateList(searchResults.getResults(), mediaType);
            }
        } else {
		    messageHelper.showMessage("Something went wrong when searching, check your API key");
		    clearAllSearchResults();
        }

	}

	private void populateList(List<? extends TmdbObject> tmdbObjects, MediaType mediaType) {
		int searchPaneIndex = 0;
		List<Node> searchResultListView = rootPane.getChildren();

		//find the index in the stackpane of our desired listview to show (they get shuffled around as they're reordered)
        //about branch density, I don't agree here. I think a switch statement is appropriate given the clarity it provides,
        //switching between mediatypes and clearly handling them differently based on which mediatype it is.
        switch(mediaType) {
			case MOVIE:
                List<Movie> movies = tmdbObjects.stream().filter(tmdbItem -> tmdbItem.getMediaType().equals(MediaType.MOVIE))
                        .map(tmdbItem -> (Movie) tmdbItem).collect(Collectors.toList());
				movieObservableList.clear();
				movieObservableList.addAll(movies);
				movieListView.setItems(movieObservableList);
				movieListView.setCellFactory(listView -> new MovieListViewCell());
				for(int i = 0; i < searchResultListView.size(); i++) {
					if(searchResultListView.get(i).getId().equals(MOVIE_LW_FX_ID)) {
						searchPaneIndex = i;
						break;
					}
				}
				break;
			case SERIES:
                List<Series> series = tmdbObjects.stream().filter(tmdbItem -> tmdbItem.getMediaType().equals(MediaType.SERIES))
                        .map(tmdbItem -> (Series) tmdbItem).collect(Collectors.toList());
				seriesObservableList.clear();
				seriesObservableList.addAll(series);
				seriesListView.setItems(seriesObservableList);
				seriesListView.setCellFactory(listView -> new SeriesListViewCell());
				for(int i = 0; i < searchResultListView.size(); i++) {
					if(searchResultListView.get(i).getId().equals(SERIES_LW_FX_ID)) {
						searchPaneIndex = i;
						break;
					}
				}
				break;
			case PERSON:
                List<Person> people = tmdbObjects.stream().filter(tmdbItem -> tmdbItem.getMediaType().equals(MediaType.PERSON))
                        .map(tmdbItem -> (Person) tmdbItem).collect(Collectors.toList());
				personObservableList.clear();
				personObservableList.addAll(people);
				personListView.setItems(personObservableList);
				personListView.setCellFactory(listView -> new PersonListViewCell());
				for(int i = 0; i < searchResultListView.size(); i++) {
					if(searchResultListView.get(i).getId().equals(PERSON_LW_FX_ID)) {
						searchPaneIndex = i;
						break;
					}
				}
				break;
		}

		rootPane.getChildren().get(searchPaneIndex).toFront();

	}

	private void clearAllSearchResults() {
	    movieObservableList.clear();
	    seriesObservableList.clear();
	    personObservableList.clear();
    }

    @Override
	public void setScreenParent(ScreenController screenController) {
		this.screenParent = screenController;
	}


}
