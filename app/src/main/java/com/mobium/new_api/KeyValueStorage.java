package com.mobium.new_api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.annimon.stream.Optional;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *  on 14.10.2015.
 */
public class KeyValueStorage implements IKeyValueStorage {

    private static volatile KeyValueStorage instance;

    private final SharedPreferences prefs;

    private static final String FIELD_TOKEN = "&";
    private static final String MAP_TOKEN = "=";

    public KeyValueStorage(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public static KeyValueStorage getInstance(Context context) {
        KeyValueStorage localInstance = instance;
        if (localInstance == null) {
            synchronized (KeyValueStorage.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new KeyValueStorage(context);
                }
            }
        }
        return localInstance;
    }


    @Override
    public String get(KeyType key, String defaultValue) {
        return prefs.getString(key.name(), defaultValue);
    }

    @Override
    public Optional<String> get(KeyType key) {
       return Optional.ofNullable(prefs.getString(key.name(), null));
    }


    @Override
    public void put(KeyType key, String value) {
        prefs.edit().putString(key.name(), value).apply();
    }

    @Override
    public Optional<Map<String, String>> getMap(KeyType key) {
        Optional<Map<String, String>> result = Optional.empty();

        Optional<String> resultFromPrefs = Optional.ofNullable(prefs.getString(key.name(), null));

        if (resultFromPrefs.isPresent()) {
            StringTokenizer tokenizer = new StringTokenizer(resultFromPrefs.get(), FIELD_TOKEN);
            result = Optional.of(new HashMap<>());

            while (tokenizer.hasMoreTokens()) {
                String fieldString = tokenizer.nextToken();
                StringTokenizer fieldToken = new StringTokenizer(fieldString, MAP_TOKEN);
                while (fieldToken.hasMoreTokens()) {
                    String fieldKey = fieldToken.nextToken();
                    String fieldValue = fieldToken.nextToken();
                    result.get().put(fieldKey, fieldValue);
                }
            }
        }
        return result;
    }

    @Override
    public void put(KeyType key, Map<String, String> value) {
        StringBuilder builder = new StringBuilder();
        for (String k : value.keySet()) {
            builder.append(k);
            builder.append(MAP_TOKEN);
            builder.append(value.get(k));
            builder.append(FIELD_TOKEN);
        }
        prefs.edit().putString(key.name(), builder.toString()).apply();
    }


}
