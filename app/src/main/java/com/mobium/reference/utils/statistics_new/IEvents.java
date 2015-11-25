package com.mobium.reference.utils.statistics_new;

import com.mobium.reference.utils.statistics_new.data_receivers.IAppDataReceiver;
import com.mobium.reference.utils.statistics_new.data_receivers.IBannersDataReceiver;
import com.mobium.reference.utils.statistics_new.data_receivers.ICartDataReceiver;
import com.mobium.reference.utils.statistics_new.data_receivers.ICatalogDataReceiver;
import com.mobium.reference.utils.statistics_new.data_receivers.IConnectivityDataReceiver;
import com.mobium.reference.utils.statistics_new.data_receivers.IFavouritesDataReceiver;
import com.mobium.reference.utils.statistics_new.data_receivers.INavigationDataReceiver;
import com.mobium.reference.utils.statistics_new.data_receivers.IOrderDataReceiver;
import com.mobium.reference.utils.statistics_new.data_receivers.IRegionsDataReceiver;
import com.mobium.reference.utils.statistics_new.data_receivers.ISearchDataReceiver;

/**
 *  on 29.09.15.
 */
public interface IEvents {
    IAppDataReceiver app();
    IBannersDataReceiver banners();
    ICartDataReceiver cart();
    ICatalogDataReceiver catalog();
    IConnectivityDataReceiver connectivity();
    IFavouritesDataReceiver favourites();
    INavigationDataReceiver navigation();
    IOrderDataReceiver order();
    IRegionsDataReceiver regions();
    ISearchDataReceiver search();
    ILifeCycleListener lifeCycle();
}
