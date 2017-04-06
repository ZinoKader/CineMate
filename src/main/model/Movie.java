package main.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalTime;

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

    @SerializedName("budget")
    private double budget;

    @SerializedName("revenue")
    private double revenue;


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

    public double getBudget() {
        return budget;
    }

    public double getRevenue() {
        return revenue;
    }

    //gets the first video from the available ones that is both a youtube video and of the trailer type
    public String getTrailerUrl() {
        for(Video video : getVideoResults().getVideos()) {
            if(video.isTrailer() && video.isYoutube()) {
                return video.getVideoUrl();
            }
        }
        return "";
    }
}
