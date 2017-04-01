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

    @SerializedName("known_for")
    private List<MotionPicture> knownFor;

    @SerializedName("biography")
    private String biograhy;

    @SerializedName("birthday")
    private String birthday;

    @SerializedName("deathday")
    private String deathday;

    @SerializedName("place_of_birth")
    private String birthplace;

    @SerializedName("cast")
    private List<Cast> castIn;

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

    public String getId() {
	return id;
    }
}
