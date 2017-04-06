package main.model;

import java.util.List;

/**
 * Simplifies and abstractifies creating and chaining query paramaters ready for JSON requests
 */
public class AppendedQueries {

    private List<TmdbQuery> queries;

    public AppendedQueries(List<TmdbQuery> queries) {
        this.queries = queries;
    }

    /**
     * Formats the string into a json-request-friendly format
     * @return String with formatted query list separated by commas
     */
    @Override
    public String toString() {
        StringBuilder queryBuilder = new StringBuilder();
        for(TmdbQuery query : queries) {
            queryBuilder.append(query.toString().toLowerCase()).append(",");
        }
        return queryBuilder.toString();
    }
}
