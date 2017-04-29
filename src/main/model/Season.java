package main.model;

import com.google.gson.annotations.SerializedName;
import main.constants.TmdbConstants;

import java.util.List;

public class Season implements SeriesDetail {

    @SerializedName("air_date")
    private String airDate;

    @SerializedName("season_number")
    private int seasonNumber;

    @SerializedName("overview")
    private String description;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("episodes")
    private List<Episode> episodes;


    public Season() {
    }


    public int getSeasonNumber() {
        return seasonNumber;
    }

    public String getDescription() {
        return description;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }


    public String getAirDate() {
        return airDate;
    }

    public String getPosterPath() {
        if(posterPath == null) {
            return TmdbConstants.POSTER_PLACEHOLDER;
        }
        return TmdbConstants.TMDB_IMAGE_ENDPOINT + TmdbConstants.DEFAULT_POSTER_SIZE + posterPath;
    }

    @Override
    public TvType getType() {
        return TvType.SEASON;
    }
}
