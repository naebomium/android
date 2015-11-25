package com.mobium.reference.utils.statistics_new.data_receivers;

import android.support.annotation.Nullable;

/**
 *  on 29.09.15.
 */
public interface IAppDataReceiver {
    void onAppStart(@Nullable String appId);
    void onAppStartFromPush(String pushId);
}
