package com.mobium.google_places_api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.mobium.google_places_api.models.AutoCompleteResult;
import com.mobium.google_places_api.models.AutoCompleteType;
import com.mobium.google_places_api.models.NearPlaceType;
import com.mobium.google_places_api.models.NearPlaces;
import com.mobium.reference.ReferenceApplication;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 *  on 27.08.15.
 */
public class PlaceHttpApi {
    private static final String url = "https://maps.googleapis.com";
    private static final String autocomplete = "/maps/api/place/autocomplete/json";
    private static final String place = "/maps/api/place/nearbysearch/json";

    private final ApiInterface apiInterface;
    private final String placeApiKey;

    @Inject
    protected @NonNull OkHttpClient client;

    @Inject
    protected @NonNull Gson gson;

    public PlaceHttpApi(@NonNull String placeApiKey,
                        boolean logDebugInfo
    ) {
        this.placeApiKey = placeApiKey;

        ReferenceApplication.getInstance().inject(this);
        apiInterface =
                new RestAdapter.Builder()
                        .setEndpoint(url)
                        .setConverter(new GsonConverter(gson))
                        .setClient(new OkClient(client))
                        .setLogLevel(logDebugInfo ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                        .build()
                        .create(ApiInterface.class);
    }


    public void getNearPlace(double latitude, double longitude, int radiusInMeters, NearPlaceType[] types, Callback<NearPlaces> callback) {
        if (radiusInMeters > 50000)
            throw new IllegalArgumentException("radiusInMeters should be less then 50'000m ");

        apiInterface.getNearPlaces(
                placeApiKey,
                latitude + "," + longitude,
                radiusInMeters,
                Stream.of(types).map(type -> type.name() + "|").collect(Collectors.joining()),
                callback
        );
    }


    public AutoCompleteResult getAutoComplete(@NonNull String input,
                                              @Nullable AutoCompleteType types[],
                                              @Nullable Double latitude,
                                              @Nullable Double longitude,
                                              @Nullable Integer radius,
                                              @Nullable Integer offset,
                                              @Nullable String components,
                                              @Nullable String language
    ) throws RetrofitError {
        return apiInterface.getAutoCompletePlaces(
                placeApiKey,
                input,
                formatLocation(latitude, longitude),
                language,
                formatTypes(types),
                radius,
                offset,
                components
        );
    }


    public interface ApiInterface {
        @GET(place)
        void getNearPlaces(@Query("key") String key,
                           @Query("location") String location,
                           @Query("radius") Integer radius,
                           @Query("types") String types,
                           Callback<NearPlaces> callback);


        @GET(autocomplete)
        AutoCompleteResult getAutoCompletePlaces(@Query("key") @NonNull String key,
                                                 @Query("input") @NonNull String input,
                                                 @Query("location") @Nullable String location,
                                                 @Query("language") @Nullable String language,
                                                 @Query("types") @Nullable String types,
                                                 @Query("radius") @Nullable Integer radius,
                                                 @Query("offset") @Nullable Integer offset,
                                                 @Query("components") @Nullable String components
        );

    }

    @Nullable
    String formatLocation(@Nullable Double latitude,
                          @Nullable Double longitude) {
        if (latitude != null && longitude != null)
            return latitude + "," + longitude;
        return null;
    }

    @Nullable
    String formatTypes(@Nullable AutoCompleteType[] types) {
        if (types != null && types.length > 0)
            return Stream.of(types).map(type -> type.serializedName + "|").collect(Collectors.joining());
        return null;
    }


    private static volatile PlaceHttpApi instance;

    public static PlaceHttpApi getInstance(@NonNull String placeApiKey,
                                           boolean logDebugInfo) {
        PlaceHttpApi localInstance = instance;
        if (localInstance == null) {
            synchronized (PlaceHttpApi.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new PlaceHttpApi(placeApiKey, logDebugInfo);
                }
            }
        }
        return localInstance;
    }

}
