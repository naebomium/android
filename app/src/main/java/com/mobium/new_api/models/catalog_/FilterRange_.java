package com.mobium.new_api.models.catalog_;

import com.mobium.client.models.filters.Filtering;

/**
 *  on 15.07.15.
 * http://mobiumapps.com/
 */
public class FilterRange_ extends Filter_ {
    public final Double value;
    public FilterRange_(Filtering.Type filter, String id, Double value) {
        super(filter, id);
        this.value = value;
    }
}
