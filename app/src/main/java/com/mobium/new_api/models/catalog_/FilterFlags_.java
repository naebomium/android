package com.mobium.new_api.models.catalog_;

import com.mobium.client.models.filters.Filtering;

import java.util.HashSet;

/**
 *  on 15.07.15.
 * http://mobiumapps.com/
 */
public class FilterFlags_ extends Filter_ {
    public final HashSet<String> values;

    public FilterFlags_(Filtering.Type filter, String id, HashSet<String> values) {
        super(filter, id);
        this.values = values;
    }

}
