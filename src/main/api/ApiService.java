package main.api;

import main.model.Movie;
import main.model.Person;
import main.model.ResultsPager;
import main.model.Series;
import main.model.TmdbObject;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

import java.util.Map;


/**
 * Interface for ApiAdapater REST adapter
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


    //This is hard to get working...
    @GET("/search/multi")
    ResultsPager<TmdbObject> searchMulti(@QueryMap Map<String, String> queries);

    @GET("/search/movie")
    ResultsPager<Movie> searchMovies(@QueryMap Map<String, String> queries);

    @GET("/search/tv")
    ResultsPager<Series> searchSeries(@QueryMap Map<String, String> queries);

    @GET("/search/person")
    ResultsPager<Person> searchPeople(@QueryMap Map<String, String> queries);


    /**
     * Gets specific objects
     * Since these only require one query, we do not need a QueryMap
     * These work by specifying
     */
    @GET("/movie/{movie_id}")
    Movie getMovie(@Path("movie_id") String movieId, @Query("api_key") String apiKey);


}
