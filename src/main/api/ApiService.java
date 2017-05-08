package main.api;

import main.constants.TmdbConstants;
import main.model.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Interface for ApiAdapater REST adapter
 * This is the standard way of implementing REST API service interfaces.
 * This interface doesn't have a concrete subclass by design.
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

    @GET("tv/{tv_id}")
    Call<Series> getSeriesDetailed(@Path("tv_id") String seriesId, @Query(TmdbConstants.API_APPEND_PARAM) AppendedQueries queries);

    @GET("tv/{tv_id}/season/{season_number}")
    Call<Season> getSeriesSeason(@Path("tv_id") String seriesId, @Path("season_number") int seasonNumber);

}
