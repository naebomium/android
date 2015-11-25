package com.mobium.new_api.models.order;

import java.io.Serializable;
import java.util.Map;

/**
 *  on 21.09.2015.
 */
public class OrderItem implements Serializable {

    public String itemId;
    public int count;
    //modification id, selected modification id
    public Map<String, String> modifications;

    public OrderItem(String itemId, int count, Map<String, String> modifications) {
        this.itemId = itemId;
        this.count = count;
        this.modifications = modifications;
    }

}
