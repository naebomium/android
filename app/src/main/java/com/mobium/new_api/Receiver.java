package com.mobium.new_api;

/**
 *  on 20.06.15.
 * http://mobiumapps.com/
 */
public interface Receiver<Data> {
    void onResult(Data data);
}
