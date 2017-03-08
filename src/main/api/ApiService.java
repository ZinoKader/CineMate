package main.api;

import main.model.Movie;
import main.model.Person;
import main.model.ResultsPager;
import main.model.Series;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

import java.util.Map;


/**
 * Interface for MovieApi REST adapter
 */
public interface ApiService {


    /**
     * API key validation
     */

    @GET("/authentication/token/new")
    void getResponse(@Query("api_key") String apiKey, Callback<Response> responseCallback);

    /**
     * Searches for several objects
     * Since all parameters here are queries, we put them in a map
     */
    @GET("/search/movie")
    ResultsPager<Movie> searchMovies(@QueryMap Map<String, String> queries);

    @GET("/search/tv")
    void searchSeries(@QueryMap Map<String, String> queries, Callback<ResultsPager<Series>> callback);

    @GET("/search/pager")
    void searchPeople(@QueryMap Map<String, String> queries, Callback<ResultsPager<Person>> callback);


    /**
     * Gets specific objects
     * Since these only require one query, we do not need a QueryMap
     * These work by specifying
     */
    @GET("/movie/{movie_id}")
    Movie getMovie(@Path("movie_id") String movieId, @Query("api_key") String apiKey);

}
