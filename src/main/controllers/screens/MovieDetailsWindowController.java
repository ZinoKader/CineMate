package main.controllers.screens;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import main.api.ApiAdapater;
import main.api.ApiService;
import main.controllers.ControlledWindow;
import main.helpers.Log;
import main.model.AppendedQueries;
import main.model.Movie;
import main.model.TmdbQuery;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MovieDetailsWindowController implements Initializable, ControlledWindow {

    private List<Movie> movies = new ArrayList<>();
    ApiAdapater apiAdapater;
    ApiService apiService;

    @Override public void initialize(final URL location, final ResourceBundle resources) {
	apiAdapater = new ApiAdapater();
	apiService = apiAdapater.getService();

	Platform.runLater( () -> getDetailedData(movies.get(0).getId()));
    }

    private void getDetailedData(String objectId) {
        //TODO: In apiservice, create a query towards https://developers.themoviedb.org/3/movies/get-movie-details
	//Then, used append_to_response to append additional data such as videos, cast(credits) and more
	//TODO: Remove manual API key
	List<TmdbQuery> queries = new ArrayList<>();
	queries.addAll(Arrays.asList(TmdbQuery.CREDITS, TmdbQuery.SIMILAR, TmdbQuery.VIDEOS));
	AppendedQueries appendedQueries = new AppendedQueries(queries);
	Movie movie = apiService.getMovieDetailed(objectId, appendedQueries, "4b45808a4d1a83471866761a8d7e5325");
	Log.debug(movie.getCast().toString());
    }

    @Override public void setPassedData(final List<?> passedData) {

	for(Object object : passedData) {
	    if(object instanceof Movie) {
		movies.add((Movie) object);
	    }
	}
    }
}
