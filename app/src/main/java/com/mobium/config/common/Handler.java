package com.mobium.config.common;

/**
 *  on 30.10.15.
 */
public interface Handler<T> {
    void onData(T t);
}
