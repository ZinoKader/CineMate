package main.api;

import main.model.AppendedQueries;
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

    @GET("/search/movie")
    ResultsPager<Movie> searchMovies(@QueryMap Map<String, String> queries);

    @GET("/search/tv")
    ResultsPager<Series> searchSeries(@QueryMap Map<String, String> queries);

    @GET("/search/person")
    ResultsPager<Person> searchPeople(@QueryMap Map<String, String> queries);


    /**
     * Gets specific objects with more details
     * Since these only require one query, we do not need a QueryMap
     * These work by specifying the object id and appending/bundling
     * several calls to the API to get a lot of details in one request
     */
    @GET("/movie/{movie_id}")
    Movie getMovieDetailed(@Path("movie_id") String movieId,
	@Query("append_to_response") AppendedQueries queries, @Query("api_key") String apiKey);

    @GET("/person/{person_id}")
    Person getPersonDetailed(@Path("person_id") String personId,
        @Query("append_to_response") AppendedQueries queries, @Query("api_key") String apiKey);

}
