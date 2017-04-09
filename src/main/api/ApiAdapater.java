package main.api;

import main.constants.TimeConstants;
import main.constants.TmdbConstants;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;


/**
 * Provides an appropriate REST adapter with our given TheMovieDB API endpoint
 */
public class ApiAdapater {


    /**
     * Movie WEB API base URL/endpoint
     */
    private static final String MOVIE_WEB_API_ENDPOINT = "https://api.themoviedb.org/3/";
    private final ApiService apiService;
    private String apiKey;


    public ApiAdapater(String apiKey) {
        this.apiKey = apiKey;
        apiService = init();
    }

    private ApiService init() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        Interceptor apiKeyInterceptor = chain -> {
            HttpUrl interceptedUrl = chain.request().url().newBuilder()
                    .addQueryParameter(TmdbConstants.API_KEY_PARAM, apiKey)
                    .build();
            Request request = chain.request().newBuilder().url(interceptedUrl).build();
            return chain.proceed(request);
        };

        //custom okHttpClient for 60 seconds timeout and body level logging
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(TimeConstants.SECONDS_IN_MINUTE, TimeUnit.SECONDS)
                .connectTimeout(TimeConstants.SECONDS_IN_MINUTE, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(apiKeyInterceptor)
                .build();

        final Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(MOVIE_WEB_API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
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