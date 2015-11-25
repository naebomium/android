package com.mobium.config.models;


import com.mobium.client.models.Action;

/**
 *  on 02.11.15.
 */
public interface ItemWithName {
    String name();
    String url();
    Action onClick();
}
