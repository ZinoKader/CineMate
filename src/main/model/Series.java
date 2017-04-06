package main.model;

import com.google.gson.annotations.SerializedName;

public class Series extends MotionPicture implements TmdbObject {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String title;

    public Series() {

    }

    @Override public MediaType getMediaType() {
        return MediaType.SERIES;
    }

    public String getTitle() {
        return title;
    }

}
