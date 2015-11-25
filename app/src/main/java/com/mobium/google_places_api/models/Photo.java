package com.mobium.google_places_api.models;

/**
 *  on 27.08.15.
 */
public class Photo {
    public int height;
    public int width;
    public int photo_reference;
    public int html_attributions;

    public Photo() {
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setPhoto_reference(int photo_reference) {
        this.photo_reference = photo_reference;
    }

    public void setHtml_attributions(int html_attributions) {
        this.html_attributions = html_attributions;
    }
}
