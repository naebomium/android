package com.mobium.config.common;

/**
 *  on 02.11.15.
 */
public interface Updater<T> {
    void requestData(int limit, int offset);
}
