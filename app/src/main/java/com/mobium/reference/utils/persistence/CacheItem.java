package com.mobium.reference.utils.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 *  on 19.05.15.
 * http://mobiumapps.com/
 */
public class CacheItem<I> implements Serializable {
    public I item;
    public long addedTime;

    public CacheItem(I item) {
        addedTime = (int) (new Date().getTime() / 1000);
        this.item = item;
    }

    public boolean isTimedOut(int itemTimeout) {
        int currentTime = (int) (new Date().getTime() / 1000);
        return currentTime - addedTime > itemTimeout;
    }

    public I getItem() {
        return item;
    }
}
