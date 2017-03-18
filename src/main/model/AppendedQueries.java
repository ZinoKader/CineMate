package main.model;

import java.util.List;

public class AppendedQueries {

    private List<TmdbQuery> queries;

    public AppendedQueries(List<TmdbQuery> queries) {
        this.queries = queries;
    }

    @Override public String toString() {
	StringBuilder queryBuilder = new StringBuilder();
	for(TmdbQuery query : queries) {
	    queryBuilder.append(query.toString().toLowerCase()).append(",");
	}
	return queryBuilder.toString();
    }
}
