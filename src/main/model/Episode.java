package main.model;

import com.google.gson.annotations.SerializedName;
import main.constants.TmdbConstants;

/**
 * An Episode of a Series
 */
@SuppressWarnings({"unused", "InstanceVariableMayNotBeInitialized"})
public class Episode implements SeriesDetail {

    @SerializedName("air_date")
    private String airDate;

    @SerializedName("episode_number")
    private int episodeNumber;

    @SerializedName("name")
    private String episodeTitle;

    @SerializedName("overview")
    private String description;

    @SerializedName("still_path")
    private String posterPath;

    public Episode() {
    }

    public String getAirDate() {
        return airDate;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public String getEpisodeTitle() {
        return episodeTitle;
    }

    public String getDescription() {
        return description;
    }

    public String getPosterPath() {
        if(posterPath == null) {
            return TmdbConstants.POSTER_PLACEHOLDER;
        }
        return TmdbConstants.TMDB_IMAGE_ENDPOINT + TmdbConstants.DEFAULT_POSTER_SIZE + posterPath;
    }

    @Override
    public TvType getType() {
        return TvType.EPISODE;
    }
}
