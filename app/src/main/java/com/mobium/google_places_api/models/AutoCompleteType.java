package com.mobium.google_places_api.models;


/**
 *  on 27.08.15.
 */
public enum AutoCompleteType {
    geocode("geocode"),
    address("address"),
    establishment("establishment"),
    cities("(cities)"),
    regions("(regions)");
    public final String serializedName;

    AutoCompleteType(String serializedName) {
        this.serializedName = serializedName;
    }
}
