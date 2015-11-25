package com.mobium.new_api.models.catalog_;

import android.support.annotation.NonNull;

import com.mobium.new_api.cache.BaseCachedItem;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 *  on 15.07.15.
 * http://mobiumapps.com/
 */

public class ShopItems_ extends BaseCachedItem {
    public final @NonNull ArrayList<ShopItem_> items;
    public final @NonNull Integer totalCount;

    public ShopItems_(@NonNull ArrayList<ShopItem_> items, @NonNull Integer totalCount) {
        this.items = items;
        this.totalCount = totalCount;
    }

    @Override
    public int getTimeOutInMinutes() {
        return 60;
    }
}
