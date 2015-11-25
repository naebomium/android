package com.mobium.new_api.models.catalog_;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 *  on 14.07.15.
 * http://mobiumapps.com/
 */

public class Media_ implements Serializable {
    private Type type;
    private String url;


    public enum Type implements Serializable {
        video, audio
    }

    public Media_() {
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
