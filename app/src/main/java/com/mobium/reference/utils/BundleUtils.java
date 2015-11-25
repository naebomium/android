package com.mobium.reference.utils;

import android.os.Bundle;

import java.io.Serializable;

/**
 *  on 24.07.15.
 * http://mobiumapps.com/
 */
public class BundleUtils {
    public static <T extends Serializable> T  fromBundle(Bundle bundle, Class<T> tClass) {
        return tClass.cast(bundle.getSerializable(tClass.getSimpleName()));
    }

    public static <T extends Serializable> Bundle toBundle(Bundle bundle, T object) {
        bundle.putSerializable(object.getClass().getSimpleName(), object);
        return bundle;
    }

    public static <T extends Serializable> Bundle toBundle(Bundle bundle, T object, Class name) {
        bundle.putSerializable(name.getSimpleName(), object);
        return bundle;
    }
}
