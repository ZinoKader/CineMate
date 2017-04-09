package main.api;

import main.constants.TmdbConstants;
import main.model.AppendedQueries;
import main.model.Movie;
import main.model.Person;
import main.model.ResultsPager;
import main.model.Series;
import retrofit2.Call;
import retrofit2.http.*;


/**
 * Interface for ApiAdapater REST adapter
 */
public interface ApiService {


    /**
     * API key validation
     * Anything that isn't a HTTP 200 will count as a bad validation
     */

    @GET("authentication/token/new")
    Call<Void> getResponse();

    /**
     * Searches for specific objects
     */

    @GET("search/movie")
    Call<ResultsPager<Movie>> searchMovies(@Query(TmdbConstants.API_QUERY_PARAM) String query);

    @GET("search/tv")
    Call<ResultsPager<Series>> searchSeries(@Query(TmdbConstants.API_QUERY_PARAM) String query);

    @GET("search/person")
    Call<ResultsPager<Person>> searchPeople(@Query(TmdbConstants.API_QUERY_PARAM) String query);


    /**
     * Gets specific objects with more details
     * These work by specifying the object id and appending/bundling
     * several calls to the API to get a lot of details in one request
     */

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetailed(@Path("movie_id") String movieId, @Query(TmdbConstants.API_APPEND_PARAM) AppendedQueries queries);

    @GET("person/{person_id}")
    Call<Person> getPersonDetailed(@Path("person_id") String personId, @Query(TmdbConstants.API_APPEND_PARAM) AppendedQueries queries);

}
