package com.mobium.new_api;

import com.mobium.new_api.models.MobiumError;

/**
 *  on 20.06.15.
 * http://mobiumapps.com/
 *
 * Handling data of method without argument
 */
public interface SimpleHandler<Data> {
    void onResult(Data data);

    void onBadArgument(MobiumError mobiumError);

    void onException(Exception ex);
}
