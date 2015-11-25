package com.mobium.new_api.methodParameters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.mobium.client.models.ShoppingCart;
import com.mobium.new_api.models.Region;
import com.mobium.new_api.models.ShopPoint;

import java.util.List;

/**
 *  on 11.07.15.
 * http://mobiumapps.com/
 */
public class GetShopPointParam {
    public final
    @NonNull
    String regionId;
    public final
    @Nullable
    String type;
    public final
    @Nullable
    Order order;


    public GetShopPointParam(@NonNull Region region, @Nullable ShopPoint.ShopPointType type) {
        regionId = region.getId();
        this.type = type == null ? null : type.getId();
        this.order = null;
    }

    public GetShopPointParam(@NonNull Region region, @Nullable ShopPoint.ShopPointType type, Order order) {
        regionId = region.getId();
        this.type = type == null ? null : type.getId();
        this.order = order;
    }

    public GetShopPointParam(@NonNull String regionId, @Nullable String type) {
        this.regionId = regionId;
        this.type = type;
        order = null;
    }

    public String getRegionId() {
        return regionId;
    }

    public String getType() {
        return type;
    }


    public static class Order {
        public String price;
        public String deliveryType;
        public Item[] items;
        public String regionId;
        public String paymentType;

        public Order(Region region, String deliveryType, ShoppingCart cart) {
            this.price = String.valueOf(cart.getItemsCost());
            this.deliveryType = deliveryType;
            this.items = Item.getItems(cart);
            this.regionId = region.getId();
            this.paymentType = "0";
        }

        protected Order() {
        }
    }

    public static class Item {
        public String id;
        public String count;

        public Item(String id, int count) {
            this.id = id;
            this.count = String.valueOf(count);
        }

        public static Item[] getItems(ShoppingCart cart) {

            List<Item> items = Stream.of(cart.getItems()).map(value -> new Item(value.shopItem.getId(), value.count)).collect(Collectors.toList());

            Item[] result = new Item[items.size()];
            items.toArray(result);
            return result;
        }
    }

    public static GetShopPointParam getShopPointParam(Region region, ShopPoint.ShopPointType type, String deliveryType, ShoppingCart cart) {
        if (cart != null && deliveryType != null)
            return new GetShopPointParam(region, type, new Order(region, deliveryType, cart));
        else
            return new GetShopPointParam(region, type, null);
    }

}
