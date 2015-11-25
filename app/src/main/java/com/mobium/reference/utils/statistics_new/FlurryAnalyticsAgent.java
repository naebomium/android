package com.mobium.reference.utils.statistics_new;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.flurry.android.FlurryAgent;
import com.mobium.client.models.CartItem;
import com.mobium.client.models.ShopCategory;
import com.mobium.client.models.ShopItem;
import com.mobium.new_api.models.Banner;
import com.mobium.new_api.models.Region;

import java.util.HashMap;
import java.util.Map;

/**
 *  on 26.09.15.
 */
class FlurryAnalyticsAgent extends StatisticAgent {
    private final String id;


    public FlurryAnalyticsAgent(String flurryId, boolean debug) {
        id = flurryId;
    }

    private void sendEvent(EventType eventType, @Nullable Map<String, String> params) {
        FlurryAgent.logEvent(eventType.name(), params);
    }

    @Override
    public void onEvent(Event<?> event) {
        final Map<String, String> params = new HashMap<>();
        CartItem cartItem;
        ShopItem shopItem;

        switch (event.type) {
            case make_order:
                Event.MakeOrder.Data eventData = ((Event.MakeOrder) event).data;
                params.put("id", eventData.data.getId());
                params.put("price", "" + eventData.data.getCost());
                params.put("count", "" + eventData.data.getCount());
                params.put("items", Stream.of(eventData.items).map(
                        item -> item.shopItem.getId() + ":" + item.count + ","
                ).collect(Collectors.joining()));
                sendEvent(event.type, params);
                break;
            case app_start:
                String appId = ((Event.AppStart) event).data;
                params.put("app_id", appId);
                sendEvent(event.type, params);
                break;
            case app_start_from_push:
                String pushId = ((Event.AppStart) event).data;
                params.put("push_id", pushId);
                sendEvent(event.type, params);
                break;
            case add_to_cart:
                cartItem = ((Event.AddToCart) event).data;
                sendEvent(event.type, getParamsFromCartItem(cartItem));
                break;
            case remove_from_cart:
                cartItem = ((Event.RemoveFromCart) event).data;
                sendEvent(event.type, getParamsFromCartItem(cartItem));
                break;
            case remove_from_favorites:
                shopItem = ((Event.RemoveFromFavourites) event).data;
                sendEvent(event.type, getParamsFromCartItem(shopItem));
                break;
            case add_to_favorites:
                shopItem = ((Event.AddToFavourites) event).data;
                sendEvent(event.type, getParamsFromCartItem(shopItem));
                break;
            case choose_city:
                Region region = ((Event.ChangeRegion) event).data;
                sendEvent(event.type, getParamsRegion(region));
                break;
            case make_call:
                params.put("phone", ((Event.MakeCall) event).data);
                sendEvent(event.type, params);
                break;
            case open_barcode_scanner:
                sendEvent(event.type, null);
                break;
            case send_feedback:
                Event.SendFeedBack.MessageData messageData = ((Event.SendFeedBack)event).data;
                params.put("message", messageData.message);
                params.put("email", messageData.email);
                sendEvent(event.type, params);
                break;
            case search:
                Event.Search.SearchData searchData = ((Event.Search) event).data;
                params.put("query", searchData.query);
                if (searchData.categotyId != null) params.put("category_id", searchData.categotyId);
                sendEvent(event.type, params);
                break;
            case open_banner:
                Banner item = ((Event.OpenBanner) event).data;
                params.put("banner_id", item.getId());
                params.put("banner_type", item.getActionData());
                sendEvent(event.type, params);
                break;
            case open_category:
                ShopCategory category = ((Event.OpenCategory) event).data;
                sendEvent(event.type, getParamsFromCategory(category));
                break;
        }
    }

    private Map<String, String> getParamsFromCategory(ShopCategory category) {
        return new HashMap<String, String>() {
            {
                put("id", category.id);
                put("title", category.title);
            }
        };
    }


    public static Map<String, String> getParamsFromCartItem(ShopItem item) {
        return new HashMap<String, String>() {
            {
                put("id", item.getId());
                put("price", item.cost.getCurrentConst() + "");
                put("title", item.title);
            }
        };

    }
    public static Map<String, String> getParamsFromCartItem(CartItem item) {
        return new HashMap<String, String>() {
            {
                put("id", item.shopItem.getId());
                put("price", item.shopItem.cost.getCurrentConst() + "");
                put("title", item.shopItem.title);
                put("count", item.count + "");
            }
        };
    }

    public static Map<String, String> getParamsRegion(Region region) {
        return new HashMap<String, String>() {
            {
                put("id", region.getId());
                put("title", region.getTitle());
                put("type", region.getType().name());
                region.getGooglePlacesId().ifPresent(value -> put("place_id", value));
            }
        };
    }


    @Override
    public void onStart(Activity context) {
        if (!FlurryAgent.isSessionActive())
            FlurryAgent.onStartSession(context);
    }

    @Override
    public void onStop(Activity context) {
        FlurryAgent.onEndSession(context);
    }

    @Override
    public void onApplicationStart(Application application) {
        FlurryAgent.init(application, id);
        FlurryAgent.onStartSession(application);
    }
}
