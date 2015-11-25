package com.mobium.reference.utils;

import com.mobium.client.models.ShopCategory;
import com.mobium.client.models.ShopItem;
import com.mobium.new_api.models.Banner;

/**
 *  on 23.06.15.
 * http://mobiumapps.com/
 */
public class Receivers {
    public interface BannerReceiver {
        void onBannerLoad(Banner[] banners);
    }

    public interface PopularCategoryReceiver  {
        void onCategoryLoad(ShopCategory[] categories);
    }

    public interface SalesReceiver {
        void onSalesLoad(ShopItem[] items);
    }

}
