package main.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Holds recommendations for a series
 */
@SuppressWarnings({"unused", "InstanceVariableMayNotBeInitialized"})
public class SeriesRecommendationResults {

    @SerializedName("results")
    private List<Series> series;

    public List<Series> getSeries() {
        return series;
    }
}
