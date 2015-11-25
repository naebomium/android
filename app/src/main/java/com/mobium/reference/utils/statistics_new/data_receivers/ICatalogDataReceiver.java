package com.mobium.reference.utils.statistics_new.data_receivers;

import com.mobium.client.models.ShopCategory;
import com.mobium.client.models.ShopItem;

/**
 *  on 29.09.15.
 */
public interface ICatalogDataReceiver {
    void onProductOpened(ShopItem item);
    void onCategoryOpened(ShopCategory category);
    void onFilterOpened(ShopCategory category);
    void onOpenBarcodeScanner();
}
