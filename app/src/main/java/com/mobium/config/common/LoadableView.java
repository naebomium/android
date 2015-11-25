package com.mobium.config.common;

/**
 *  on 02.11.15.
 * View which can receive and show some data of type T
 */
public interface LoadableView<T> {
    void setData(T data);
    boolean hasData();
    void onEmptyData();
}
