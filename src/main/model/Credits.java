package main.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Credits for a MotionPicture, holds cast and crew
 */
@SuppressWarnings({"unused", "InstanceVariableMayNotBeInitialized"})
public class Credits {

    @SerializedName("cast")
    private List<Cast> cast;

    @SerializedName("crew")
    private List<Crew> crew;

    public Credits() {
    }

    public List<Cast> getCast() {
        return cast;
    }

    public List<Crew> getCrew() {
        return crew;
    }

}
