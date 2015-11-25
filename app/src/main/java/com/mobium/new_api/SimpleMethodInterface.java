package com.mobium.new_api;

import com.mobium.new_api.models.Response;
import com.mobium.new_api.models.ResponseBase;

import retrofit.Callback;

/**
 *  on 19.06.15.
 * http://mobiumapps.com/
 *
 *
 * method without argument implementation (retrofit invoke)
 */
public interface SimpleMethodInterface<Data extends ResponseBase> {
    void call(Callback<Response<Data>> result);
}
