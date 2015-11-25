package com.mobium.config.prototype;

import android.support.annotation.NonNull;

import com.mobium.config.StringConstants;

/**
 *  on 14.11.15.
 */
public interface IConfiguration {
    @NonNull IStaticData getStaticData();
    @NonNull IApplicationData getApplicationData();
    @NonNull IColors getColors();
    @NonNull IDesign design();

    boolean logDebugInfo();

    @NonNull StringConstants strings();
}
