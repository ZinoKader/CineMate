package main.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalTime;
import java.util.List;

/**
 * Contains Movie-specific fields.
 */
public class Movie extends MotionPicture implements TmdbObject {

    private static final int MINS_IN_HOUR = 60;

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

    @SerializedName("credits")
    private Credits credits;

    @SerializedName("budget")
    private int budget;

    @SerializedName("revenue")
    private int revenue;


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

    public LocalTime getRuntime() {
        int hours = runtime / MINS_IN_HOUR;
        int minutes = runtime % MINS_IN_HOUR;
	return LocalTime.of(hours, minutes);
    }

    @Override public MediaType getMediaType() {
	return MediaType.MOVIE;
    }

    public List<Cast> getCast() {
        return credits.getCast();
    }

    public List<Crew> getCrew() {
        return credits.getCrew();
    }

    public int getBudget() {
	return budget;
    }

    public int getRevenue() {
	return revenue;
    }
}
