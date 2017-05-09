package main.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Holds credits for a series, can be used to receive a new Series DAO with credits attached (AccreditedSeries)
 */
@SuppressWarnings({"unused", "InstanceVariableMayNotBeInitialized"})
public class SeriesCredits {

    @SerializedName("cast")
    private List<AccreditedSeries> series;

    public List<AccreditedSeries> getSeries() {
        return series;
    }
}
