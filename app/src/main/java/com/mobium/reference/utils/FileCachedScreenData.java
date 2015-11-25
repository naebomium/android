package com.mobium.reference.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import com.mobium.client.models.ShopCategory;
import com.mobium.client.models.ShopItem;
import com.mobium.reference.utils.persistence.TimePersistenceCache;
import com.mobium.new_api.models.Banner;

import java.io.Serializable;

/**
 *  on 07.07.15.
 * http://mobiumapps.com/
 */
public class FileCachedScreenData extends TimePersistenceCache<Serializable[]> {
    private static final String BANNERS_CACHE_TAG = "BANNERS";
    private static final String POPULAR_CATEGORY_CACHE_TAG = "POPULAR_CATEGORIES";
    private static final String DISCOUNT_CACHE_TAG = "DISCOUNT";

    private String regionId;

    public FileCachedScreenData(Context context, String currentRegionId) {
        super(context, 10 * 60);
        tryLoad();
        if (regionId == null || !regionId.equals(currentRegionId)) {
            clearCache();
            regionId = currentRegionId;
        }
    }

    public boolean needLoad() {
        return !(has(POPULAR_CATEGORY_CACHE_TAG) &&
                has(DISCOUNT_CACHE_TAG));
    }

    public
    @Nullable
    ShopCategory[] getPopularCategoryis() {
        return (ShopCategory[]) load(POPULAR_CATEGORY_CACHE_TAG);
    }

    public
    @Nullable
    ShopItem[] getDiscountItems() {
        return (ShopItem[]) load(DISCOUNT_CACHE_TAG);
    }

    public
    @Nullable
    Banner[] getBannerItems() {
        return (Banner[]) load(BANNERS_CACHE_TAG);
    }

    public void setBannerItems(Banner... banners) {
        save(BANNERS_CACHE_TAG, banners);
    }

    public void setDiscountItems(ShopItem... items) {
        save(DISCOUNT_CACHE_TAG, items);
    }

    public void setPopularCategoryes(ShopCategory... shopCategories) {
        save(POPULAR_CATEGORY_CACHE_TAG, shopCategories);
    }

}
