package com.mobium.client.models;

import android.content.Context;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.mobium.client.LogicUtils;
import com.mobium.reference.utils.persistence.ContextPersistence;

import java.util.*;


public class ShoppingCart extends ContextPersistence {
    private HashMap<String, CartItem> items = new HashMap<>();

    private final transient HashSet<LogicUtils.OnChangeCartListener> listeners = new HashSet<>() ;

    public ShoppingCart(Context context) {
        super(context);
    }


    public void addCartListener(LogicUtils.OnChangeCartListener listener) {
        listeners.add(listener);
    }

    public void removeCartListener(LogicUtils.OnChangeCartListener listener) {
        listeners.remove(listener);
    }

    public CartItem[] getItems() {
        Collection<CartItem> var = items.values();
        return var.toArray(new CartItem[var.size()]);
    }

    public void addItem(ShopItem item, int count) {
        if (items.containsKey(item.id)) {
            items.get(item.id).count += count;
            if (items.get(item.id).count <= 0) {
                items.remove(item.id);
            }
        } else {
            items.put(item.id, new CartItem(item, count));
        }
        Stream.of(listeners).forEach(l -> l.onChange(new HashSet<>(items.values())));
    }

    public void addItem(ShopItem item) {
        addItem(item, 1);
    }

    public int getItemCount(String id) {
        if (items.containsKey(id)) {
            return items.get(id).count;
        } else {
            return 0;
        }
    }


    public int getItemsCount() {
        int res = 0;

        for (CartItem item : items.values()) {
            res += item.count;
        }
        return res;
    }

    public int getItemsCost() {
        int res = 0;
        for (CartItem item : items.values()) {
            res += item.count * item.shopItem.cost.getCurrentConst();
        }
        return res;
    }

    public void clear() {
        items.clear();
        Stream.of(listeners).forEach(l -> l.onChange(Collections.<CartItem>emptySet()));
    }

    public void notifyListeners() {
        Stream.of(listeners).forEach(l -> l.onChange(new HashSet<>(items.values())));
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }


    public Optional<CartItem> getItem(String id) {
        return Optional.ofNullable(items.get(id));
    }
}
