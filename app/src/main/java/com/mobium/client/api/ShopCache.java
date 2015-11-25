package com.mobium.client.api;

import com.mobium.client.ShopDataStorage;
import com.mobium.client.models.ShopCategory;
import com.mobium.client.models.ShopItem;
import com.mobium.new_api.Api;
import com.mobium.new_api.models.ShopPoint;
import com.mobium.new_api.models.catalog.DiscountItems;
import com.mobium.new_api.models.catalog.PopularCategory;
import com.mobium.reference.utils.executing.ExecutingException;
import com.mobium.reference.utils.persistence.TimedCache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 *
 *
 * Date: 01.12.12
 * Time: 2:09
 */
public class ShopCache {
    private TimedCache<ShopCategory> categoryTimedCache;
    private TimedCache<ShopItem[]> categoryItemsTimedCache;
    private TimedCache<ShopItem> itemTimedCache;

    private ShopApiExecutor executor;

    public ShopCache(ShopApiExecutor executor) {
        categoryTimedCache = new TimedCache<>(10 * 60 /* 10 min */);
        categoryItemsTimedCache = new TimedCache<>(10 * 60 /* 10 min */);
        itemTimedCache = new TimedCache<>(10 * 60 /* 10 min */);
        this.executor = executor;
    }

    public ShopItem fetchItem(String id) {
        return itemTimedCache.fetchFromCache(id);
    }

    public ShopItem fetchItemFromServer(String itemId) throws ExecutingException {
        ShopItem res = executor.loadItem(itemId, ShopDataStorage.getRegionId());
        itemTimedCache.putToCache(itemId, res);
        return res;
    }

    public ShopItem[] getItemsByRealId(List<String> id) throws ExecutingException {
        ShopItem[] res = executor.getItemsByRealId(id, ShopDataStorage.getRegionId());
        for (ShopItem item : res)
            itemTimedCache.putToCache(item.id, item);
        return res;
    }

    public ShopItem[] getItemsById(List<String> id) throws ExecutingException {
        ShopItem[] res = executor.getItemsById(id);
        for (ShopItem item : res)
            itemTimedCache.putToCache(item.id, item);
        return res;
    }


    public ShopItem getItemByRealId(String realId) throws ExecutingException {
        ShopItem res = executor.getItemByRealId(realId, ShopDataStorage.getRegionId());
        itemTimedCache.putToCache(res.getId(), res);
        return res;
    }

    public ShopItem[] fetchItemsFromServer(ShopCategory category) throws ExecutingException {
        ShopItem[] res = fetchCategoryItems(category.id);
        if (res == null || res.length == 0) {
            res = executor.loadItems(category, ShopDataStorage.getRegionId());
            categoryItemsTimedCache.putToCache(category.id, res);
        }
        return res;
    }

    public ShopItem[] fetchItemsFromServer(ShopCategory shopCategory, int offset, int limit) throws ExecutingException {
        List<ShopItem> resultItems = new ArrayList<>();
        ArrayList<ShopItem> allItems = new ArrayList<>();

        ShopItem[] oldItems = categoryItemsTimedCache.fetchFromCache(shopCategory.id);

        ShopItem[] newItems = executor.loadItems(shopCategory, offset, limit, ShopDataStorage.getRegionId());

        if (oldItems != null) {
            Collections.addAll(allItems, oldItems);
        }
        Collections.addAll(allItems, newItems);
        categoryItemsTimedCache.putToCache(shopCategory.id, allItems.toArray(new ShopItem[allItems.size()]));

        Collections.addAll(resultItems, newItems);
        return resultItems.toArray(new ShopItem[resultItems.size()]);
    }

    public ShopItem[] fetchCategoryItems(String id) {
        return categoryItemsTimedCache.fetchFromCache(id);
    }

    public int countOfCahcedShopItems() {
        return categoryItemsTimedCache.countOfAllItems();
    }

    public void clearCache() {
        categoryTimedCache.clear();
        categoryItemsTimedCache.clear();
        itemTimedCache.clear();
    }
    public Set<ShopItem> getShopPoints() {
        return Collections.unmodifiableSet(itemTimedCache.getAll());
    }

    public boolean hasItem(String id) {
        return itemTimedCache.has(id);
    }


    public ShopItem[] loadDiscounts() throws ExecutingException {
        try {
            return Api.cacheManager.getDiscountItems().items;
        } catch (Exception e) {
            ShopItem[] discount = executor.loadDiscounts();
            try {
                Api.cacheManager.saveDiscountItems(new DiscountItems(discount));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return discount;
        }
    }

    public ShopCategory[] loadPopularCategories() throws ExecutingException {
        try {
            return Api.cacheManager.getPopularCategory().categories;
        } catch (Exception e) {
            ShopCategory[] categories = executor.loadPopularCategories(ShopDataStorage.getRegionId());
            try {
                Api.cacheManager.savePopularCategory(new PopularCategory(categories));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return categories;
        }
    }
}