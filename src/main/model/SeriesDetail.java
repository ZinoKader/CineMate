package main.model;

/**
 * Marker interface for Seasons and Episodes
 * Allows us to make use of polymorphism to consider these objects the same
 * This helps in creating Season and Episode objects in the same Cell (SeasonTreeViewCell), as they are both SeriesDetail objects
 */
public interface SeriesDetail {
    TvType getType();
}
