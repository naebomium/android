package com.mobium.new_api.cache;

import android.support.v4.util.LruCache;

import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirDeleteCallback;
import com.mobium.new_api.models.BannerList;
import com.mobium.new_api.models.RegionList;
import com.mobium.new_api.models.catalog.DiscountItems;
import com.mobium.new_api.models.catalog.PopularCategory;
import com.mobium.new_api.models.catalog_.ShopItem_;
import com.mobium.new_api.models.catalog_.ShopItems_;

import java.util.Calendar;

/**
 *  on 16.07.15.
 * http://mobiumapps.com/
 */
public class RamCache implements ICacheManager {
    private final LruCache ramCache = new LruCache(300);


    private int getCurrentMinute() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MINUTE);
    }


    @Override
    public <T extends ICachedItem> T getCachedItem(String id, Class<T> type) throws Exception {
        T item = (T) ramCache.get(id);
        //if (item == null)
        //    item = Reservoir.get(id, type);
        if (item == null)
            throw new NullPointerException();

        if (item.getTimeOutInMinutes() < getCurrentMinute() - item.getMinuteOfCaching()) {
            Reservoir.deleteAsync(id, new ReservoirDeleteCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(Exception e) {

                }
            });
            ramCache.remove(id);
            throw new NullPointerException();
        }

        return item;
    }

    @Override
    public void saveShopItem(ShopItem_ item) throws Exception {
        saveCachedItem(item.id, item);
    }

    @Override
    public void saveCategoryItems(String id, ShopItems_ items_) throws Exception {
        saveCachedItem(id, items_);
    }

    @Override
    public void saveBannerList(BannerList list) throws Exception {
        saveCachedItem(BannerList.class.getSimpleName(), list);
    }

    @Override
    public void saveRegionsWithShops(RegionList regions) throws Exception {
        saveCachedItem(RegionList.class.getSimpleName() + "", regions);
    }

    @Override
    public void savePopularCategory(PopularCategory categories) throws Exception {
        saveCachedItem(PopularCategory.class.getSimpleName(), categories);

    }

    @Override
    public void saveDiscountItems(DiscountItems items) throws Exception {
        saveCachedItem(DiscountItems.class.getSimpleName(), items);
    }

    @Override
    public ShopItem_ getItem(String id) throws Exception {
        return getCachedItem(id, ShopItem_.class);
    }

    @Override
    public ShopItems_ getCategoryItems(String id) throws Exception {
        return getCachedItem(id, ShopItems_.class);
    }

    @Override
    public BannerList getBannerList() throws Exception {
        return getCachedItem(BannerList.class.getSimpleName(), BannerList.class);
    }

    @Override
    public RegionList getRegionsWithShop() throws Exception {
        return getCachedItem("regionsWithShop", RegionList.class);
    }

    @Override
    public PopularCategory getPopularCategory() throws Exception {
        return getCachedItem(PopularCategory.class.getSimpleName(), PopularCategory.class);
    }

    @Override
    public DiscountItems getDiscountItems() throws Exception {
        return getCachedItem(DiscountItems.class.getSimpleName(), DiscountItems.class);
    }

    @Override
    public void saveCachedItem(String id, ICachedItem item) throws Exception {
        item.putMinuteOfCaching(getCurrentMinute());
        ramCache.put(id, item);
      //  Reservoir.put(id, item);
    }


}
