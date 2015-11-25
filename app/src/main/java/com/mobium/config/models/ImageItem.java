package com.mobium.config.models;

import android.view.View;

import com.mobium.client.models.Action;

/**
 *  on 02.11.15.
 */
public interface ImageItem {
    String imageUrl();
    View.OnClickListener onClick();
}
