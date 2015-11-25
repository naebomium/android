package com.mobium.new_api.cache;

import com.mobium.new_api.models.ResponseBase;

/**
 *  on 16.07.15.
 * http://mobiumapps.com/
 */
public abstract class BaseCachedItem extends ResponseBase implements ICachedItem {
    private int cacheMinute;

    @Override
    public int getMinuteOfCaching() {
        return cacheMinute;
    }

    @Override
    public void putMinuteOfCaching(int second) {
        cacheMinute = second;
    }
}
