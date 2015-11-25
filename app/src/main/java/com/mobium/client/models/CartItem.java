package com.mobium.client.models;

import java.io.Serializable;

/**
 *  IDEA.
 *
 * Date: 23.09.11
 * Time: 22:01
 * To change this template use File | Settings | File Templates.
 */
public class CartItem implements Serializable{
    public final ShopItem shopItem;
    public int count;

    public CartItem(ShopItem shopItem, int count) {
        this.shopItem = shopItem;
        this.count = count;
    }
}
