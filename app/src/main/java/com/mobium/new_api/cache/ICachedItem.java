package com.mobium.new_api.cache;

import java.io.Serializable;

/**
 *  on 16.07.15.
 * http://mobiumapps.com/
 */
public interface ICachedItem  extends Serializable{
    int getTimeOutInMinutes();
    void putMinuteOfCaching(int minute);
    int getMinuteOfCaching();
}
