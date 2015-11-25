package com.mobium.new_api.models;

import com.mobium.new_api.cache.BaseCachedItem;


import java.util.List;

/**
 *  on 23.06.15.
 * http://mobiumapps.com/
 */

public class RegionList extends BaseCachedItem {
    private Region.PlacesService service;
    private List<Region> regions;

    public RegionList() {
    }

    public Region.PlacesService getService() {
        return service;
    }

    public void setService(Region.PlacesService service) {
        this.service = service;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    @Override
    public int getTimeOutInMinutes() {
        return 60;
    }
}
