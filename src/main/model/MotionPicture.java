package main.model;


import com.google.gson.annotations.SerializedName;
import main.api.constants.TmdbConstants;

/**
 * Abstract class for shared values between Movies and TV-series.
 */
public abstract class MotionPicture {

    @SerializedName("id")
    private String id;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("overview")
    private String description;

    @SerializedName("vote_average")
    private float averageRating;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;


    /**
     * These getters are used by Handlebars (HTML templating engine)
     * Handlebar will use the getter that matches the field name automatically,
     * therefore the fields that need reformatting are formatted in the original getters,
     * but have getSomethingRaw() functions that return the original, unformatted value in case they are needed.
     * Even though not all of these functions are used currently, it's good practice to make all private fields reachable
     * from their respective getters.
     */


    public String getId() {
	return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

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
