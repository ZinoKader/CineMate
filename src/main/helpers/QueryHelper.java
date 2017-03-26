package main.helpers;

import java.util.HashMap;
import java.util.Map;

/**
 * Helps us keep controllers who need to send query maps to the apiservice clean of abstract code.
 * Also handles
 */
public final class QueryHelper {

    //Static because we don't want/need an instance of QueryHelper to hold fields, both method args are constantly changing
    public static Map<String, String> createQueryMap(String apiKey, String query) {
	Map<String, String> queries = new HashMap<>();
	//TODO: Remove this when compiling
	queries.put("api_key", "4b45808a4d1a83471866761a8d7e5325");
	//and replace with this: apiQueries.put("api_key", apiKey);
	queries.put("query", query);
	return queries;
    }

}
