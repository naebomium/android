package com.mobium.reference.utils;

import java.util.ArrayList;
import java.util.Collection;

/**
 *  on 26.10.15.
 */
public class CollectionUtil {
    public static <T> ArrayList<T> toArrayList(Collection<T> c) {
        return c instanceof ArrayList ? (ArrayList<T>) c : new ArrayList<>(c);
    }
}
