package com.mobium.client.models;

import java.io.Serializable;

/**
 *
 *
 * Date: 17.12.12
 * Time: 4:43
 */
public class SearchResult implements Serializable {
    private ShopItem[] items;
    private ShopCategory[] categories;

    public SearchResult(ShopItem[] items, ShopCategory... categories) {
        this.items = items;
        this.categories = categories;
    }

    public ShopItem[] getItems() {
        return items;
    }

    public ShopCategory[] getCategories() {
        return categories;
    }

    public boolean isEmpty() {
        return items.length + categories.length == 0;
    }
}
