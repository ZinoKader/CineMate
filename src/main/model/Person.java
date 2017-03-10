package main.model;

import com.google.gson.annotations.SerializedName;
import main.api.constants.TmdbConstants;

import java.util.List;

/**
 *
 */

public class Person implements TmdbObject {

    @SerializedName("name")
    private String name;

    @SerializedName("profile_path")
    private String profilePath;

    @SerializedName("known_for")
    private List<MotionPicture> knownFor;

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

    public List<MotionPicture> getKnownFor() {
	return knownFor;
    }
}
