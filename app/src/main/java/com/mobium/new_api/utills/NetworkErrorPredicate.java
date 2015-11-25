package com.mobium.new_api.utills;

import com.annimon.stream.function.Predicate;
import retrofit.RetrofitError;

/**
 *  on 12.07.15.
 * http://mobiumapps.com/
 */
public class NetworkErrorPredicate implements Predicate<Exception> {
    @Override
    public boolean test(Exception e) {
        if (e instanceof RetrofitError) {
            RetrofitError retrofitError = (RetrofitError) e;
            RetrofitError.Kind kind = retrofitError.getKind();
            return kind == RetrofitError.Kind.NETWORK;
        }
        return false;
    }
}
