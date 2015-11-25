package com.mobium.new_api.models;

import com.google.gson.annotations.SerializedName;
import com.mobium.new_api.models.order.NewOrderResult;

import org.parceler.Parcel;

/**
 *  on 19.06.15.
 * http://mobiumapps.com/
 *
 * server response with response field
 */

public class Response<Result extends ResponseBase> {
    @SerializedName("response")
    public Result result;


    public Response() {
    }

    public Response(Result result) {
        this.result = result;
    }

    public MobiumError error() {
        if(result != null)
            return new MobiumError(result.getError(), result.getDescription(), result.isMayRetry());
        return new MobiumError("empty result", "unknown error", false);
    }

    public boolean success() {
       return result != null && result.getError() == null;
    }

    public String getError() {
        return result.getError();
    }


    public String getDescription() {
        return result.getDescription();
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
