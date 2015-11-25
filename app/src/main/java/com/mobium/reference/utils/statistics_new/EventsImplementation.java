package com.mobium.reference.utils.statistics_new;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mobium.client.models.CartItem;
import com.mobium.new_api.models.order.IOrder;
import com.mobium.client.models.ShopCategory;
import com.mobium.client.models.ShopItem;
import com.mobium.new_api.models.Banner;
import com.mobium.new_api.models.Region;
import com.mobium.reference.ReferenceApplication;
import com.mobium.reference.utils.NetworkUtils;
import com.mobium.config.common.Config;
import com.mobium.reference.utils.statistics_new.data_receivers.IEventReceiver;

import java.util.ArrayList;
import java.util.List;

/**
 *  on 24.09.15.
 */
class EventsImplementation implements IEventReceiver, EventReceiver {
    private static boolean hasStart = false;
    private final List<StatisticAgent> agents;
    private List<Event<?>> pendingEvents = new ArrayList<>();


    public EventsImplementation(Activity activity, boolean debug) {
        agents = new ArrayList<>();

        agents.add(new PushGoalAgent(10));

        String adId = Config.get().getApplicationData().getServiceMatAdvertiser();
        String conK = Config.get().getApplicationData().getServiceMatConversation();
        if (adId != null && conK != null)
            agents.add(new MobileAppTrackingAgent(activity, adId, conK, debug));

        String flurryOd = Config.get().getApplicationData().getServiceFlurryId();
        if (flurryOd != null)
            agents.add(new FlurryAnalyticsAgent(flurryOd, debug));

        String googleAnalytics = Config.get().getApplicationData().getServiceGoogleAnalyticsId();
        if (googleAnalytics != null)
            agents.add(new GoogleAnalyticsAgent(activity, googleAnalytics, debug));

        onApplicationStart(activity.getApplication());

    }


    @Override
    public void onEvent(Event<?> event) {
        if (NetworkUtils.isOnline(ReferenceApplication.getInstance())) {
            for (EventReceiver handler : agents)  {
                handler.onEvent(event);
                for (Event pendingEvent : pendingEvents)
                    handler.onEvent(pendingEvent);
            }
            pendingEvents.clear();
        }
        else
            pendingEvents.add(event);
        Log.i("EventsImplementation", event.toString());
    }

    @Override
    public void onMakeCall(String cellNumber) {
        onEvent(new Event.MakeCall(cellNumber));
    }

    @Override
    public void onSendFeedback(String message, String email) {
        onEvent(new Event.SendFeedBack(new Event.SendFeedBack.MessageData(email, message)));
    }

    @Override
    public void onSearch(String query, @Nullable String openedCategoryId) {
        onEvent(new Event.Search(new Event.Search.SearchData(query, openedCategoryId)));
    }

    @Override
    public void onRegionSelect(Region newRegion) {
        onEvent(new Event.ChangeRegion(newRegion));
    }

    @Override
    public void onBannerSelect(Banner bannerItem) {
        onEvent(new Event.OpenBanner(bannerItem));
    }

    @Override
    public void onAppStart(@Nullable String appId) {
        onEvent(new Event.AppStart(appId));
    }

    @Override
    public void onAppStartFromPush(String pushId) {
        onEvent(new Event.AppStartFromPush(pushId));
    }

    @Override
    public void onMakeOrder(IOrder order, CartItem[] orderItems) {
        onEvent(new Event.MakeOrder(new Event.MakeOrder.Data(orderItems, order)));
    }

    @Override
    public void onProductOpened(ShopItem item) {
        onEvent(new Event.OpenProduct(item));
    }

    @Override
    public void onCategoryOpened(ShopCategory category) {
        onEvent(new Event.OpenCategory(category));
    }

    @Override
    public void onFilterOpened(ShopCategory category) {
        onEvent(new Event.OpenFilters(category));
    }

    @Override
    public void onOpenBarcodeScanner() {
        onEvent(new Event.OpenBarcodeScanner());
    }

    @Override
    public void onFavoritesAdd(ShopItem item) {
        onEvent(new Event.AddToFavourites(item));
    }

    @Override
    public void onFavoritesRemove(ShopItem item) {
        onEvent(new Event.RemoveFromFavourites(item));
    }

    @Override
    public void onOpenFromFavourites(ShopItem item) {
        onEvent(new Event.OnOpenFromFavourite(item));
    }

    @Override
    public void onPageOpen(String name) {
        onEvent(new Event.OpenPage(name));
    }

    @Override
    public void onAddToCart(CartItem item) {
        onEvent(new Event.AddToCart(item));
    }

    @Override
    public void onRemoveFromCart(CartItem item) {
        onEvent(new Event.RemoveFromCart(item));
    }

    @Override
    public void onOpenFromCart(CartItem item) {
        onEvent(new Event.OpenFromCart(item));
    }

    @Override
    public void onStart(Activity context) {
        for (StatisticAgent statisticAgent: agents)
            statisticAgent.onStart(context);
    }

    @Override
    public void onStop(Activity context) {
        for (StatisticAgent statisticAgent: agents)
            statisticAgent.onStart(context);
    }

    @Override
    public void onApplicationStart(Application application) {
        for (StatisticAgent statisticAgent: agents)
            statisticAgent.onApplicationStart(application);
        hasStart = true;
    }

}
