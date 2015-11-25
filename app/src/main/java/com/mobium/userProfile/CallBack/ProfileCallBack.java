package com.mobium.userProfile.CallBack;

import android.support.annotation.Nullable;

import com.mobium.userProfile.Response;

import retrofit.RetrofitError;

/**
 *  on 11.06.15.
 * http://mobiumapps.com/
 */

public abstract class ProfileCallBack<Param> extends CancelableCallBack<Response<Param>> {

    public abstract void onSuccess(Param data);

    public abstract void onAbort(Response.ResponseStatus type, @Nullable Param data);

    public abstract void onError(RetrofitError error);


    @Override
    public final void failure(RetrofitError error) {
        if (error.getResponse() != null)
            try {
                Response response = (Response) error.getBodyAs(Response.class);
                onAbort(response.getType(), null);
            } catch (Exception e) {
                onError(error);
            }
        else
            onError(error);
    }


    @Override
    public final void success(Response<Param> response, retrofit.client.Response rawResponse) {
        super.success(response, rawResponse);
        if (response.status == Response.ResponseStatus.OK)
            onSuccess(response.data);
        else
            onAbort(response.status, response.data);
    }

}
