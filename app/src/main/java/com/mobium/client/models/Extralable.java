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

package com.mobium.client.models;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Date: 19.09.12
 * Time: 16:12
 */
public abstract class Extralable<T> implements Serializable {
    private HashMap<String, Serializable> params = new HashMap<>();

    public T putExtra(String key, int value) {
        params.put(key, value);
        return (T) this;
    }

    public T putExtra(String key, boolean value) {
        params.put(key, value);
        return (T) this;
    }

    public T putExtra(String key, String value) {
        params.put(key, value);
        return (T) this;
    }

    public T putExtra(String key, Serializable value) {
        params.put(key, value);
        return (T) this;
    }

    public Serializable getExtra(String key) {
        return getExtra(key, null);
    }

    public Serializable getExtra(String key, Serializable defaultValue) {
        if (params.containsKey(key)) {
            Serializable val = params.get(key);
            if (val != null)
                return val;
        }
        return defaultValue;
    }

    public String getExtraString(String key) {
        return getExtraString(key, null);
    }

    public String getExtraString(String key, String defaultValue) {
        if (params.containsKey(key)) {
            Object val = params.get(key);
            if ((val != null) && (val instanceof String))
                return (String) val;
        }
        return defaultValue;
    }

    public Integer getExtraInteger(String key) {
        return getExtraInteger(key, null);
    }

    public Integer getExtraInteger(String key, Integer defaultValue) {
        if (params.containsKey(key)) {
            Object val = params.get(key);
            if ((val != null) && (val instanceof Integer))
                return (Integer) val;
        }
        return defaultValue;
    }

    public Boolean getExtraBoolean(String key) {
        return getExtraBoolean(key, false);
    }

    public Boolean getExtraBoolean(String key, Boolean defaultValue) {
        if (params.containsKey(key)) {
            Object val = params.get(key);
            if ((val != null) && (val instanceof Boolean))
                return (Boolean) val;
        }
        return defaultValue;
    }


    public boolean hasExtra(String key) {
        return params.containsKey(key);
    }

    public HashMap<String, Serializable> getExtraParams() {
        return params;
    }

    public void setExtraParams(HashMap<String, Serializable> params) {
        this.params = params;
    }
}
