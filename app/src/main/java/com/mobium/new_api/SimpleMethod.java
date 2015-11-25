package com.mobium.new_api;

import com.mobium.new_api.models.Response;
import com.mobium.new_api.models.ResponseBase;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 *  on 19.06.15.
 * http://mobiumapps.com/
 *
 * method without argument
 */

public class SimpleMethod<Data extends ResponseBase>  {
    public static ExceptionHandler staticHandler;
    private final SimpleMethodInterface<Data> methodImplementation;
    private final Receiver<Data> internalReceiver;

    public SimpleMethod(SimpleMethodInterface<Data> methodImplementation) {
        this.methodImplementation = methodImplementation;
        internalReceiver = null;
    }

    public SimpleMethod(SimpleMethodInterface<Data> methodImplementation,
                        Receiver<Data> internalReceiver) {
        this.methodImplementation = methodImplementation;
        this.internalReceiver = internalReceiver;
    }

    public void call(SimpleHandler<Data> handler) {
        try {
            methodImplementation.call(new Callback<Response<Data>>() {
                @Override
                public void success(Response<Data> dataResponse, retrofit.client.Response response) {
                    if (dataResponse.success()) {
                        handler.onResult(dataResponse.result);
                        if (internalReceiver != null)
                            internalReceiver.onResult(dataResponse.result);
                    } else
                        handler.onBadArgument(dataResponse.error());
                }

                @Override
                public void failure(RetrofitError error) {
                    handler.onException(error);
                    if (staticHandler!= null)
                        staticHandler.onExeption(error);
                }
            });
        } catch (Exception e) {
            handler.onException(e);
            if (staticHandler!= null)
                staticHandler.onExeption(e);
        }
    }


}
