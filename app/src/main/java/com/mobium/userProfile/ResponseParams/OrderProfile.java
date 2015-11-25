package com.mobium.userProfile.ResponseParams;

import android.support.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.mobium.client.models.CartItem;
import com.mobium.new_api.models.order.IOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *  on 11.06.15.
 * http://mobiumapps.com/
 */
public class OrderProfile implements Serializable, IOrder {
    public  String id;
    public  String status;
    private float total;
    public  String deliveryType;
    public  ArrayList<OrderItem> items;
    private transient ArrayList<CartItem> chacheItems;

    public OrderProfile(String id, String status, float total, String deliveryType, ArrayList<OrderItem> items) {
        this.id = id;
        this.status = status;
        this.total =  total;
        this.deliveryType = deliveryType;
        this.items = items;
    }

    public OrderProfile() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public void setItems(ArrayList<OrderItem> items) {
        this.items = items;
    }


    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getCost() {
        if (items == null)
            return (int) total;
        int result = 0;
        for (OrderItem item : items)
            result += item.price * item.count;
        return result;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (items != null)
            for (OrderItem item : items)
                count += item.count;
        return count;
    }

    @Override
    public Optional<Long> getDate() {
        return Optional.empty();
    }

    @Override
    public Map<String, String> getUserData() {
        return Collections.emptyMap();
    }


    public List<String> getRealId() {
        return Stream.of(items).map(orderItem -> orderItem.id).collect(Collectors.toList());
    }

    public @Nullable ArrayList<CartItem> getCacheItems() {
        return chacheItems;
    }

    public void setCachedItems(ArrayList<CartItem> cachedItems) {
        this.chacheItems = cachedItems;
    }
}
