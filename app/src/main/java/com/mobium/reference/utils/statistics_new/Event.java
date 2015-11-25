package com.mobium.reference.utils.statistics_new;

import com.mobium.client.models.CartItem;
import com.mobium.new_api.models.order.IOrder;

import com.mobium.client.models.ShopCategory;
import com.mobium.client.models.ShopItem;
import com.mobium.new_api.models.Banner;
import com.mobium.new_api.models.Region;

/**
 *  on 24.09.15.
 */
class Event<T> {
    public final EventType type;
    public final T data;

    public Event(EventType type, T data) {
        this.type = type;
        this.data = data;
    }

    public static class AddToCart extends Event<CartItem> {
        public AddToCart(CartItem data) {
            super(EventType.add_to_cart, data);
        }
    }

    public static class OpenFromCart extends Event<CartItem> {
        public OpenFromCart(CartItem data) {
            super(EventType.open_from_cart, data);
        }
    }

    public static class RemoveFromCart extends Event<CartItem> {
        public RemoveFromCart(CartItem data) {
            super(EventType.remove_from_cart, data);
        }
    }

    public static class AddToFavourites extends Event<ShopItem> {
        public AddToFavourites(ShopItem data) {
            super(EventType.add_to_favorites, data);
        }
    }

    public static class RemoveFromFavourites extends Event<ShopItem> {
        public RemoveFromFavourites(ShopItem data) {
            super(EventType.remove_from_favorites, data);
        }
    }

    public static class OpenPage extends Event<String> {
        public OpenPage(String pageName) {
            super(EventType.open_page, pageName);
        }
    }

    public static class MakeCall extends Event<String> {
        public MakeCall(String pageName) {
            super(EventType.make_call, pageName);
        }
    }

    public static class SendFeedBack extends Event<SendFeedBack.MessageData> {
        public SendFeedBack(MessageData pageName) {
            super(EventType.send_feedback, pageName);
        }

        public static final class MessageData {
            public final String email;
            public final String message;

            public MessageData(String email, String message) {
                this.email = email;
                this.message = message;
            }
        }
    }

    public static class Search extends Event<Search.SearchData> {
        public Search(SearchData pageName) {
            super(EventType.search, pageName);
        }

        public static final class SearchData {
            public final String query;
            public final String categotyId;

            public SearchData(String query, String categotyId) {
                this.query = query;
                this.categotyId = categotyId;
            }
        }
    }

    public static class ChangeRegion extends Event<Region> {
        public ChangeRegion(Region pageName) {
            super(EventType.choose_city, pageName);
        }
    }

    public static class OpenBanner extends Event<Banner> {
        public OpenBanner(Banner data) {
            super(EventType.open_banner, data);
        }
    }

    public static class AppStart extends Event<String> {
        public AppStart(String appId) {
            super(EventType.app_start, appId);
        }
    }

    public static class AppStartFromPush extends Event<String> {
        public AppStartFromPush(String pushId) {
            super(EventType.app_start_from_push, pushId);
        }
    }

    public static class MakeOrder extends Event<MakeOrder.Data> {
        public MakeOrder(MakeOrder.Data data) {
            super(EventType.make_order, data);
        }

        public static class Data {
            public final CartItem[] items;
            public final IOrder data;

            public Data(CartItem[] items, IOrder data) {
                this.items = items;
                this.data = data;
            }
        }
    }

    public static class OpenProduct extends Event<ShopItem> {
        public OpenProduct(ShopItem data) {
            super(EventType.open_offer, data);
        }
    }

    public static class OpenCategory extends Event<ShopCategory> {
        public OpenCategory(ShopCategory data) {
            super(EventType.open_category, data);
        }
    }

    public static class OpenFilters extends Event<ShopCategory> {
        public OpenFilters(ShopCategory data) {
            super(EventType.open_filters, data);
        }
    }

    public static class OpenBarcodeScanner extends Event<Object> {
        public OpenBarcodeScanner() {
            super(EventType.open_barcode_scanner, new Object());
        }
    }

    public static class OnOpenFromFavourite extends Event<ShopItem> {
        public OnOpenFromFavourite(ShopItem item) {
            super(EventType.open_from_favorites, item);
        }
    }

    @Override
    public String toString() {
        return "Event{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }
}
