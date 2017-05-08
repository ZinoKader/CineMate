package main.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings({"unused", "InstanceVariableMayNotBeInitialized"})
public class MovieRecommendationResults {

    @SerializedName("results")
    private List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }
}
