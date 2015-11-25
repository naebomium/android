package com.mobium.reference.utils.statistics_new;

import android.app.Activity;
import android.app.Application;

/**
 *  on 26.09.15.
 */
public interface ILifeCycleListener {
    void onStart(Activity context);
    void onStop(Activity context);
    void onApplicationStart(Application application);
}
