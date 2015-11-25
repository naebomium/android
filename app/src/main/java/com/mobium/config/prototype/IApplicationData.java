package com.mobium.config.prototype;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 *  on 14.11.15.
 */
public interface IApplicationData {
    boolean getShowPrices();
    boolean getProfileEnabled();
    boolean getApplicationShopEnabled();

    @Nullable String getProfileBaseUrl();
    @Nullable String getServiceGoogleAnalyticsId();
    @Nullable String getServiceFlurryId();
    @Nullable String getServiceMatConversation();
    @Nullable String getServiceMatAdvertiser();
    @Nullable String getServiceHockeyappKey();


    @Nullable String getSocialVk();
    @Nullable String getSocialFacebook();
    @Nullable String getSocialYoutube();
    @Nullable String getSocialTwitter();

    @NonNull String getApiKeyGoogleYoutubeApi();
    @NonNull String getApiKeyGooglePlaceApi();
    @NonNull String getApiKeyGoogleMapsApi();

    @NonNull String getShopPhone();

    @NonNull String getCurrencyPlaceholder();


    @NonNull String[] getSenderId();
}
