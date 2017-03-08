package main.model;

import com.google.gson.annotations.SerializedName;

/**
 * Contains Movie-specific fields.
 */
public class Movie extends MotionPicture {

    @SerializedName("runtime")
    private int runtime;

    @SerializedName("adult")
    private boolean adult;

    @SerializedName("release_date")
    private String releaseDate;

    public Movie() {
    }

    public boolean isAdult() {
	return adult;
    }

    public String getReleaseDate() {
	return releaseDate;
    }

    public int getRunTimeRaw() {
        return runtime;
    }

    public String getRuntime() {
        int hours = runtime / 60;
        int minutes = runtime % 60;
	return hours + " hours" + " and " + minutes + " minutes";
    }
}
