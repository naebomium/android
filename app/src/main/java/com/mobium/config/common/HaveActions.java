package com.mobium.config.common;
import com.mobium.client.models.Action;

/**
 *  on 03.11.15.
 */
public interface HaveActions {
    void setActionHandler(Handler<Action> handler);
}
