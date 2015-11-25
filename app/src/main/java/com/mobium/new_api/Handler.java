package com.mobium.new_api;

import com.mobium.new_api.models.MobiumError;

/**
 *  on 20.06.15.
 * http://mobiumapps.com/
 */
public interface Handler<Arg, Data> {
    void onResult(Data data);

    void onBadArgument(MobiumError mobiumError, Arg arg);

    void onException(Exception ex);
}
