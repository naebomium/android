package com.mobium.new_api;

import retrofit.Callback;

/**
 *  on 19.06.15.
 * http://mobiumapps.com/
 * method interface (retrofit invoke)
 */
public interface MethodInterface<Arg, Response> {
    void call(Arg request, Callback<Response> result);
}
