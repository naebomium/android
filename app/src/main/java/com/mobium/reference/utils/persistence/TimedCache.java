/*
 * Copyright (c) 2013 Extradea LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mobium.reference.utils.persistence;

import com.annimon.stream.Collectors;
import com.annimon.stream.Objects;
import com.annimon.stream.Stream;

import java.io.Serializable;
import java.util.*;

/**
 *
 *
 * Date: 01.12.12
 * Time: 1:59
 */
public class TimedCache<T> implements Serializable {
    private final int itemTimeout;
    private HashMap<String, CacheItem<T>> cache;

    public TimedCache(int itemTimeout) {
        this.itemTimeout = itemTimeout;
        cache = new HashMap<>();
    }

    public synchronized void putToCache(String key, T obj) {
        if (cache.containsKey(key)) {
            cache.remove(key);
        }
        cache.put(key, new CacheItem<>(obj));
    }

    public synchronized T fetchFromCache(String key) {
        if (cache.containsKey(key)) {
            CacheItem<T> item = cache.get(key);
            if (item.isTimedOut(itemTimeout)) {
                cache.remove(key);
                return null;
            } else {
                return item.item;
            }
        } else {
            return null;
        }
    }

    public synchronized boolean has(String key) {
        return fetchFromCache(key) != null;
    }

    public void clear() {
        cache.clear();
    }

    public void removeFromCache(String key) {
        cache.remove(key);
    }

    public int countOfAllItems() {
        int countOfAllItems = 0;
        for (String key : cache.keySet()) {
            if (cache.get(key).item instanceof Object[]) {
                countOfAllItems += ((Object[]) cache.get(key).item).length;
            }
        }
        return countOfAllItems;
    }

    public Set<T> getAll() {
        List<CacheItem<T>> items = new ArrayList<>(cache.values());

        return  Stream.of(cache.values())
                .map(CacheItem::getItem)
                .filter(t -> !Objects.equals(t, null))
                .collect(Collectors.toSet());
    }
}