package main.model;

import com.google.gson.annotations.SerializedName;
import main.constants.TmdbConstants;

import java.util.List;

/**
 *
 */

public class Person implements TmdbObject {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("profile_path")
    private String profilePath;

    @SerializedName("biography")
    private String biograhy;

    @SerializedName("birthday")
    private String birthday;

    @SerializedName("deathday")
    private String deathday;

    @SerializedName("place_of_birth")
    private String birthplace;

    @SerializedName("movie_credits")
    private MovieCredits movieCredits;

    @SerializedName("tv_credits")
    private SeriesCredits seriesCredits;

    public Person() {

    }

    @Override public MediaType getMediaType() {
        return MediaType.PERSON;
    }

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        if(profilePath == null) {
            return TmdbConstants.POSTER_PLACEHOLDER;
        } else {
            return TmdbConstants.TMDB_IMAGE_ENDPOINT + TmdbConstants.DEFAULT_POSTER_SIZE + profilePath;
        }
    }

    public String getId() {
        return id;
    }

    public String getBiograhy() {
        return biograhy;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public MovieCredits getMovieCredits() {
        return movieCredits;
    }

    public SeriesCredits getSeriesCredits() {
        return seriesCredits;
    }
}
