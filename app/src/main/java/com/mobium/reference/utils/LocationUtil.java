package com.mobium.reference.utils;

import android.location.Location;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;

/**
 *  on 9/16/15.
 * http://mobiumapps.com/
 */

public class LocationUtil {

    public static final OkHttpClient client = new OkHttpClient();
    private static final String geoURL = "https://geocode-maps.yandex.ru/1.x";

    public static String getFullURL(Location location, int result) {
        return geoURL + "/?geocode=" + location.getLongitude() + "," +
                location.getLatitude() + "&format=json" + "&result=" + result
                + "&kind=locality";
    }

    public static Call get(String url) throws IOException {
        Request request = new Request.Builder()
                .header("Host", "geocode-maps.yandex.ru")
                .url(url)
                .build();
        return client.newCall(request);
    }
}
