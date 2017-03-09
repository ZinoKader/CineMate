package main.model;

import com.google.gson.annotations.SerializedName;

/**
 * Contains Movie-specific fields.
 */
public class Movie extends MotionPicture implements TmdbObject {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("runtime")
    private int runtime;

    @SerializedName("adult")
    private boolean adult;

    @SerializedName("release_date")
    private String releaseDate;

    public Movie() {
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
	return id;
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

    @Override public MediaType getMediaType() {
	return MediaType.MOVIE;
    }
}
