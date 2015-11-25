package com.mobium.reference.utils.statistics_new;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.mobileapptracker.MATEventItem;
import com.mobileapptracker.MobileAppTracker;
import com.mobium.client.models.CartItem;
import com.mobium.reference.utils.statistics_new.Event;
import com.mobium.reference.utils.statistics_new.StatisticAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  on 26.09.15.
 */
class MobileAppTrackingAgent extends StatisticAgent {
    private final MobileAppTracker tracker;
    private volatile boolean registered;
    private final List<Event<?>> pendingEvents = new ArrayList<>();


    public MobileAppTrackingAgent (Context context, String advertiserId, String conversionKey, boolean debug) {
        MobileAppTracker.init(context, advertiserId, conversionKey);
        tracker = MobileAppTracker.getInstance();
        tracker.setDebugMode(debug);
        new MobileAppTrackerInitialisation(context).execute();
    }


    @Override
    public void onEvent(Event<?> event) {
        if (!registered) {
            pendingEvents.add(event);
            return;
        }

        switch (event.type) {
            case make_order:
                Event.MakeOrder.Data eventData = ((Event.MakeOrder) event).data;
                List eventList = cartItemsToPseudoEventList(eventData.items);
                tracker.measureAction("purchase", eventList, (double) eventData.data.getCost(), "RUB", eventData.data.getId());
                break;
        }
    }


    @Override
    public void onStart(Activity context) {
        tracker.setReferralSources(context);
        tracker.measureSession();
    }

    @Override
    public void onStop(Activity context) {

    }

    @Override
    public void onApplicationStart(Application application) {

    }

    private void runPendingEvents() {
        registered = true;
        for (Event event: pendingEvents)
            onEvent(event);

        pendingEvents.clear();
    }


    /**
     * Регистрация mobileAppTracking в сети
     */
    private class MobileAppTrackerInitialisation extends AsyncTask<Void, Void, AdvertisingIdClient.Info> {
        private final Context context;

        private MobileAppTrackerInitialisation(Context context) {
            this.context = context;
        }

        @Override
        protected AdvertisingIdClient.Info doInBackground(Void... voids) {
            if (checkGooglePSAvailable(context)) {
                try {
                    return AdvertisingIdClient.getAdvertisingIdInfo(context.getApplicationContext());
                } catch (IOException | NullPointerException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                    registerByAppId();
                }
            } else {
                registerByAppId();
            }
            return null;
        }

        private void registerByAppId() {
            tracker.setAndroidId(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
            runPendingEvents();
        }

        @Override
        protected void onPostExecute(AdvertisingIdClient.Info adInfo) {
            if (adInfo != null) {
                tracker.setGoogleAdvertisingId(adInfo.getId(), adInfo.isLimitAdTrackingEnabled());
                runPendingEvents();
            }
        }

    }

    private static boolean checkGooglePSAvailable(Context context) {
        try {
            final int googlePSAvailabilityCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
            return googlePSAvailabilityCode == ConnectionResult.SUCCESS ||
                    googlePSAvailabilityCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static List cartItemsToPseudoEventList(CartItem[] items) {
        String name = "";
        int cost = 0;
        for (CartItem cartItem : items) {
            name += cartItem.shopItem.title + " : " + cartItem.count + ";  \n";
            cost += cartItem.shopItem.cost.getCurrentConst() * cartItem.count;
        }
        return Collections.singletonList(new MATEventItem(name, 1, cost, cost));
    }
}
