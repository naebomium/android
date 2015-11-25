package com.mobium.config;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mobium.config.prototype.IConfiguration;
import com.mobium.config.screen_models.Screen;

/**
 *  on 04.08.15.
 * http://mobiumapps.com/
 */
public interface ShopConfiguration {
    String getShopUrl();
    String getBuildId();
    String getProfileUrl();
    String getMainNumber();
    String[] getSenderId();

    @Nullable String getFlurryId();
    @Nullable String getGoogleAnalyticsId();
    @Nullable String getMatConversionKey();
    @Nullable String getMatAdvertiserId();
    @Nullable String getPlaceApiKey();
    @Nullable String getGoogleYouTubeApiKey();
    @Nullable String getHockeyappKey();

    @NonNull
    StringConstants strings();

    @NonNull
    Screens screens();


    boolean isShowPrices();

    boolean logDebugInfo();

    boolean isProfileAvailable();
}
