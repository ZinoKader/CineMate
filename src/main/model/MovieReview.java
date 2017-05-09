package main.model;

import com.google.gson.annotations.SerializedName;

/**
 * Movie review DAO, only used for Movie as Series don't have reviews in the TMDB API
 */
@SuppressWarnings({"unused", "InstanceVariableMayNotBeInitialized"})
public class MovieReview {

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
