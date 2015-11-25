package com.mobium.config.prototype;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

/**
 *  on 14.11.15.
 */
public interface IDesign {
    @NonNull INavigationBar defaultNavigationBar();
    @NonNull INavigationBar getNavigationBarForSreen(IScreen screen);
    @NonNull Set<IScreen> screens();
    @NonNull ILeftMenu getLeftMenu();
}
