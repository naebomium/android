package com.mobium.config.prototype_models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobium.config.prototype.IApplicationData;

/**
 *  on 16.11.15.
 */
public class ApplicationDataModel implements IApplicationData {
    @SerializedName("profile_enabled")
    @Expose
    private boolean profileEnabled;
    @SerializedName("profile_base_url")
    @Expose
    private String profileBaseUrl;
    @SerializedName("mobium_profile_api")
    @Expose
    private MobiumProfileApi mobiumProfileApi;
    @SerializedName("application_shop_enabled")
    @Expose
    private boolean applicationShopEnabled;
    @SerializedName("service_google_analytics_id")
    @Expose
    private String serviceGoogleAnalyticsId;
    @SerializedName("service_flurry_id")
    @Expose
    private String serviceFlurryId;
    @SerializedName("service_mat_conversation")
    @Expose
    private String serviceMatConversation;
    @SerializedName("service_mat_advertiser")
    @Expose
    private String serviceMatAdvertiser;
    @SerializedName("social_vk")
    @Expose
    private String socialVk;
    @SerializedName("social_facebook")
    @Expose
    private String socialFacebook;
    @SerializedName("social_youtube")
    @Expose
    private String socialYoutube;
    @SerializedName("social_twitter")
    @Expose
    private String socialTwitter;
    @SerializedName("api_key_google_youtube_api")
    @Expose
    private String apiKeyGoogleYoutubeApi;
    @SerializedName("api_key_google_place_api")
    @Expose
    private String apiKeyGooglePlaceApi;
    @SerializedName("api_key_google_maps_api")
    @Expose
    private String apiKeyGoogleMapsApi;
    @SerializedName("shop_phone")
    @Expose
    private String shopPhone;
    @SerializedName("shop_currency_placeholder")
    private String shopCurrencyPlaceholder;

    @SerializedName("service_hockeyapp_key")
    private String serviceHockeyappKey;

    @SerializedName("application_show_prices")
    private boolean showPrices;

    @SerializedName("application_sender_id")
    private String[] applicationSenderId;


    @Override
    public boolean getShowPrices() {
        return showPrices;
    }

    /**
     *
     * @return
     * The profileEnabled
     */
    public boolean getProfileEnabled() {
        return profileEnabled;
    }

    /**
     *
     * @param profileEnabled
     * The profile_enabled
     */
    public void setProfileEnabled(boolean profileEnabled) {
        this.profileEnabled = profileEnabled;
    }

    /**
     *
     * @return
     * The profileBaseUrl
     */
    public String getProfileBaseUrl() {
        return profileBaseUrl;
    }

    /**
     *
     * @param profileBaseUrl
     * The profile_base_url
     */
    public void setProfileBaseUrl(String profileBaseUrl) {
        this.profileBaseUrl = profileBaseUrl;
    }

    /**
     *
     * @return
     * The mobiumProfileApi
     */
    public MobiumProfileApi getMobiumProfileApi() {
        return mobiumProfileApi;
    }

    /**
     *
     * @param mobiumProfileApi
     * The mobium_profile_api
     */
    public void setMobiumProfileApi(MobiumProfileApi mobiumProfileApi) {
        this.mobiumProfileApi = mobiumProfileApi;
    }

    /**
     *
     * @return
     * The applicationShopEnabled
     */
    public boolean getApplicationShopEnabled() {
        return applicationShopEnabled;
    }

    /**
     *
     * @param applicationShopEnabled
     * The application_shop_enabled
     */
    public void setApplicationShopEnabled(boolean applicationShopEnabled) {
        this.applicationShopEnabled = applicationShopEnabled;
    }

    /**
     *
     * @return
     * The serviceGoogleAnalyticsId
     */
    public String getServiceGoogleAnalyticsId() {
        return serviceGoogleAnalyticsId;
    }

    /**
     *
     * @param serviceGoogleAnalyticsId
     * The service_google_analytics_id
     */
    public void setServiceGoogleAnalyticsId(String serviceGoogleAnalyticsId) {
        this.serviceGoogleAnalyticsId = serviceGoogleAnalyticsId;
    }

    /**
     *
     * @return
     * The serviceFlurryId
     */
    public String getServiceFlurryId() {
        return serviceFlurryId;
    }

    /**
     *
     * @param serviceFlurryId
     * The service_flurry_id
     */
    public void setServiceFlurryId(String serviceFlurryId) {
        this.serviceFlurryId = serviceFlurryId;
    }

    /**
     *
     * @return
     * The serviceMatConversation
     */
    public String getServiceMatConversation() {
        return serviceMatConversation;
    }

    /**
     *
     * @param serviceMatConversation
     * The service_mat_conversation
     */
    public void setServiceMatConversation(String serviceMatConversation) {
        this.serviceMatConversation = serviceMatConversation;
    }

    /**
     *
     * @return
     * The serviceMatAdvertiser
     */
    public String getServiceMatAdvertiser() {
        return serviceMatAdvertiser;
    }

    @Nullable
    @Override
    public String getServiceHockeyappKey() {
        return serviceHockeyappKey;
    }

    /**
     *
     * @param serviceMatAdvertiser
     * The service_mat_advertiser
     */
    public void setServiceMatAdvertiser(String serviceMatAdvertiser) {
        this.serviceMatAdvertiser = serviceMatAdvertiser;
    }

    /**
     *
     * @return
     * The socialVk
     */
    public String getSocialVk() {
        return socialVk;
    }

    /**
     *
     * @param socialVk
     * The social_vk
     */
    public void setSocialVk(String socialVk) {
        this.socialVk = socialVk;
    }

    /**
     *
     * @return
     * The socialFacebook
     */
    public String getSocialFacebook() {
        return socialFacebook;
    }

    /**
     *
     * @param socialFacebook
     * The social_facebook
     */
    public void setSocialFacebook(String socialFacebook) {
        this.socialFacebook = socialFacebook;
    }

    /**
     *
     * @return
     * The socialYoutube
     */
    public String getSocialYoutube() {
        return socialYoutube;
    }

    /**
     *
     * @param socialYoutube
     * The social_youtube
     */
    public void setSocialYoutube(String socialYoutube) {
        this.socialYoutube = socialYoutube;
    }

    /**
     *
     * @return
     * The socialTwitter
     */
    public String getSocialTwitter() {
        return socialTwitter;
    }

    /**
     *
     * @param socialTwitter
     * The social_twitter
     */
    public void setSocialTwitter(String socialTwitter) {
        this.socialTwitter = socialTwitter;
    }

    /**
     *
     * @return
     * The apiKeyGoogleYoutubeApi
     */
    @NonNull
    public String getApiKeyGoogleYoutubeApi() {
        return apiKeyGoogleYoutubeApi;
    }

    /**
     *
     * @param apiKeyGoogleYoutubeApi
     * The api_key_google_youtube_api
     */
    public void setApiKeyGoogleYoutubeApi(String apiKeyGoogleYoutubeApi) {
        this.apiKeyGoogleYoutubeApi = apiKeyGoogleYoutubeApi;
    }

    /**
     *
     * @return
     * The apiKeyGooglePlaceApi
     */
    @NonNull
    public String getApiKeyGooglePlaceApi() {
        return apiKeyGooglePlaceApi;
    }

    /**
     *
     * @param apiKeyGooglePlaceApi
     * The api_key_google_place_api
     */
    public void setApiKeyGooglePlaceApi(String apiKeyGooglePlaceApi) {
        this.apiKeyGooglePlaceApi = apiKeyGooglePlaceApi;
    }

    /**
     *
     * @return
     * The apiKeyGoogleMapsApi
     */
    @NonNull
    public String getApiKeyGoogleMapsApi() {
        return apiKeyGoogleMapsApi;
    }

    /**
     *
     * @param apiKeyGoogleMapsApi
     * The api_key_google_maps_api
     */
    public void setApiKeyGoogleMapsApi(String apiKeyGoogleMapsApi) {
        this.apiKeyGoogleMapsApi = apiKeyGoogleMapsApi;
    }

    /**
     *
     * @return
     * The shopPhone
     */
    @NonNull
    public String getShopPhone() {
        return shopPhone;
    }

    @NonNull
    @Override
    public String getCurrencyPlaceholder() {
        return shopCurrencyPlaceholder;
    }

    @NonNull
    @Override
    public String[] getSenderId() {
        return applicationSenderId;
    }

    /**
     *
     * @param shopPhone
     * The shop_phone
     */
    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }

    public void setShopCurrencyPlaceholder(String shopCurrencyPlaceholder) {
        this.shopCurrencyPlaceholder = shopCurrencyPlaceholder;
    }

    public boolean isShowPrices() {
        return showPrices;
    }

    public void setShowPrices(boolean showPrices) {
        this.showPrices = showPrices;
    }

    public void setServiceHockeyappKey(String serviceHockeyappKey) {
        this.serviceHockeyappKey = serviceHockeyappKey;
    }

    public String[] getApplicationSenderId() {
        return applicationSenderId;
    }

    public void setApplicationSenderId(String[] applicationSenderId) {
        this.applicationSenderId = applicationSenderId;
    }
}
