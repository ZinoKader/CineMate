package main.model;

/**
 * Marker interface to allow for abstraction between Movies, Series and Persons
 */
public interface TmdbObject {
    MediaType getMediaType();
    String getId();
}
