package com.mobium.config.common;

/**
 *  on 02.11.15.
 * View which can receive and show data and also
 * request updates as result some touch events
 * */
public interface UpdatableLoadableView<T> {
    void setData(T data, int offset);
    void setUpdater(Updater<T> updater);
    boolean hasData();
    void onEmptyData(int offset);
}
