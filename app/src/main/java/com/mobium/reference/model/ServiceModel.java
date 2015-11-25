package com.mobium.reference.model;

import com.mobium.new_api.models.Region;
import com.mobium.new_api.models.ShopPoint;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *  on 24.07.15.
 * http://mobiumapps.com/
 */
public class ServiceModel implements Serializable {
    public final ArrayList<ShopPoint> services;
    public final String title;
    public final String urlIcon;
    public final Region region;

    public ServiceModel(ArrayList<ShopPoint> services, String title, String urlIcon, Region region) {
        this.services = services;
        this.title = title;
        this.urlIcon = urlIcon;
        this.region = region;
    }

}
