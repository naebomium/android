package com.mobium.reference.view;

import android.content.DialogInterface;

import com.mobium.client.models.CartItem;
import com.mobium.client.models.ShopItem;

import java.util.List;

/**
 *  on 05.10.15.
 */
public interface PreCartView {
    void showCartItem(CartItem item);

    void showRelatedItems(List<ShopItem> itemList, String label);

    void exit();
    void showError(String title, String message, DialogInterface.OnClickListener apply, DialogInterface.OnClickListener cancel);

    void updateCounts(CartItem cartItem);

}
