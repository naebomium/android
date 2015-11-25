package com.mobium.new_api.cache;

import com.mobium.new_api.models.BannerList;
import com.mobium.new_api.models.RegionList;
import com.mobium.new_api.models.catalog.DiscountItems;
import com.mobium.new_api.models.catalog.PopularCategory;
import com.mobium.new_api.models.catalog_.ShopItem_;
import com.mobium.new_api.models.catalog_.ShopItems_;

/**
 *  on 16.07.15.
 * http://mobiumapps.com/
 */
public interface ICacheManager {
    void saveShopItem(ShopItem_ item) throws Exception;
    void saveCategoryItems(String id, ShopItems_ items_) throws Exception;

    void saveBannerList(BannerList list) throws Exception;
    void saveRegionsWithShops(RegionList regions) throws Exception;

    void savePopularCategory(PopularCategory categories) throws Exception;
    void saveDiscountItems(DiscountItems items) throws Exception;




    ShopItem_ getItem(String id) throws Exception;
    ShopItems_ getCategoryItems(String id) throws Exception;
    BannerList getBannerList() throws Exception;
    RegionList getRegionsWithShop() throws Exception;
    PopularCategory getPopularCategory() throws Exception;
    DiscountItems getDiscountItems() throws Exception;


    void saveCachedItem(String id, ICachedItem item) throws Exception;
    <T extends ICachedItem> T getCachedItem(String id, Class<T> type) throws Exception;
}
