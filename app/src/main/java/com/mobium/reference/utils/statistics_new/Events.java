package com.mobium.reference.utils.statistics_new;

import android.app.Activity;

import com.mobium.reference.utils.statistics_new.data_receivers.IAppDataReceiver;
import com.mobium.reference.utils.statistics_new.data_receivers.IBannersDataReceiver;
import com.mobium.reference.utils.statistics_new.data_receivers.ICartDataReceiver;
import com.mobium.reference.utils.statistics_new.data_receivers.ICatalogDataReceiver;
import com.mobium.reference.utils.statistics_new.data_receivers.IConnectivityDataReceiver;
import com.mobium.reference.utils.statistics_new.data_receivers.IEventReceiver;
import com.mobium.reference.utils.statistics_new.data_receivers.IFavouritesDataReceiver;
import com.mobium.reference.utils.statistics_new.data_receivers.INavigationDataReceiver;
import com.mobium.reference.utils.statistics_new.data_receivers.IOrderDataReceiver;
import com.mobium.reference.utils.statistics_new.data_receivers.IRegionsDataReceiver;
import com.mobium.reference.utils.statistics_new.data_receivers.ISearchDataReceiver;

/**
 *  on 29.09.15.
 */
public class Events implements IEvents {
    private static volatile Events instance;
    private final IEventReceiver impl;
    private final boolean debug = false;


    private Events(Activity activity) {
        this.impl = new EventsImplementation(activity, debug) ;
    }

    @Override
    public IAppDataReceiver app() {
        return impl;
    }

    @Override
    public IBannersDataReceiver banners() {
        return impl;
    }

    @Override
    public ICartDataReceiver cart() {
        return impl;
    }

    @Override
    public ICatalogDataReceiver catalog() {
        return impl;
    }

    @Override
    public IConnectivityDataReceiver connectivity() {
        return impl;
    }

    @Override
    public IFavouritesDataReceiver favourites() {
        return impl;
    }

    @Override
    public INavigationDataReceiver navigation() {
        return impl;
    }

    @Override
    public IOrderDataReceiver order() {
        return impl;
    }

    @Override
    public IRegionsDataReceiver regions() {
        return impl;
    }

    @Override
    public ISearchDataReceiver search() {
        return impl;
    }

    @Override
    public ILifeCycleListener lifeCycle() {
        return impl;
    }

    public static IEvents get(Activity activity) {
        Events localInstance = instance;
        if (localInstance == null)
            synchronized (Events.class) {
                localInstance = instance;
                if (localInstance == null)
                    instance = localInstance = new Events(activity);
            }
        return localInstance;
    }
}
