package com.mobium.reference.utils.storage;

import android.content.Context;
import android.util.Log;

import com.annimon.stream.Optional;
import com.mobium.new_api.KeyType;
import com.mobium.new_api.KeyValueStorage;
import com.mobium.reference.utils.Functional;

import java.util.HashSet;
import java.util.Set;

/**
 *  on 15.10.15.
 */
public class AppIdProvider {
    private static final Set<Functional.ChangeListener<String>> list = new HashSet<>();
    private static String cache;

    public static Optional<String> getAppId(Context context) {
        if (cache != null)
            return Optional.of(cache);
        Optional<String> result = KeyValueStorage.getInstance(context).get(KeyType.appId);
        result.ifPresent(id -> {
            Log.v(AppIdProvider.class.getSimpleName(), "AppId load from cache " + id);
            cache = id;
        });
        return result;
    }

    public static void putAppId(Context context, String appId) {
        KeyValueStorage.getInstance(context).put(KeyType.appId, appId);
        cache = appId;
        for (Functional.ChangeListener<String> l : list)
            l.onChange(appId);
    }

    public static void addListener(Functional.ChangeListener<String> appIdListener) {
        list.add(appIdListener);
    }
    public static void removeListener(Functional.ChangeListener<String> appIdListener) {
        list.remove(appIdListener);
    }
}
