package com.mobium.userProfile;

import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

/**
 *  on 11.06.15.
 * http://mobiumapps.com/
 */

public class Response<T> {
    public ResponseStatus status;

    public T data;

    public Response(ResponseStatus status, T data) {
        this.status = status;
        this.data = data;
    }


    public Response() {
    }


    public void setStatus(ResponseStatus type) {
        this.status = type;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseStatus getType() {
        return status;
    }

    public T getData() {
        return data;
    }

    public enum ResponseStatus {
        @SerializedName("ok")
        OK,

        @SerializedName("error")
        ERROR,

        @SerializedName("auth_error")
        NEED_AUTH,

        @SerializedName("activationRequired")
        NEED_ACTIVATION
    }
}
