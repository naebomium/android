package com.mobium.new_api.models;

/**
 *  on 19.06.15.
 * http://mobiumapps.com/
 *
 *
 * server error
 */
public class MobiumError {
    public final String error;
    public final String description;
    public final boolean mayRetry;

    public MobiumError(String error, String description, boolean mayRetry) {
        this.error = error;
        this.description = description;
        this.mayRetry = mayRetry;
    }
}
