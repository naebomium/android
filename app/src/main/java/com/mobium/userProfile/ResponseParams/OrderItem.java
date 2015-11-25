package com.mobium.userProfile.ResponseParams;

import java.io.Serializable;

/**
 *  on 16.07.15.
 * http://mobiumapps.com/
 */
public final class OrderItem implements Serializable {
    public  String id;
    public  Integer count;
    public  float price;

    public OrderItem(String id, Integer count, float price) {
        this.id = id;
        this.count = count;
        this.price = price;
    }

    public OrderItem() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
