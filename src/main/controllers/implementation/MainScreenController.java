package main.controllers.implementation;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import main.CineMateApplication;
import main.api.ApiAdapater;
import main.api.ApiService;
import main.config.UserSettings;
import main.constants.FXConstants;
import main.controllers.ScreenController;
import main.controllers.contract.ControlledScreen;
import main.exceptions.PropertyAccessException;
import main.exceptions.PropertyLoadException;
import main.helpers.MessageHelper;
import main.helpers.QueryHelper;
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
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable, ControlledScreen {

	@FXML
	private JFXTextField searchTextField;

	@FXML
	private JFXComboBox<SearchType> searchTypeBox;

	@FXML
	private StackPane searchPane;

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
	private ScreenController screenController;
	private UserSettings userSettings;
	private MessageHelper messageHelper;
	private ApiAdapater apiAdapter;
	private ApiService apiService;

	/**
	 * Types you can search for in the searchfield
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

		messageHelper = new MessageHelper(searchPane);

		try {
			userSettings = new UserSettings();
		} catch (PropertyLoadException e) {
			e.printStackTrace();
		}
		apiAdapter = new ApiAdapater();
		apiService = apiAdapter.getApiService();


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
						screenController.loadWindow(CineMateApplication.MOVIE_WINDOW_FXML, selectedMovie);
						break;
					case SERIES:
						Series selectedSeries = seriesListView.getSelectionModel().getSelectedItem();
						screenController.loadWindow(CineMateApplication.SERIES_WINDOW_FXML, selectedSeries);
						break;
					case PEOPLE:
						Person selectedPerson = personListView.getSelectionModel().getSelectedItem();
						screenController.loadWindow(CineMateApplication.PERSON_WINDOW_FXML, selectedPerson);
						break;
				}
			}
		});

	}

	public void searchButtonPressed() throws PropertyAccessException {
		if(!searchTextField.getText().isEmpty() && currentSearchType != null) {
			handleSearch(QueryHelper.createQueryMap(userSettings.getApiKey(), searchTextField.getText()));
		} else {
			messageHelper.showMessage("You need to choose a search type and enter a query to search");
		}
	}

	private void handleSearch(Map<String, String> apiQueries) {
		//All of our search objects implement TmdbObject, "? extends ..." really means "is a subclass of ..."
		ResultsPager<? extends TmdbObject> searchResults = new ResultsPager<>();
		MediaType mediaType = MediaType.NONE;

		switch(currentSearchType) {
			case MOVIES:
				mediaType = MediaType.MOVIE;
				searchResults = apiService.searchMovies(apiQueries);
				break;
			case SERIES:
				mediaType = MediaType.SERIES;
				searchResults = apiService.searchSeries(apiQueries);
				break;
			case PEOPLE:
				mediaType = MediaType.PERSON;
				searchResults = apiService.searchPeople(apiQueries);
				break;
		}

		if(!searchResults.getResults().isEmpty()) {
			populateList(searchResults.getResults(), mediaType);
		} else {
			messageHelper.showMessage("No results for this query");
		}

	}

	@SuppressWarnings("unchecked")
	private void populateList(List<? extends TmdbObject> listObjects, MediaType mediaType) {
		int searchPaneIndex = 0;
		List<Node> searchResultListView = searchPane.getChildren();

		//find the index in the stackpane of our desired listview to show (they get shuffled around as they're reordered)
		switch(mediaType) {
			case MOVIE:
				movieObservableList.clear();
				movieObservableList.addAll((List<? extends Movie>) listObjects);
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
				seriesObservableList.clear();
				seriesObservableList.addAll((List<? extends Series>) listObjects);
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
				personObservableList.clear();
				personObservableList.addAll((List<? extends Person>) listObjects);
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

		searchPane.getChildren().get(searchPaneIndex).toFront();

	}


	@Override
	public void setScreenParent(final ScreenController screenController) {
		this.screenController = screenController;
	}


}
