package main.model;

import com.google.gson.annotations.SerializedName;
import main.constants.TimeConstants;

import java.time.LocalTime;

/**
 * Contains Movie-specific fields.
 */
@SuppressWarnings({"unused", "InstanceVariableMayNotBeInitialized"})
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

    @SerializedName("budget")
    private double budget;

    @SerializedName("revenue")
    private double revenue;

    @SerializedName("reviews")
    private ResultsPager<MovieReview> reviews;

    @SerializedName("recommendations")
    private MovieRecommendationResults recommendations;


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

    public LocalTime getRuntime() {
        int hours = runtime / TimeConstants.MINUTES_IN_HOUR;
        int minutes = runtime % TimeConstants.MINUTES_IN_HOUR;
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

    public ResultsPager<MovieReview> getReviews() {
        return reviews;
    }

    public MovieRecommendationResults getRecommendationResults() {
        return recommendations;
    }

}
