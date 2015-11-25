package com.mobium.config.prototype;

import android.support.annotation.NonNull;

/**
 *  on 14.11.15.
 */
public interface IStaticData {
    @NonNull String appName();
    @NonNull String packageName();
    @NonNull String mobiumBuildId();
    @NonNull String mobiumApiUrl();
    @NonNull String mobiumApiProtocol();
    @NonNull String mobiumApiKey();
}
