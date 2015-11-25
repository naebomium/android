package com.mobium.client.models;

import android.support.annotation.Nullable;

import com.annimon.stream.Objects;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.mobium.new_api.models.order.IOrder;
import com.mobium.new_api.models.order.OrderItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 *  on 16.05.15.
 * http://mobiumapps.com/
 */
public class SuccessOrderData implements Serializable, IOrder {
    public static final String newOrder = "Заказ отправлен в магазин";

    private final String orderId;
    private final int cost;
    private String status;

    private long time;

    private boolean profile;

    //список товаров и модификаций
    private List<OrderItem> items;
    private transient List<CartItem> chacheItems;
    private Map<String, String> userData;

    public SuccessOrderData(String orderId, int cost, String status) {
        this.orderId = orderId;
        this.cost = cost;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public int getCount() {
        int result = 0;
        for (OrderItem item : items)
            result+=item.count;
        return result;
    }

    @Override
    public Optional<Long> getDate() {
        return Optional.ofNullable(time == 0 ? null : time);
    }

    @Override
    public Map<String, String> getUserData() {
        return userData;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String getId() {
        return orderId;
    }


    public List<OrderItem> getItemCounts() {
        return items;
    }



    public SuccessOrderData setItemCounts(List<OrderItem> items) {
        this.items = items;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<String> getProductIds() {
        List<String> ids = new ArrayList<>();
        Stream.of(items).forEach(item -> {
                    ids.add(item.itemId);
                }
        );
        return ids;
    }


    public int getTotalItemsCount() {
        int result = 0;
        for (OrderItem item : items)
            result += item.count;
        return result;
    }

    public void setProfile(boolean profile) {
        this.profile = profile;
    }

    public boolean isProfile() {
        return profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuccessOrderData data = (SuccessOrderData) o;
        return Objects.equals(cost, data.cost) &&
                Objects.equals(time, data.time) &&
                Objects.equals(profile, data.profile) &&
                Objects.equals(orderId, data.orderId) &&
                Objects.equals(status, data.status) &&
                Objects.equals(items, data.items);
    }

    @Override
    public int hashCode() {
        int result = orderId != null ? orderId.hashCode() : 0;
        result = 31 * result + cost;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + (profile ? 1 : 0);
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }


    public String getFirstId() {
        return items.iterator().next().itemId;
    }


    public void setCacheItems(ArrayList<CartItem> items) {
        chacheItems = items;
    }

    public @Nullable List<CartItem> getChacheItems() {
        return chacheItems;
    }

    public void setUserData(Map<String, String> userData) {
        this.userData = userData;
    }
}
