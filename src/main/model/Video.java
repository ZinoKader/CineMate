package main.model;

import com.google.gson.annotations.SerializedName;

public class Video {

    private static final String YOUTUBE_VIDEO_ENDPOINT = "https://www.youtube.com/embed/";
    private static final String YOUTUBE_EMBED_OPTIONS = "?modestbranding=1&rel=0&iv_load_policy=3&fs=0?disablekb=1";
    private static final String VIDEO_SITE_YOUTUBE = "YouTube";
    private static final String VIDEO_TYPE_TRAILER = "Trailer";

    @SerializedName("key")
    private String urlKey;

    @SerializedName("type")
    private String videoType;

    @SerializedName("site")
    private String videoSite;


    public Video() {
    }

    public boolean isYoutube() {
        return videoSite.equals(VIDEO_SITE_YOUTUBE);
    }

    public boolean isTrailer() {
	return videoType.equals(VIDEO_TYPE_TRAILER);
    }

    public String getVideoUrl() {
        return YOUTUBE_VIDEO_ENDPOINT + urlKey + YOUTUBE_EMBED_OPTIONS;
    }

}
