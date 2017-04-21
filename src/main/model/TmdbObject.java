package main.model;

/**
 * Marker interface to allow for abstraction between Movies, Series and Persons
 */
public interface TmdbObject {

    public MediaType getMediaType();
    public String getId();

}
