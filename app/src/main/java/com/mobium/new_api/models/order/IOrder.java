package com.mobium.new_api.models.order;

import com.annimon.stream.Optional;

import java.io.Serializable;
import java.util.Map;

/**
 *  on 16.07.15.
 * http://mobiumapps.com/
 */
public interface IOrder  extends Serializable {
    String getStatus();
    String getId();
    int getCost();
    int getCount();
    Optional<Long> getDate();
    Map<String, String> getUserData();
}
