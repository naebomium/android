package com.mobium.new_api.models;

/**
 *  on 19.06.15.
 * http://mobiumapps.com/
 *
 *
 * request with param and extra fields
 */
public class RequestExtra<T, E> extends Request<T> {
    public E extra;

    public RequestExtra(String request, String codeRevision, String version, String protocolVersion, String buildId, String key, String appId) {
        super(request, codeRevision, version, protocolVersion, buildId, key, appId);
    }

    public RequestExtra<T, E> setParamExtra(T param, E extra) {
        this.param = param;
        this.extra = extra;
        return this;
    }

    public RequestExtra<T, E> setExtra(E extra) {
        this.extra = extra;
        return this;
    }
}
