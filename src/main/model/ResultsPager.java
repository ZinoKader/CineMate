package main.model;

import com.google.gson.annotations.SerializedName;

import java.util.Iterator;
import java.util.List;

/**
 * Pager for a JSON result page from the API endpoint, which can be paged
 * Takes any param T and holds such data types and allows paging of these
 * Will be used in the future to allow paging of more result pages
 * @param <T> The data type to paginate
 */
@SuppressWarnings({"unused", "InstanceVariableMayNotBeInitialized"})
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