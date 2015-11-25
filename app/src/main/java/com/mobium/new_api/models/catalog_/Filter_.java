package com.mobium.new_api.models.catalog_;

import com.mobium.client.models.filters.Filtering;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 *  on 15.07.15.
 * http://mobiumapps.com/
 */
public abstract class Filter_ implements Serializable {
    public final Filtering.Type type;
    public final String id;

    public Filter_(Filtering.Type type, String id) {
        this.type = type;
        this.id = id;
    }

}
