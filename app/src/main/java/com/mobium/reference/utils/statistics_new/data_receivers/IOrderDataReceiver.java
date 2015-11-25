package com.mobium.reference.utils.statistics_new.data_receivers;

import com.mobium.client.models.CartItem;
import com.mobium.new_api.models.order.IOrder;

/**
 *  on 29.09.15.
 */
public interface IOrderDataReceiver {
    void onMakeOrder(IOrder order, CartItem orderItems[]);
}
