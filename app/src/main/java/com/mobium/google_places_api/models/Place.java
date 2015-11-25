package com.mobium.google_places_api.models;

/**
 *  on 27.08.15.
 */
public class Place {
    private String name;
    private String place_id;
    private NearPlaceType[] types;
    private String icon;
    private String scope;
    private OpeningHours openingHours;
    private Photo[] photos;
    private byte price_level;
    private float rating;

    public Place() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public void setTypes(NearPlaceType[] types) {
        this.types = types;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public void setPhotos(Photo[] photos) {
        this.photos = photos;
    }

    public void setPrice_level(byte price_level) {
        this.price_level = price_level;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public NearPlaceType[] getTypes() {
        return types;
    }


    public String getIcon() {
        return icon;
    }

    public String getScope() {
        return scope;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public Photo[] getPhotos() {
        return photos;
    }

    public byte getPrice_level() {
        return price_level;
    }
}
