package com.mobium.config.models;

import android.support.annotation.Nullable;
import android.view.View;

import com.mobium.client.models.Action;

/**
 *  on 02.11.15.
 */
public interface ItemWithCost {
    String name();
    String cost();
    @Nullable String oldCost();
    String iconUrl();
    Action onClick();
}
