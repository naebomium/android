package com.mobium.config.prototype_models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.mobium.config.prototype.IStaticData;

/**
 *  on 16.11.15.
 */
public class StaticDataModel implements IStaticData{
    private @SerializedName("app_name") String appName;
    private @SerializedName("package_name") String packageName;
    private @SerializedName("mobium_build_id") String mobiumBuildId;
    private @SerializedName("mobium_api_url") String mobiumApiUrl;
    private @SerializedName("mobium_api_protocol") String mobiumApiProtocol;
    private @SerializedName("mobium_api_key") String mobiumApiKey;


    @NonNull
    @Override
    public String appName() {
        return appName;
    }

    @NonNull
    @Override
    public String packageName() {
        return packageName;
    }


    @NonNull
    @Override
    public String mobiumBuildId() {
        return mobiumBuildId;
    }

    @NonNull
    @Override
    public String mobiumApiUrl() {
        return mobiumApiUrl;
    }

    @NonNull
    @Override
    public String mobiumApiProtocol() {
        return mobiumApiProtocol;
    }

    @NonNull
    @Override
    public String mobiumApiKey() {
        return mobiumApiKey;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setMobiumBuildId(String mobiumBuildId) {
        this.mobiumBuildId = mobiumBuildId;
    }

    public void setMobiumApiUrl(String mobiumApiUrl) {
        this.mobiumApiUrl = mobiumApiUrl;
    }

    public void setMobiumApiProtocol(String mobiumApiProtocol) {
        this.mobiumApiProtocol = mobiumApiProtocol;
    }

    public void setMobiumApiKey(String mobiumApiKey) {
        this.mobiumApiKey = mobiumApiKey;
    }
}
