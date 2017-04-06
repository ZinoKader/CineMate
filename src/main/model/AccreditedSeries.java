package main.model;

import com.google.gson.annotations.SerializedName;

public class AccreditedSeries extends Series {

    @SerializedName("character")
    private String character;

    public String getCharacter() {
        if(!character.isEmpty()) {
            return character;
        } else {
            return "Character not available";
        }
    }

    @Override
    public MediaType getMediaType() {
        return MediaType.ACCREDITED_SERIES;
    }

}
