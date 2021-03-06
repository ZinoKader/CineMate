package main.constants;


public final class TmdbConstants {

    /** IMAGE RELATED CONSTANTS
     * Appending an integer to this URL creates a complete path to an image.
     * Example: http://image.tmdb.org/t/p/w500/27423b212f3.jpg
     */
    public static final String TMDB_IMAGE_ENDPOINT = "http://image.tmdb.org/t/p/w";

    public static final int DEFAULT_POSTER_SIZE = 500;
    public static final String POSTER_PLACEHOLDER = "https://www.zinokader.se/img/poster_placeholder.png";

    public static final int DEFAULT_BACKDROP_SIZE = 1280;
    public static final String BACKDROP_PLACEHOLDER = "https://www.zinokader.se/img/backdrop_placeholder.png";

    public static final String TRAILER_PLACEHOLDER = "https://www.zinokader.se/img/trailer_placeholder.png";

    /**
     * GENERAL CONSTANTS
     */

    public static final String API_KEY_PARAM = "api_key";
    public static final String API_APPEND_PARAM = "append_to_response";
    public static final String API_QUERY_PARAM = "query";

    public static final int MAX_AVERAGE_RATING = 10;

    private TmdbConstants() {}

}
