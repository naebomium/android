package com.mobium.reference.modules;

import com.google.gson.Gson;
import com.mobium.client.api.ShopApiInterface;
import com.mobium.client.api.networking.ExtraApiInterface;
import com.mobium.google_places_api.PlaceHttpApi;
import com.mobium.new_api.Api;
import com.mobium.reference.ReferenceApplication;
import com.mobium.userProfile.ProfileApi;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

/**
 *  on 19.10.15.
 */

@Module(
        injects = {
                ShopApiInterface.class,
                Api.class,
                ProfileApi.class,
                ExtraApiInterface.class,
                ReferenceApplication.class,
                PlaceHttpApi.class
        }
)
public class NetworkModule
{
    @Provides
    @Singleton
    OkHttpClient provideHttpClient()
    {
        return new OkHttpClient();
    }

    @Provides
    @Singleton
    Gson getGson() {
        return new Gson();
    }

}
