package com.martinfilliau.javaeetrucs.services.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Useful to build parameters for a query.
 * Should be used with this syntax:
 * with("param", 1).and("param2", "ok");
 * Use with import static QueryParameter.*;
 * @author martin filliau
 * @see <a href="http://www.adam-bien.com/roller/abien/entry/generic_crud_service_aka_dao">Adam Bien blog</a>
 */
public final class QueryParameter {

    private Map parameters = null;

    /**
     * Initiate a new Map and put a new parameter
     * @param name - parameter name
     * @param value - parameter value
     */
    private QueryParameter(String name,Object value){
        this.parameters = new HashMap();
        this.parameters.put(name, value);
    }

    /**
     * Static method to initiate a new map with a new parameter
     * Cosmetic.
     * @param name - parameter name
     * @param value - parameter value
     * @return QueryParameter object
     */
    public static QueryParameter with(String name,Object value){
        return new QueryParameter(name, value);
    }

    /**
     * Add a new parameter to the map
     * @param name - parameter name
     * @param value - parameter value
     * @return QueryParameter object
     */
    public QueryParameter and(String name,Object value){
        this.parameters.put(name, value);
        return this;
    }

    /**
     * Get the map
     * @return Map containing the parameters
     */
    public Map parameters(){
        return this.parameters;
    }
}
