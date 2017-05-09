package main.model;

import com.google.gson.annotations.SerializedName;
import main.constants.TimeConstants;
import main.constants.TmdbConstants;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Person DAO derived straight from TMDB API
 * Is a TmdbObject along with Movie and Series to make use of polymorphism when using these DAO in lists together
 */
@SuppressWarnings({"unused", "InstanceVariableMayNotBeInitialized"})
public class Person implements TmdbObject {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("profile_path")
    private String profilePath;

    @SerializedName("biography")
    private String biograhy;

    @SerializedName("birthday")
    private String birthday;

    @SerializedName("deathday")
    private String deathday;

    @SerializedName("place_of_birth")
    private String birthplace;

    @SerializedName("movie_credits")
    private MovieCredits movieCredits;

    @SerializedName("tv_credits")
    private SeriesCredits seriesCredits;

    public Person() {

    }

    @Override public MediaType getMediaType() {
        return MediaType.PERSON;
    }

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        if(profilePath == null) {
            return TmdbConstants.POSTER_PLACEHOLDER;
        }
        return TmdbConstants.TMDB_IMAGE_ENDPOINT + TmdbConstants.DEFAULT_POSTER_SIZE + profilePath;
    }

    public String getId() {
        return id;
    }

    public String getBiograhy() {
        return biograhy;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public boolean isDead() {
        return !deathday.isEmpty();
    }

    public int getAge() {

        if(birthday == null || deathday == null && !birthday.isEmpty()) return -1;

        LocalDate birthdayDate = LocalDate.parse(birthday, TimeConstants.YEAR_MONTH_DAY_FORMAT);

        if(isDead()) {
            LocalDate deathdayDate = LocalDate.parse(deathday, TimeConstants.YEAR_MONTH_DAY_FORMAT);
            return (int) (Duration.between(birthdayDate.atStartOfDay(), deathdayDate.atStartOfDay()).toDays() / TimeConstants.DAYS_IN_YEAR);
        } else {
            return (int) (Duration.between(birthdayDate.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays() / TimeConstants.DAYS_IN_YEAR);
        }
    }

    public MovieCredits getMovieCredits() {
        return movieCredits;
    }

    public SeriesCredits getSeriesCredits() {
        return seriesCredits;
    }

}
