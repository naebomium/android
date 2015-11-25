package com.mobium.reference.utils.statistics_new.data_receivers;
import com.mobium.reference.utils.statistics_new.ILifeCycleListener;

/**
 *  on 29.09.15.
 */
public interface IEventReceiver extends
        IAppDataReceiver,
        IBannersDataReceiver,
        ICartDataReceiver,
        ICatalogDataReceiver,
        IConnectivityDataReceiver,
        IFavouritesDataReceiver,
        INavigationDataReceiver,
        IOrderDataReceiver,
        IRegionsDataReceiver,
        ISearchDataReceiver,
        ILifeCycleListener {
}
