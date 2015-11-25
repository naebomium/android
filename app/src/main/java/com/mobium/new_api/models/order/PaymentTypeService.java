package com.mobium.new_api.models.order;

import java.io.Serializable;

/**
 *  on 13.10.15.
 */
public class PaymentTypeService implements Serializable  {
    public final String id;
    public PaymentTypeService(String id) {
        this.id = id;
    }
}
