package main.api;

import retrofit.RestAdapter;
import retrofit.RestAdapter.Builder;
import retrofit.RestAdapter.LogLevel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Provides an appropriate rest adapter with our given TheMovieDB API endpoint
 */
public class ApiAdapater {


    /**
     * Movie WEB API base URL/endpoint
     */
    private static final String MOVIE_WEB_API_ENDPOINT = "https://api.themoviedb.org/3";
    private final ApiService apiService;
    private String apiKey;


    /**
     *  New instance of ApiAdapater,
     *  with single thread executor both for http and callbacks.
     */
    public ApiAdapater() {
        Executor httpExecutor = Executors.newSingleThreadExecutor();
        Executor callbackExecutor = Executors.newSingleThreadExecutor();
        apiService = init(httpExecutor, callbackExecutor);
    }

    private ApiService init(Executor httpExecutor, Executor callbackExecutor) {
        final RestAdapter restAdapter = new Builder()
                .setLogLevel(LogLevel.BASIC)
                .setExecutors(httpExecutor, callbackExecutor)
                .setEndpoint(MOVIE_WEB_API_ENDPOINT)
                .build();

         return restAdapter.create(ApiService.class);
    }


    /**
     * @return The ApiAdapater instance
     */
    public ApiService getApiService() {
        return apiService;
    }
}