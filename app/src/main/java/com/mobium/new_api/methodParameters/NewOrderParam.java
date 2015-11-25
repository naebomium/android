package com.mobium.new_api.methodParameters;

import java.util.HashMap;
import java.util.Map;

/**
 *  on 13.10.15.
 */
public class NewOrderParam {
    private boolean fake = false;
    private String regionId;
    private String deliveryType;
    private int paymentType;
    private int price;
    private HashMap<String, String> orderInfo;

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    Item[] items;

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public void setFake(boolean fake) {
        this.fake = fake;
    }

    private String pointId;

    public NewOrderParam setRegionId(String regionId) {
        this.regionId = regionId;
        return this;
    }

    public NewOrderParam setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
        return this;
    }

    public NewOrderParam setPaymentType(int paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public NewOrderParam setPrice(int price) {
        this.price = price;
        return this;
    }

    public NewOrderParam setOrderInfo(HashMap<String, String> orderInfo) {
        this.orderInfo = orderInfo;
        return this;
    }

    public NewOrderParam() {
    }


    public String getRegionId() {
        return regionId;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public int getPrice() {
        return price;
    }

    public Map<String, String> getOrderInfo() {
        return orderInfo;
    }

    public static class Item {
        public String id;
        public int count;
        public Map<String, String> modifications;

        public Item(String id, int count, Map<String, String> modifications) {
            this.id = id;
            this.count = count;
            this.modifications = modifications;
        }
    }
}
