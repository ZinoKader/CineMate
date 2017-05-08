package main.api;

import main.constants.TimeConstants;
import main.constants.TmdbConstants;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
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
        loggingInterceptor.setLevel(Level.BASIC);

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

        final Retrofit restAdapter = new Builder()
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