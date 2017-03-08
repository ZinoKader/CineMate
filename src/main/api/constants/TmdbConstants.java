package main.api.constants;


public class TmdbConstants {

    /**
     * Appending an integer to this URL creates a complete path to an image.
     * Example: http://image.tmdb.org/t/p/w500/27423b212f3.jpg
     */
    public static final String TMDB_IMAGE_ENDPOINT = "http://image.tmdb.org/t/p/w";

    /**
     *
     */
    public static final int DEFAULT_POSTER_SIZE = 300;
    public static final String POSTER_PLACEHOLDER = "https://www.zinokader.se/img/poster_placeholder.png";

    public static final int DEFAULT_BACKDROP_SIZE = 1280;
    public static final String BACKDROP_PLACEHOLDER = "https://www.zinokader.se/img/backdrop_placeholder.png";
}
