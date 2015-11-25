package com.mobium.new_api.models.catalog;

import com.mobium.client.models.ShopCategory;
import com.mobium.new_api.cache.BaseCachedItem;

/**
 *  on 09.08.15.
 */
public class PopularCategory extends BaseCachedItem {
    public final ShopCategory[] categories;

    public PopularCategory(ShopCategory[] categories) {
        this.categories = categories;
    }

    @Override
    public int getTimeOutInMinutes() {
        return 60 * 12;
    }
}
