package com.mobium.new_api.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.UUID;

/**
 *  on 19.06.15.
 * http://mobiumapps.com/
 *
 * server request with param field
 */
public class Request<T> implements Serializable {
    @SerializedName("request")
    public String request;
    @SerializedName("codeRevision")
    public String codeRevision;
    @SerializedName("guid")
    public String guid;
    @SerializedName("version")
    public String version;
    @SerializedName("protocolVersion")
    public String protocolVersion;
    @SerializedName("buildId")
    public String buildId;
    @SerializedName("key")
    public String key;
    @SerializedName("appId")
    public String appId;

    public T param;

    public Request(String request,
                   String codeRevision,
                   String version,
                   String protocolVersion,
                   String buildId,
                   String key,
                   String appId) {
        this.request = request;
        this.codeRevision = codeRevision;
        this.version = version;
        this.protocolVersion = protocolVersion;
        this.buildId = buildId;
        this.key = key;
        this.appId = appId;
        guid = UUID.randomUUID().toString();
    }

    public Request<T> setParam(T param) {
        this.param = param;
        return this;
    }


    public Request(T param, String method) {
        this.param = param;
        this.request = method;
    }
}
