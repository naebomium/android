package com.mobium.reference.utils.statistics_new.data_receivers;

import com.mobium.client.models.CartItem;

/**
 *  on 29.09.15.
 */
public interface ICartDataReceiver {
    void onAddToCart(CartItem item);
    void onRemoveFromCart(CartItem item);
    void onOpenFromCart(CartItem item);
}
