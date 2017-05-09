package main.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Holds credits for a movie, can be used to receive a new Movie DAO with credits attached (AccreditedMovie)
 */
@SuppressWarnings({"unused", "InstanceVariableMayNotBeInitialized"})
public class MovieCredits {

    @SerializedName("cast")
    private List<AccreditedMovie> movies;

    public List<AccreditedMovie> getMovies() {
        return movies;
    }

}
