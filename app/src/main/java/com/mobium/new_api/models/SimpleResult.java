package com.mobium.new_api.models;

import org.parceler.Parcel;

/**
 *  on 19.06.15.
 * http://mobiumapps.com/
 */

public class SimpleResult extends ResponseBase {
    public String info;

    public SimpleResult(String info) {
        this.info = info;
    }

    public SimpleResult() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
