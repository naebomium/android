package com.mobium.new_api.models;

import org.parceler.Parcel;

/**
 *  on 24.06.15.
 * http://mobiumapps.com/
 */


public class ImageMap {
    public String sd;
    public String hd;

    public ImageMap(String sd, String hd) {
        this.sd = sd;
        this.hd = hd;
    }

    public ImageMap() {
    }

    public String getSd() {
        return sd;
    }

    public void setSd(String sd) {
        this.sd = sd;
    }

    public String getHd() {
        return hd;
    }

    public void setHd(String hd) {
        this.hd = hd;
    }
}
