package main.model;

import com.esotericsoftware.minlog.Log;
import com.google.gson.annotations.SerializedName;
import main.constants.TimeConstants;

import java.time.LocalTime;
import java.util.List;

@SuppressWarnings({"unused", "InstanceVariableMayNotBeInitialized"})
public class Series extends MotionPicture implements TmdbObject {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String title;

    @SerializedName("first_air_date")
    private String releaseDate;

    @SerializedName("number_of_seasons")
    private int numberOfSeasons;

    @SerializedName("episode_run_time")
    private List<Integer> episodeRunTimes;

    @SerializedName("recommendations")
    private SeriesRecommendationResults recommendations;

    public Series() {

    }

    @Override public MediaType getMediaType() {
        return MediaType.SERIES;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public LocalTime getRuntime() {
        if(episodeRunTimes == null || episodeRunTimes.isEmpty()) {
            return LocalTime.of(0, 0);
        } else {
            int runtimeAverage = episodeRunTimes.stream().mapToInt(runtime -> runtime / episodeRunTimes.size()).sum();
            Log.debug("Average: " + runtimeAverage + ". First: " + episodeRunTimes.get(0));
            int hours =  runtimeAverage / TimeConstants.MINUTES_IN_HOUR;
            int minutes = runtimeAverage % TimeConstants.MINUTES_IN_HOUR;
            return LocalTime.of(hours, minutes);
        }
    }

    public SeriesRecommendationResults getRecommendationResults() {
        return recommendations;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }
}
