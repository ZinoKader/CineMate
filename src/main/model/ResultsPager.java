package main.model;

import com.google.gson.annotations.SerializedName;

import java.util.Iterator;
import java.util.List;

public class ResultsPager<T> implements Iterable<T> {

    @SerializedName("results")
    private List<T> results;

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("total_results")
    private int totalResults;
    
    public ResultsPager() {
    }

    @Override
    public Iterator<T> iterator() {
        return results.iterator();
    }

    public List<T> getResults() {
        return results;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

}