package com.mobium.config.prototype;

import android.support.annotation.Nullable;

/**
 *  on 16.11.15.
 */
public interface IScreen {
    String title();
    @Nullable INavigationBar navigationBar();
}
