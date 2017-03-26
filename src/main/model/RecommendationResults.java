package main.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecommendationResults {

    @SerializedName("results")
    private List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }

}
