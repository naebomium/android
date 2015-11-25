package com.mobium.new_api.models.catalog_;

import com.mobium.client.models.filters.Filtering;

/**
 *  on 15.07.15.
 * http://mobiumapps.com/
 */
public class FilterSingle_ extends Filter_ {
    public final Boolean value;
    public FilterSingle_(Filtering.Type filter, String id, Boolean value) {
        super(filter, id);
        this.value = value;
    }
}
