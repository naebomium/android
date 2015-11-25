package com.mobium.reference.utils.statistics_new.data_receivers;

import com.mobium.client.models.ShopItem;

/**
 *  on 29.09.15.
 */
public interface IFavouritesDataReceiver {
    void onFavoritesAdd(ShopItem item);
    void onFavoritesRemove(ShopItem item);
    void onOpenFromFavourites(ShopItem item);
}
