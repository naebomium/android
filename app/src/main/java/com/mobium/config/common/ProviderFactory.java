package com.mobium.config.common;

import android.support.v4.app.FragmentActivity;

/**
 *  on 02.11.15.
 */
public interface ProviderFactory {
    <T> Provider<T> get(FragmentActivity activity, LoadableView<T> view);
    <T> UpdatesProvider<T> get(FragmentActivity activity, UpdatableLoadableView<T> view);
}
