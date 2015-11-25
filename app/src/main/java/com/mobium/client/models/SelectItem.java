package com.mobium.client.models;

import java.io.Serializable;

/**
 *  on 07.12.13.
 * http://mobiumapps.com/
 */
public class SelectItem implements Serializable {
    private String id;
    private String title;

    public SelectItem(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
