package com.mobium.new_api.models;

import com.mobium.new_api.cache.BaseCachedItem;
import org.parceler.Parcel;

/**
 *  on 18.06.15.
 * http://mobiumapps.com/
 */

public class BannerList extends BaseCachedItem {
    public Banner[] banners;

    public BannerList(Banner[] banners) {
        this.banners = banners;
    }

    public BannerList() {
    }

    public Banner[] getBanners() {
        return banners;
    }

    public void setBanners(Banner[] banners) {
        this.banners = banners;
    }

    @Override
    public int getTimeOutInMinutes() {
        return 10;
    }
}
