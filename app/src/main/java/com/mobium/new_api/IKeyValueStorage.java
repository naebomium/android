package com.mobium.new_api;


import com.annimon.stream.Optional;

import java.util.Map;

/**
 *  on 14.10.2015.
 */
public interface IKeyValueStorage {

    String get (KeyType key, String defaultValue);
    Optional<String> get(KeyType key);

    void put(KeyType key, String value);

    Optional<Map<String, String>> getMap(KeyType key);
    void put(KeyType key, Map<String, String> value);
}
