package com.mobium.new_api.methodParameters;

/**
 *  on 19.06.15.
 * http://mobiumapps.com/
 */
public class RegisterPushTokenParam {
    public final String service;
    public final String token;

    public RegisterPushTokenParam(String service, String token) {
        this.service = service;
        this.token = token;
    }
}
