package main.model;


import com.google.gson.annotations.SerializedName;
import main.constants.TmdbConstants;

import java.util.List;

/**
 * This class would be abstract if we didn't need to keep an instance of it in our Person class.
 * Gson needs to be able to create an instance of MotionPicture so it can serialize to a List of MotionPicture in Person
 */
public class MotionPicture {

    public MotionPicture() {
    }

    @SerializedName("overview")
    private String description;

    @SerializedName("vote_average")
    private float averageRating;

    @SerializedName("credits")
    private Credits credits;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("videos")
    private VideoResults videos;

    @SerializedName("recommendations")
    private RecommendationResults recommendations;

    public String getDescription() {
	return description;
    }


    public String getAverageRating() {
	return String.valueOf(averageRating);
    }


    public List<Cast> getCast() {
        return credits.getCast();
    }

    public List<Crew> getCrew() {
        return credits.getCrew();
    }

    public String getPosterPathRaw() {
        return posterPath;
    }

    public String getPosterPath() {
        if(posterPath == null) {
	    return TmdbConstants.POSTER_PLACEHOLDER;
	} else {
	    return TmdbConstants.TMDB_IMAGE_ENDPOINT + TmdbConstants.DEFAULT_POSTER_SIZE + posterPath;
	}
    }

    public String getBackdropPathRaw() {
        return backdropPath;
    }

    public String getBackdropPath() {
        if(backdropPath == null) {
	    return TmdbConstants.BACKDROP_PLACEHOLDER;
	} else {
	    return TmdbConstants.TMDB_IMAGE_ENDPOINT + TmdbConstants.DEFAULT_BACKDROP_SIZE + backdropPath;
	}
    }

    public VideoResults getVideoResults() {
        return videos;
    }

    public RecommendationResults getRecommendationResults() {
        return recommendations;
    }


}
