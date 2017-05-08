package main.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings({"unused", "InstanceVariableMayNotBeInitialized"})
public class AccreditedMovie extends Movie {

    @SerializedName("character")
    private String character;

    public String getCharacter() {
        if(character.isEmpty()) {
            return "Character not available";
        }
        return character;
    }

    @Override
    public MediaType getMediaType() {
        return MediaType.ACCREDITED_MOVIE;
    }
}
