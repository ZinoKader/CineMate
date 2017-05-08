package main.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings({"unused", "InstanceVariableMayNotBeInitialized"})
public class AccreditedSeries extends Series {

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
        return MediaType.ACCREDITED_SERIES;
    }

}
