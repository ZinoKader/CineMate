package main.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Serializes video results into a list of videos
 */
public class VideoResults {

    @SerializedName("results")
    private List<Video> videos;

    public Video getFirstVideo() {
        return videos.get(0);
    }

    public List<Video> getVideos() {
        return videos;
    }

}
