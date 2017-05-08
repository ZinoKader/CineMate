package main.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings({"unused", "InstanceVariableMayNotBeInitialized"})
public class SeriesCredits {

    @SerializedName("cast")
    private List<AccreditedSeries> series;

    public List<AccreditedSeries> getSeries() {
        return series;
    }
}
