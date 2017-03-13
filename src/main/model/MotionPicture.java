package main.model;


import com.google.gson.annotations.SerializedName;
import main.api.constants.TmdbConstants;

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

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    public String getDescription() {
	return description;
    }

    public float getAverageRatingRaw() {
        return averageRating;
    }

    public String getAverageRating() {
        if(averageRating == 0.0) {
            //If the rating is 0.0, more than very likely there just aren't any ratings
	    //with inheritance magic, we can figure out and print out the specific MotionPicture-type for which...
	    //...no ratings could be found
            return "No ratings could be found for this " + this.getClass().getSimpleName().toLowerCase() + ".";
	} else {
	    return String.valueOf(averageRating);
	}
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


}
