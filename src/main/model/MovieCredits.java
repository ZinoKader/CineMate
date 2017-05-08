package main.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings({"unused", "InstanceVariableMayNotBeInitialized"})
public class MovieCredits {

    @SerializedName("cast")
    private List<AccreditedMovie> movies;

    public List<AccreditedMovie> getMovies() {
        return movies;
    }

}
