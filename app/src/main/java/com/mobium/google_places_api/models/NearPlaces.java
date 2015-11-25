package com.mobium.google_places_api.models;

/**
 *  on 27.08.15.
 */
public class NearPlaces {
    private Status status;
    private Place places[];

    public NearPlaces() {
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Place[] getPlaces() {
        return places;
    }

    public void setPlaces(Place[] places) {
        this.places = places;
    }
}
