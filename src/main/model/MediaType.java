package main.model;

/**
 * Very general DAO types that can be received from our TMDB API endpoint
 * MediaType.NONE helps with declaring a mediatype before deciding which one it is
 */
public enum MediaType {
    MOVIE, ACCREDITED_MOVIE, PERSON, SERIES, ACCREDITED_SERIES, NONE
}