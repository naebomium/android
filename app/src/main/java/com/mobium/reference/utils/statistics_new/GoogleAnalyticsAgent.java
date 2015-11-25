package com.mobium.reference.utils.statistics_new;

import android.app.Activity;
import android.app.Application;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mobium.client.models.CartItem;
import com.mobium.client.models.ShopCategory;

/**
 *  on 26.09.15.
 */
class GoogleAnalyticsAgent extends StatisticAgent {
    private final GoogleAnalytics analytics;
    private final Tracker tracker;


    public GoogleAnalyticsAgent(Activity activity, String key, boolean debug) {
        analytics = GoogleAnalytics.getInstance(activity.getApplicationContext());
        analytics.setDryRun(debug);
        tracker = analytics.newTracker(key);
        tracker.enableExceptionReporting(true);

    }

    @Override
    public void onEvent(Event<?> event) {
        switch (event.type) {
            case make_order:
                Event.MakeOrder.Data makeOrderData = ((Event.MakeOrder) event).data;
                tracker.send(new HitBuilders.TransactionBuilder()
                                .setTransactionId(makeOrderData.data.getId())
                                .setAffiliation("")
                                .setRevenue(makeOrderData.data.getCost())
                                .setTax(0)
                                .setShipping(0)
                                .build()
                );

                for (CartItem cartItem : makeOrderData.items) {
                    tracker.send(new HitBuilders.ItemBuilder()
                                    .setTransactionId(makeOrderData.data.getId())
                                    .setName(cartItem.shopItem.title)
                                    .setSku(cartItem.shopItem.id)
                                    .setCategory(cartItem.shopItem.categoryId().orElse("undefined"))
                                    .setPrice(cartItem.shopItem.cost.getCurrentConst())
                                    .setQuantity(cartItem.count)
                                    .build()
                    );
                }

//                tracker.send(new HitBuilders.EventBuilder()
//                                .setAction(event.type.name())
//                                .setCategory(event.type.group.name())
//                                .setLabel(getProductString(makeOrderData.items))
//                                .setValue(makeOrderData.data.getCost())
//                                .build()
//                );
                break;

            case make_call:
              //  sendEvent(event.type,  String.format("Phone number dialed: '%s'", ((Event.MakeCall) event).data), null);
                break;
            case send_feedback:
                sendEvent(event.type,"Feedback sent", null);
                break;
            case open_banner:
                sendEvent(event.type, String.format("Banner '%s' opened", ((Event.OpenBanner) event).data.id), null);
                break;

            case remove_from_cart:
                Event.RemoveFromCart removeFromCart = (Event.RemoveFromCart) event;
                sendEvent(event.type,
                        String.format("Item '%s' removed from cart", removeFromCart.data.shopItem.id),
                        (long) removeFromCart.data.count
                );
                break;
            case add_to_cart:
                Event.AddToCart addToCart = (Event.AddToCart) event;
                sendEvent(event.type,
                        String.format("Item '%s' added to cart", addToCart.data.shopItem.id),
                        (long) addToCart.data.count
                );
                break;
            case open_from_cart:
                Event.OpenFromCart openFromCart = (Event.OpenFromCart) event;
                sendEvent(event.type,
                        String.format("Item '%s' opened from cart",openFromCart.data.shopItem.id),
                        (long) openFromCart.data.count
                );
                break;
            case search:
                sendEvent(event.type, String.format("Search request: '%s'", ((Event.Search) event).data.query), null);
                break;
            case app_start_from_push:
                sendEvent(event.type, String.format("App started from push '%s'", ((Event.AppStartFromPush) event).data), null);
                break;
            case open_barcode_scanner:
                sendEvent(event.type, null, null);
                break;
            case open_offer:
                Event.OpenProduct openProduct = ((Event.OpenProduct) event);
                sendEvent(event.type, String.format("Offer '%s' opened", openProduct.data.getRealId().orElse(openProduct.data.getId())), null);
                break;
            case open_category:
                Event.OpenCategory openCategory = ((Event.OpenCategory) event);
                sendEvent(event.type, String.format("Category '%s' opened", openCategory.data.id), null);
                break;
            case open_from_favorites:
                Event.OnOpenFromFavourite open = (Event.OnOpenFromFavourite) event;
                sendEvent(event.type, String.format("Item '%s' open from favorites", open.data.getRealId().orElse(open.data.id)), null);
                break;
            case add_to_favorites:
                Event.AddToFavourites add = (Event.AddToFavourites) event;
                sendEvent(event.type, String.format("Item '%s' added to favorites", add.data.getRealId().orElse(add.data.getId())), null);
                break;
            case remove_from_favorites:
                Event.RemoveFromFavourites remove = (Event.RemoveFromFavourites) event;
                sendEvent(event.type, String.format("Item %s' removed from favorites", remove.data.getRealId().orElse(remove.data.id)), null);
                break;
            case choose_city:
                Event.ChangeRegion changeRegion = (Event.ChangeRegion) event;
                sendEvent(event.type, String.format("City changed: '%s'", changeRegion.data.getGooglePlacesId().orElse("undefined")), null);
                break;
            case open_filters:
                ShopCategory category = ((Event.OpenFilters) event).data;
                sendEvent(event.type, String.format("Filters '%s' opened", category.id), null);
                break;
            case open_page:
                String pageName = ((Event.OpenPage) event).data;
                sendEvent(event.type, (String.format("Screen '%s' opened", pageName)), null);
                tracker.setPage(((Event.OpenPage) event).data);
                break;

        }
    }

    private void sendEvent(EventType type, String data, Long value) {
        sendEvent(type.group.name(), type.name(), data, value);
    }

    private void sendEvent(String category, String action, String label, Long value) {
        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
                .setAction(action)
                .setCategory(category)
                .setLabel(label);
        if (value != null)
            builder.setValue(value);
        tracker.send(builder.build());
    }

    @Override
    public void onStart(Activity context) {
        analytics.reportActivityStart(context);
    }

    @Override
    public void onStop(Activity context) {
        analytics.reportActivityStop(context);
    }

    @Override
    public void onApplicationStart(Application application) {

    }

    public static String getProductString(CartItem items[]) {
        return Stream.of(items)
                .map(item -> item.shopItem.getId() + "(" + item.count + ")")
                .collect(Collectors.joining());
    }

}
