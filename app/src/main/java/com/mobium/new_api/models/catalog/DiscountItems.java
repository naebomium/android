package com.mobium.new_api.models.catalog;

import com.mobium.client.models.ShopItem;
import com.mobium.new_api.cache.BaseCachedItem;

/**
 *  on 09.08.15.
 */
public class DiscountItems extends BaseCachedItem {
    public final ShopItem[] items;

    public DiscountItems(ShopItem[] items) {
        this.items = items;
    }

    @Override
    public int getTimeOutInMinutes() {
        return 12 * 60;
    }
}
