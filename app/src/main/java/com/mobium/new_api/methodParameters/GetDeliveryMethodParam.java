package com.mobium.new_api.methodParameters;

import java.util.HashMap;
import java.util.List;

/**
 *  on 13.10.15.
 */
public class GetDeliveryMethodParam {
    private int price;
    private String regionId;
    private OrderItem[] items;

    public GetDeliveryMethodParam(int price, String regionId, OrderItem[] items) {
        this.price = price;
        this.regionId = regionId;
        this.items = items;
    }

    public GetDeliveryMethodParam(int price, String regionId, List<OrderItem> items) {
        this.price = price;
        this.regionId = regionId;
        this.items = items.toArray(new OrderItem[items.size()]);
    }



    public static class OrderItem {
        public final String id;
        public final int count;

        public OrderItem(String id, int count) {
            this.id = id;
            this.count = count;
        }

    }

    public int getPrice() {
        return price;
    }

    public String getRegionId() {
        return regionId;
    }

    public OrderItem[] getItems() {
        return items;
    }
}
