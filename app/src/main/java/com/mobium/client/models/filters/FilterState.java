package com.mobium.client.models.filters;

import android.support.annotation.NonNull;
import com.mobium.client.models.ShopItem;
import com.mobium.reference.utils.Functional;

import java.util.HashSet;

/**
 *  IDEA.
 *
 * Date: 04.12.11
 * Time: 23:58
 */
public abstract class FilterState {
    public abstract Boolean filterOut(@NonNull ShopItem item);
    public abstract Boolean isCustomState();
    public abstract void clear();
    public HashSet<Functional.ChangeListener<FilterState>> listeners = new HashSet<>();


    public void addLister(Functional.ChangeListener<FilterState> listener) {
        listeners.add(listener);
    }

    public void removeListener(Functional.ChangeListener<FilterState> listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    public void notyfiListers() {
        for (Functional.ChangeListener<FilterState> listener : listeners)
            listener.onChange(this);
    }

}
