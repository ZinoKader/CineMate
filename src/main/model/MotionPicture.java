package main.model;


import com.google.gson.annotations.SerializedName;
import main.constants.TmdbConstants;

import java.util.List;

/**
 * Abstraction for Movie and Series, holding common fields for these classes.
 * This class would be abstract if we didn't need to keep an instance of it in our Person class for castIn and knownFor.
 * Gson needs to be able to create an instance of MotionPicture so it can serialize to a List of MotionPicture in Person
 */
public class MotionPicture {

    public MotionPicture() {
    }

    private MediaType mediaType;

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

    public String getPosterPath() {
        if(posterPath == null) {
            return TmdbConstants.POSTER_PLACEHOLDER;
        } else {
            return TmdbConstants.TMDB_IMAGE_ENDPOINT + TmdbConstants.DEFAULT_POSTER_SIZE + posterPath;
        }
    }

    public String getBackdropPath() {
        if(backdropPath == null) {
            return TmdbConstants.BACKDROP_PLACEHOLDER;
        } else {
            return TmdbConstants.TMDB_IMAGE_ENDPOINT + TmdbConstants.DEFAULT_BACKDROP_SIZE + backdropPath;
        }
    }

    //gets the first video from the available ones that is both a youtube video and of the trailer type
    public String getTrailerUrl() {
        for(Video video : videos.getVideos()) {
            if(video.isTrailer() && video.isYoutube()) {
                return video.getVideoUrl();
            }
        }
        return TmdbConstants.TRAILER_PLACEHOLDER;
    }

    public MediaType getMediaType() {
        return mediaType;
    }


}
